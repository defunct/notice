package com.goodworkalan.notice;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public abstract class Notice<Self> implements Noticeable<Self> {
    final static Object SKIP = new Object();
    
    final static Set<String> DEEP = Collections.emptySet();
    
    final static Set<String> SHALLOW = Collections.singleton("\0");
    
    /** Cache of resource bundles. */
    private final static ConcurrentMap<String, ResourceBundle> bundles = new ConcurrentHashMap<String, ResourceBundle>();
    
    private final String context;
    
    private static final Map<Class<?>, Converter> defaultConverters = new ConcurrentHashMap<Class<?>, Converter>();
    
    /** The map of object converters. */
    private static final WeakHashMap<ClassLoader, ConcurrentMap<Class<?>, Converter>> classLoaderConverters = new WeakHashMap<ClassLoader, ConcurrentMap<Class<?>,Converter>>();
    
    /** The log line. */
    private final Map<String, Object> line;
    
    /** The log entry variables. */
    private final Map<String, Object> objects;

    static {
        defaultConverters.put(Byte.class, NullConverter.INSTANCE);
        defaultConverters.put(Boolean.class, NullConverter.INSTANCE);
        defaultConverters.put(Short.class, NullConverter.INSTANCE);
        defaultConverters.put(Character.class, NullConverter.INSTANCE);
        defaultConverters.put(Integer.class, NullConverter.INSTANCE);
        defaultConverters.put(Long.class, NullConverter.INSTANCE);
        defaultConverters.put(Float.class, NullConverter.INSTANCE);
        defaultConverters.put(Double.class, NullConverter.INSTANCE);
        defaultConverters.put(String.class, NullConverter.INSTANCE);
        defaultConverters.put(Object.class, BeanConverter.INSTANCE);
        defaultConverters.put(Map.class, MapConverter.INSTANCE);
        defaultConverters.put(Collection.class, CollectionConverter.INSTANCE);
        defaultConverters.put(URL.class, ToStringConverter.INSTANCE);
        defaultConverters.put(URL.class, ToStringConverter.INSTANCE);
        defaultConverters.put(URI.class, ToStringConverter.INSTANCE);
        defaultConverters.put(Class.class, ClassConverter.INSTANCE);
        defaultConverters.put(CharSequence.class, ToStringConverter.INSTANCE);
        defaultConverters.put(Date.class, DateConverter.INSTANCE);
    }
    
    protected Notice(Notice<?> entry, String context, Object...extras) {
        this.context = context;
        this.objects = entry.objects;
        this.line = entry.line;
        this.initialize(extras);
    }
    
    protected Notice(String context, Object...extras) {
        this.context = context;
        this.objects = new LinkedHashMap<String, Object>();
        this.line = new LinkedHashMap<String, Object>();
        this.initialize(extras);
    }
    
    private void initialize(Object...extras) {
        for (int i = 0; i < extras.length; i += 2) {
            line.put(extras[i].toString(), flatten(extras[i + 1]));
        }
        line.put("vars", Collections.unmodifiableMap(objects));
    }
    
    private static Map<Class<?>, Converter> getClassLoaderConverters(ClassLoader classLoader) {
        synchronized (Notice.classLoaderConverters) {
            ConcurrentMap<Class<?>, Converter> converters = Notice.classLoaderConverters.get(classLoader);
            if (converters == null) {
                converters = new ConcurrentHashMap<Class<?>, Converter>(Notice.getConverters(classLoader.getParent()));
                Notice.classLoaderConverters.put(classLoader, converters);
            }
            return converters;
        }
    }
    
    private static Map<Class<?>, Converter> getConverters(ClassLoader classLoader) {
        if (classLoader == null) {
            return defaultConverters;
        }
        return Notice.getClassLoaderConverters(classLoader);
    }
    
    public static void setConverter(Class<?> type, Converter converter) {
        Notice.getConverters(type.getClassLoader()).put(type, converter);
    }
    
    public static void toString(Class<?> toStringClass) {
        Notice.setConverter(toStringClass, ToStringConverter.INSTANCE);
    }
    
    public static Converter getConverter(Class<?> type) {
        if (type.isArray()) {
            return ArrayConverter.INSTANCE;
        }
        if (type.isPrimitive()) {
            return NullConverter.INSTANCE;
        }
        boolean encache = false;
        Class<?> iterator = type;
        Map<Class<?>, Converter> converters = Notice.getConverters(type.getClassLoader());
        for (;;) {
            Converter converter = converters.get(iterator);
            if (converter == null) {
                encache = true;
                converter = Notice.interfaceConverter(converters, iterator.getInterfaces());
            }
            if (converter != null) {
                if (encache) {
                    converters.put(type, converter);
                }
                return converter;
            }
            encache = true;
            iterator = iterator.getSuperclass();
        }
    }

    public abstract Self getSelf();
    
    protected void remove(String name) {
        objects.remove(name);
    }
    
    protected void recordName(String name) {
    }
    
    public Map<String, Object> getObjects() {
        return objects;
    }

    /**
     * Freeze the given object, copying all arrays and Java collections classes,
     * turning all the classes specified in the list classes into frozen beans.
     * 
     * @param object
     *            The object to freeze.
     * @param freeze
     *            The set of classes to freeze when encountered.
     * @return A frozen object.
     */
    public static Object flatten(Object object, Set<String> includes) {
        if (object == null) {
            return null;
        }
        return getConverter(object.getClass()).convert(object, new StringBuilder(), includes);
    }

    public static Object flatten(Object object) {
        if (object == null) {
            return null;
        }
        return getConverter(object.getClass()).convert(object, new StringBuilder(), Collections.singleton("\0"));
    }
    
    public static Object flatten(Object object, boolean recurse) {
        if (object == null) {
            return null;
        }
        return getConverter(object.getClass()).convert(object, new StringBuilder(), recurse ? Collections.<String>emptySet() : Collections.singleton("\0"));
    }

    public static Object flatten(Object object, String...includes) {
        if (object == null) {
            return null;
        }
        return getConverter(object.getClass()).convert(object, new StringBuilder(), new HashSet<String>(Arrays.asList(includes)));
    }
    
    private static Converter interfaceConverter(Map<Class<?>, Converter> converters, Class<?>[] ifaces) {
        LinkedList<Class<?>> queue = new LinkedList<Class<?>>(Arrays.asList(ifaces));
        while (!queue.isEmpty()) {
            Class<?> iface = queue.removeFirst();
            Converter converter = converters.get(iface);
            if (converter != null) {
                return converter;
            }
            queue.addAll(Arrays.asList((Class<?>[]) iface.getInterfaces()));
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#put(java.lang.String, java.lang.Object)
     */
    public Self put(String name, Object object) {
        recordName(name);
        objects.put(name, flatten(object, SHALLOW));
        return getSelf();
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#put(java.lang.String, java.lang.Object, java.lang.String)
     */
    public Self put(String name, Object object, String...paths) {
        recordName(name);
        objects.put(name, flatten(object, new HashSet<String>(Arrays.asList(paths))));
        return getSelf();
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#put(java.lang.String, java.lang.Object, boolean)
     */
    public Self put(String name, Object object, boolean recurse) {
        recordName(name);
        objects.put(name, flatten(object, recurse ? DEEP : SHALLOW));
        return getSelf();
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#map(java.lang.String)
     */
    public Mapper<Self> map(String name) {
        recordName(name);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        objects.put(name, Collections.unmodifiableMap(map));
        return new CoreMapper<Self>(getSelf(), map);
    }
    
    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#list(java.lang.String)
     */
    public Lister<Self> list(String name) {
        recordName(name);
        List<Object> list = new ArrayList<Object>();
        objects.put(name, Collections.unmodifiableList(list));
        return new CoreLister<Self>(getSelf(), list);
    }

    /**
     * Determine whether a string represents an integer.
     * 
     * @param name
     *            The integer string.
     * @return True if the string represents an integer.
     */
    static boolean isInteger(String name) {
        for (int i = 0, stop = name.length(); i < stop; i++) {
            if (!Character.isDigit(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determine if the given name is a valid Java identifier.
     * 
     * @param name
     *            The name to check.
     * @return True if the name is valid Java identifier.
     * @exception NullPointerException
     *                If the name is null.
     */
    static boolean checkJavaIdentifier(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (name.length() == 0) {
            return false;
        }
        if (!Character.isJavaIdentifierStart(name.charAt(0))) {
            return false;
        }
        for (int i = 1, stop = name.length(); i < stop; i++) {
            if (!Character.isJavaIdentifierPart(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Evaluate the given path against the report structure.
     * 
     * @param path
     *            The path.
     * @return The value found by navigating the path or null if the path does
     *         not exist.
     * @exception IllegalArgumentException
     *                If any part of the given path is not a valid Java
     *                identifier or list index.
     */
    private Object getValue(String path) {
        int start = -1, end, stop = path.length();
        Object current = line;
        while (start != stop) {
            start++;
            end = path.indexOf('.', start);
            if (end == -1) {
                end = stop;
            }
            String name = path.substring(start, end);
            start = end;
            if (current instanceof Map<?, ?>) {
                if (!checkJavaIdentifier(name)) {
                    throw new IllegalArgumentException();
                }
                current = ((Map<?, ?>) current).get(name);
            } else if (current instanceof List<?>) {
                if (!isInteger(name)) {
                    if (!checkJavaIdentifier(name)) {
                        throw new IllegalArgumentException();
                    }
                    throw new NoSuchElementException();
                }
                int index = Integer.parseInt(name, 10);
                List<?> list = (List<?>) current;
                if (index >= list.size()) {
                    throw new NoSuchElementException();
                }
                current = list.get(index);
            } else {
                throw new NoSuchElementException();
            }
        }
        return current;
    }
    
    /**
     * Get the value in the report structure at the given path.
     * 
     * @param path
     *            The path.
     * @return The value found by navigating the path or null if the path does
     *         not exist.
     * @exception IllegalArgumentException
     *                If any part of the given path is not a valid Java
     *                identifier or list index.
     */
    public Object get(String path) {
        try {
            return getValue(path);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    public String getContext() {
        return context;
    }
    
    protected void sending() {
    }
    
    protected void sent() {
    }
    
    protected abstract String getMessageKey();

    public void send(Sink sink) {
        sending();
        line.put("message", toString());
        Sink.getInstance().send(line);
        sent();
    }
    
    public String toString() {
        int lastDot = context.lastIndexOf('.');
        String packageName = context.substring(0, lastDot == -1 ? context.length() : lastDot);
        ResourceBundle bundle = bundles.get(packageName);
        if (bundle == null) {
            try {
                bundle = ResourceBundle.getBundle(packageName + ".prattle");
            } catch (MissingResourceException e) {
                bundle = ResourceBundle.getBundle("com.goodworkalan.prattle.missing");
            }
            bundles.put(packageName, bundle);
        }
        String key = getMessageKey();
        String format;
        try {
            format = bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
        format = format.trim();
        if (format.length() == 0) {
            return key;
        }
        int tilde = format.indexOf("~");
        if (tilde == -1) {
            return format;
        }
        String paths = format.substring(0, tilde);
        format = format.substring(tilde + 1);
        int length = 0, index = 0;
        for (;;) {
            length++;
            index = paths.indexOf(',', index);
            if (index == -1) {
                break;
            }
            index += 1;
        }
        Object[] arguments = new Object[length];
        int start = -1, end, stop = paths.length(), position = 0;
        while (start != stop) {
            start++;
            end = paths.indexOf(',', start);
            if (end == -1) {
                end = stop;
            }
            String name = paths.substring(start, end);
            start = end;
            Object argument = "";
            try {
                argument = getValue(name);
            } catch (IllegalArgumentException e) {
                return key;
            } catch (NoSuchElementException e) {
                return key;
            }
            arguments[position++] = argument;
        }
        try {
            return String.format(format, arguments);
        } catch (RuntimeException e) {
            return key;
        }
    }
}
