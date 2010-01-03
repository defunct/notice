package com.goodworkalan.notice;

import java.io.File;
import java.io.StringWriter;
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
import java.util.ResourceBundle;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.goodworkalan.notice.message.Message;

/**
 * Abstract implementation of a structured logging or error message.
 * 
 * @author Alan Gutierrez
 * 
 * @param <Self>
 *            The type of the descendant class, used to return the descendant
 *            type from the chained methods that build the structure.
 */
public abstract class Notice<Self> implements Noticeable<Self> {
    /** Cache of resource bundles. */
    private final static ConcurrentMap<String, ResourceBundle> bundles = new ConcurrentHashMap<String, ResourceBundle>();

    /**
     * An set to indicate deep copy. An empty set because no includes means
     * include all.
     */
    final static Set<String> DEEP = Collections.emptySet();
    
    final static Set<String> IGNORE = new HashSet<String>(Arrays.asList(new String[]{
            "context", "threadId", "threadName", "vars", "date", "message"
    }));

    /**
     * A set to indicate a shallow copy. The set contains a single include,
     * which means check the set for container paths, but the null string will
     * match no path.
     */
    final static Set<String> SHALLOW = Collections.singleton("\0");

    /** The map of the default converters. */
    private static final Map<Class<?>, Converter> defaultConverters = new ConcurrentHashMap<Class<?>, Converter>();

    /**
     * The map of maps of object converters, indexed by class loader. The weak
     * hash map associates a map of converters with a class loader. This will
     * allow custom object converters to be garbage collected when a class loader
     * is unloaded and disposed of.
     */
    private static final WeakHashMap<ClassLoader, ConcurrentMap<Class<?>, Converter>> classLoaderConverters = new WeakHashMap<ClassLoader, ConcurrentMap<Class<?>,Converter>>();

    /**
     * The top level map that contains properties set by the notice
     * implementation. Properties added by the <code>put</code>,
     * <code>map</code> and <code>list</code> methods are assigned to the
     * <code>vars</code> map.
     */
    private final Map<String, Object> line;
    
    /** The notice variables. */
    private final Map<String, Object> vars;
    
    private final List<Property> deferred;
    
