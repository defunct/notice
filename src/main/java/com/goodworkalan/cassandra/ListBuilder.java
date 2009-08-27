package com.goodworkalan.cassandra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
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
public class ListBuilder<T> {
    /** The parent builder element or the root exception. */
    private final T parent;

    /** The list to build. */
    private final List<Object> list;

    /**
     * Create a list builder with the given parent element that builds the given
     * list.
     * 
     * @param parent
     *            The parent builder element or the root exception.
     * @param list
     *            The list to build.
     */
    public ListBuilder(T parent, List<Object> list) {
        this.parent = parent;
        this.list = list;
    }

    /**
     * Add the given object to the list. The object must either be immutable or
     * otherwise guaranteed not to change until the exception is handled.
     * 
     * @param object
     *            The object to add to the list.
     * @return This list builder to allow for chained methods calls to construct
     *         the report structure.
     */
    public ListBuilder<T> add(Object object) {
        list.add(object);
        return this;
    }

    /**
     * Add a list to the list currently being built.
     * 
     * @return A list builder for a list added to the list currently being
     *         built.
     */
    public ListBuilder<ListBuilder<T>> list() {
        List<Object> subList = new ArrayList<Object>();
        list.add(Collections.unmodifiableList(subList));
        return new ListBuilder<ListBuilder<T>>(this, subList);
    }

    /**
     * Add a map to the list currently being built.
     * 
     * @return A map builder for a map added to the list currently being built.
     */
    public MapBuilder<ListBuilder<T>> map() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        list.add(Collections.unmodifiableMap(map));
        return new MapBuilder<ListBuilder<T>>(this, map);
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
