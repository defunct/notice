package com.goodworkalan.prattle;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.sun.org.apache.xerces.internal.util.URI;


public abstract class Entry {
    final static Object SKIP = new Object();
    
    final static Set<String> DEEP = Collections.emptySet();
    
    final static Set<String> SHALLOW = Collections.singleton("\0");
    
    /** The map of object converters. */
    final static ConcurrentMap<Class<?>, Converter> converters  = new ConcurrentHashMap<Class<?>, Converter>();
    
    static {
        converters.put(Byte.class, NullConverter.INSTANCE);
        converters.put(Boolean.class, NullConverter.INSTANCE);
        converters.put(Short.class, NullConverter.INSTANCE);
        converters.put(Character.class, NullConverter.INSTANCE);
        converters.put(Integer.class, NullConverter.INSTANCE);
        converters.put(Long.class, NullConverter.INSTANCE);
        converters.put(Float.class, NullConverter.INSTANCE);
        converters.put(Double.class, NullConverter.INSTANCE);
        converters.put(String.class, NullConverter.INSTANCE);
        converters.put(Object.class, BeanConverter.INSTANCE);
        converters.put(Map.class, new MapConverter());
        converters.put(Collection.class, new CollectionConverter());
        toString(URL.class);
        toString(URI.class);
        toString(Class.class);
        converters.put(Date.class, new DateConverter());
    }
    
    public static void toString(Class<?> toStringClass) {
        converters.put(toStringClass, ToStringConverter.INSTANCE);
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
        Map<Class<?>, Converter> converters = Entry.converters;
        for (;;) {
            Converter converter = converters.get(iterator);
            if (converter == null) {
                encache = true;
                converter = interfaceConverter(iterator.getInterfaces());
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
    
    private static Converter interfaceConverter(Class<?>[] ifaces) {
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

    /**
     * Write the <code>toString</code> value of the given object to the objects
     * map of the entry.
     * 
     * @param name
     *            The entry name.
     * @param object
     *            The object to convert to a string.
     * @return This entry to continue the logging statement.
     */
    public abstract Entry string(String name, Object object);

    /**
     * Flatten the given object, referencing the Entry configuration to
     * determine if the object should be converted to a <code>Map</code>
     * or converted to a <code>String</code> using <code>toString</code>.
     * <p>
     * The given object graph must be a tree. Logging cyclical graphs will
     * result in endless recursion.
     * <p>
     * Object in the tree are turned into maps of their properites, unless
     * the object has been marked as one that will be converted using either
     * the toString method or a string converter.  
     * 
     * @param name
     *            The property name.
     * @param object
     *            The object property.
     * @return This log language element to continue the logging statement.
     */
    public abstract Entry put(String name, Object object);
    
    public abstract Entry put(String name, Object object, String...paths);
    
    public abstract Entry put(String name, Object object, boolean recurse);

    /**
     * Create a map building language element to build a map that will be
     * written to the log flagged with the given property name.
     * 
     * @param name
     *            The property name.
     * @return A map building language element to build a map to log.
     */
    public abstract Mapper<Entry> map(String id);

    /**
     * Create a map building language element to build a map that will be
     * written to the log flagged with the given property name.
     * 
     * @param name
     *            The property name.
     * @return A map building language element to build a map to log.
     */
    public abstract Lister<Entry> list(String id);

    /**
     * Write the log message to the default message log and send the output
     * properties to the consumer tread.
     */
    public abstract void send();
}