    private final Message message;

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
        defaultConverters.put(File.class, ToStringConverter.INSTANCE);
        defaultConverters.put(URL.class, ToStringConverter.INSTANCE);
        defaultConverters.put(URI.class, ToStringConverter.INSTANCE);
        defaultConverters.put(Class.class, ClassConverter.INSTANCE);
        defaultConverters.put(CharSequence.class, ToStringConverter.INSTANCE);
        defaultConverters.put(StringWriter.class, ToStringConverter.INSTANCE);
        defaultConverters.put(Date.class, DateConverter.INSTANCE);
    }

    protected static Property now(String name, Object value) {
        return new Property(name, value, SHALLOW, false);
    }
    
    protected static Property later(String name, Object value) {
        return new Property(name, value, SHALLOW, true);
    }
    
    /**
     * Construct a copy of the given notice that will use the given context to
     * find the message bundle and add the given extras to the top level map.
     * 
     * @param notice
     *            The notice to copy.
     * @param context
     *            The context.
     * @param extras
     *            An array of extra properties to add to the top level map.
     */
    protected Notice(Notice<?> notice, String context, String messageKey, Property...properties) {
        this.vars = MapConverter.INSTANCE.modifiable(notice.vars, new StringBuilder(), DEEP);
        this.line = notice.line;
        this.message = new Message(bundles, context, notice.message.getBundleName(), messageKey, notice.message.getVariables());
        this.deferred = notice.deferred;
        this.initialize(properties);
        this.line.put("context", context);
        this.line.put("threadId", notice.get("threadId"));
        this.line.put("threadName", notice.get("threadName"));
        this.line.put("date", notice.get("date"));
    }
    
    protected Notice(String context, String bundleName, String messageKey, Property...properties) {
        Thread thread = Thread.currentThread();
        
        this.vars = new LinkedHashMap<String, Object>();
        this.line = new LinkedHashMap<String, Object>();
        this.message = new Message(bundles, context, bundleName, messageKey, this.line);
        this.deferred = new ArrayList<Property>();
        this.initialize(properties);
        this.line.put("context", context);
        this.line.put("threadId", thread.getId());
        this.line.put("threadName", thread.getName());
        this.line.put("date", new Date().getTime());
    }
    
    private void initialize(Property...properties) {
        for (Property property : properties) {
            if (property.isDeferred()) {
                deferred.add(property);
            } else {
                property.put(line);
            }
        }
        line.put("vars", Collections.unmodifiableMap(vars));
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

    /**
     * Assign the given object converter to the given object type. The converter
     * will be assigned to a map of converters that is associated with the
     * <code>ClassLoader</code> of the given object type. The assignment will be
     * inherited by any subsequently created child class loaders of the
     * associated class loader, but not by existing child class loaders.
     * 
     * @param type
     *            The object type.
     * @param converter
     *            The object converter.
     */
    public static void setConverter(Class<?> type, Converter converter) {
        Notice.getConverters(type.getClassLoader()).put(type, converter);
    }

    /**
     * Assign the to string converter to the given object type. The converter
     * will be assigned to a map of converters that is associated with the
     * <code>ClassLoader</code> of the given object type. The assignment will be
     * inherited by any subsequently created child class loaders of the
     * associated class loader, but not by existing child class loaders.
     * 
     * @param type
     *            The object type.
     * @param converter
     *            The object converter.
     */
    public static void toString(Class<?> toStringClass) {
        Notice.setConverter(toStringClass, ToStringConverter.INSTANCE);
    }

    /**
     * Get the object converter for the given object type.
     * 
     * @param type
     *            The object type.
     * @return The object converter.
     */
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

    /**
     * Return this object, but cast to the type needed for the chained variable
     * map population method. Descendant classes implement this method and
     * simply return the <code>this</code> object.
     * 
     * @return This object.
     */
    public abstract Self getSelf();

    /**
     * Called when an object is added to the notice. Descendant classes can take
     * actions based on the name. This used by <code>Clue</code> in Cassandra to
     * record the name as part of the mark and clear facility.
     * 
     * @param name
     *            The name.
     */
    protected void added(String name) {
    }

    /**
     * Remove the object with the given name from the variable map.Descendant
     * classes can take can remove objects from the variable map. This used by
     * <code>Clue</code> in Cassandra to record the name as part of the mark and
     * clear facility.
     * 
     * 
     * @param name
     *            The object name.
     */
    protected void remove(String name) {
        vars.remove(name);
    }

    public Map<String, Object> getVars() {
        return vars;
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
        added(name);
        vars.put(name, flatten(object, SHALLOW));
        return getSelf();
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#put(java.lang.String, java.lang.Object, java.lang.String)
     */
    public Self put(String name, Object object, String...paths) {
        added(name);
        vars.put(name, flatten(object, new HashSet<String>(Arrays.asList(paths))));
        return getSelf();
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#put(java.lang.String, java.lang.Object, boolean)
     */
    public Self put(String name, Object object, boolean recurse) {
        added(name);
        vars.put(name, flatten(object, recurse ? DEEP : SHALLOW));
        return getSelf();
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#map(java.lang.String)
     */
    public Mapper<Self> map(String name) {
        added(name);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        vars.put(name, Collections.unmodifiableMap(map));
        return new CoreMapper<Self>(getSelf(), map);
    }
    
    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#list(java.lang.String)
     */
    public Lister<Self> list(String name) {
        added(name);
        List<Object> list = new ArrayList<Object>();
        vars.put(name, Collections.unmodifiableList(list));
        return new CoreLister<Self>(getSelf(), list);
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
        return message.get(path);
    }
    
    public String getContext() {
        return message.getContext();
    }
    
    protected void sending() {
    }
    
    protected void sent() {
    }
    
    public void send(Sink sink) {
        if (!deferred.isEmpty()) {
            for (Property property : deferred) {
                property.put(line);
            }
        }
        sending();
        line.put("message", toString());
        sink.send(line);
        sent();
    }
    
    protected final static class Property {
        private final String name;
        
        private final Object object;
        
        private final Set<String> includes;
        
        private final boolean deferred;

        public Property(String name, Object object, Set<String> includes, boolean deferred) {
            this.name = name;
            this.object = object;
            this.includes = includes;
            this.deferred = deferred;
        }
        
        protected void put(Map<String, Object> map) {
            if (!IGNORE.contains(name)) {
                map.put(name, flatten(object, includes));
            }
        }
        
        public boolean isDeferred() {
            return deferred;
        }
    }
}
