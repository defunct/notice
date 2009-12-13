package com.goodworkalan.cassandra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds a list in the report structure of an exception.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The type of the parent builder element in the report structure or
 *            the type of exception at the root.
 */
public class MapBuilder<T> {
    /** The parent builder element or the root exception. */
    private final T parent;

    /** The map to build. */
    private final Map<String, Object> map;

    /**
     * Create a map builder with the given parent element that builds the given
     * map.
     * 
     * @param parent
     *            The parent builder element or the root exception.
     * @param map
     *            The map to build.
     */
    public MapBuilder(T parent, Map<String, Object> map) {
        this.parent = parent;
        this.map = map;
    }

    /**
     * Add the given name and given object into the map. The object must either
     * be immutable or otherwise guaranteed not to change until the exception is
     * handled.
     * 
     * @param name
     *            The name of the object in the map.
     * @param object
     *            The object to add to the map.
     * @return This map builder to allow for chained methods calls to construct
     *         the report structure.
     */
    public MapBuilder<T> put(String name, Object object) {
        if (!Message.checkJavaIdentifier(name)) {
            throw new IllegalArgumentException();
        }
        map.put(name, object);
        return this;
    }

    /**
     * Add a list to the map currently being built.
     * 
     * @param name
     *            The name of the list in the map currently being built.
     * @return A list builder for a list added to the map currently being built.
     */
    public ListBuilder<MapBuilder<T>> list(String name) {
        if (!Message.checkJavaIdentifier(name)) {
            throw new IllegalArgumentException();
        }
        List<Object> list = new ArrayList<Object>();
        map.put(name, Collections.unmodifiableList(list));
        return new ListBuilder<MapBuilder<T>>(this, list);
    }

    /**
     * Add a map to the map currently being built.
     * 
     * @param name
     *            The name of the map in the map currently being built.
     * @return A map builder for a map added to the map currently being built.
     */
    public MapBuilder<MapBuilder<T>> map(String name) {
        if (!Message.checkJavaIdentifier(name)) {
            throw new IllegalArgumentException();
        }
        Map<String, Object> subMap = new HashMap<String, Object>();
        map.put(name, Collections.unmodifiableMap(subMap));
        return new MapBuilder<MapBuilder<T>>(this, subMap);
    }

    /**
     * Return the parent builder element in the report structure.
     * 
     * @return The parent builder element in the report structure.
     */
    public T end() {
        return parent;
    }
}
