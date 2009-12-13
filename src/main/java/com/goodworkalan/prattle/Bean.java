package com.goodworkalan.prattle;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A frozen Java been that has been converted into a map.
 * 
 * @author Alan Gutierrez
 */
public class Bean {
    /** The bean class. */
    private final Class<?> beanClass;

    /** The bean properties. */
    private final Map<String, Object> properties;

    /**
     * Create a frozen bean with the given bean class and the given map of
     * properties.
     * 
     * @param beanClass
     *            The bean class.
     * @param properties
     *            The bean properties;
     */
    Bean(Class<?> beanClass, Map<String, Object> properties) {
        this.beanClass = beanClass;
        this.properties = properties;
    }

    /**
     * Get the bean class.
     * 
     * @return The bean class.
     */
    public Class<?> getBeanClass() {
        return beanClass;
    }

    /**
     * Get an immutable list of the bean properties.
     * 
     * @return The bean properties.
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * Freeze the given object, copying all arrays and Java collections classes,
     * turning all the classes specified in the list classes into frozen beans.
     * 
     * @param object
     *            The object to freeze.
     * @param freeze
     *            The list of classes to freeze when encountered.
     * @return A frozen object.
     */
    public static Object freeze(Object object, Class<?>... freeze) {
        return freeze(object, new HashSet<Class<?>>(Arrays.asList(freeze)));
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
    private static Object freeze(Object object, Set<Class<?>> freeze) {
        if (object == null) {
            return null;
        }
        if (object instanceof Map<?, ?>) {
            Map<?, ?> original = (Map<?, ?>) object;
            Map<String, Object> copy = new LinkedHashMap<String, Object>();
            for (Map.Entry<?, ?> entry : original.entrySet()) {
                copy.put(entry.getKey().toString(), freeze(entry.getValue(), freeze));
            }
            return copy;
        }
        if (object instanceof Set<?>) {
            Set<?> original = (Set<?>) object;
            Set<Object> copy = new LinkedHashSet<Object>();
            for (Object item : original) {
                copy.add(freeze(item, freeze));
            }
            return copy;
        }
        if (object instanceof Collection<?>) {
            Collection<?> original = (Collection<?>) object;
            List<Object> copy = new ArrayList<Object>();
            for (Object item : original) {
                copy.add(freeze(item, freeze));
            }
            return copy;
        }
        if (object.getClass().isAnnotation()) {
            Object[] original = (Object[]) object;
            Object[] copy = new Object[original.length];
            for (int i = 0, stop = original.length; i < stop; i++) {
                copy[i] = freeze(original[i], freeze);
            }
        }
        for (Class<?> freezeClass : freeze) {
            if (freezeClass.isAssignableFrom(object.getClass())) {
                return getInstance(object, freeze);
            }
        }
        return object;
    }

    /**
     * Create a bean instance for the given object, adding all of the readable
     * bean properties. Each property read from the bean will be in turn frozen
     * using the {@link #freeze(Object, Set) freeze} method according to the set
     * of classes to freeze when encountered.
     * 
     * @param object
     *            The object to freeze.
     * @param freeze
     *            The set of classes to freeze when encountered.
     * @return A frozen bean.
     */
    private static Bean getInstance(Object object, Set<Class<?>> freeze) {
        Class<?> beanClass = object.getClass();
        Map<String, Object> properties = new LinkedHashMap<String, Object>();
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(beanClass, Object.class);
        } catch (IntrospectionException e) {
            throw new PrattleException(0, e);
        }
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            Method read = descriptor.getReadMethod();
            if (read != null) {
                try {
                    properties.put(descriptor.getName(), freeze(read.invoke(object), freeze));
                } catch (Exception e) {
                    throw new PrattleException(0, e);
                }
            }
        }
        return new Bean(beanClass, properties);
    }
}
