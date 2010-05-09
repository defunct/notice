package com.goodworkalan.notice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.diffuse.Diffuser;

/**
 * Builds a list that is part of a notice entry.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The type of parent builder.
 */
class CoreLister<T> implements Lister<T> {
    /**
     * A map of classes to strategies for converting objects of the class into a
     * tree of maps, lists and scalars, where a scalar is a primitive type or
     * string.
     */
    private final Diffuser diffuser;

    /**
     * The parent builder in the domain-specific language use to create
     * messages.
     */
    private final T parent;
    
    /** The list to build. */
    private final List<Object> list;

    /**
     * Create a new list builder with the given parent builder and the given
     * list to build.
     * 
     * @param parent
     *            The parent builder in the domain-specific language use to
     *            create messages.
     * @param list
     *            The list to build.
     */
    public CoreLister(Diffuser diffuse, T parent, List<Object> list) {
        this.diffuser = diffuse;
        this.parent = parent;
        this.list = list;
    }

    /**
     * Add a shallow copy of the given object to the list.
     * 
     * @param object
     *            The object to copy and add to list.
     * @return This list builder to continue building the list.
     */
    public Lister<T> add(Object object) {
        list.add(diffuser.diffuse(object, CoreNotice.SHALLOW));
        return this;
    }

    /**
     * Add a recursive copy of the given object to the list excluding the 
     * given object paths from the recursive copy.
     * 
     * @param object
     *            The object to copy and add to list.
     * @return This list builder to continue building the list.
     */
    public Lister<T> add(Object object, String... paths) {
        list.add(diffuser.diffuse(object, new HashSet<String>(Arrays.asList(paths))));
        return this;
    }

    /**
     * Add a recursive copy of the given object to the list if the given
     * recursive flag is true, shallow if it is false.
     * 
     * @param object
     *            The object to copy and add to list.
     * @return This list builder to continue building the list.
     */
    public Lister<T> add(Object object, boolean recurse) {
        list.add(diffuser.diffuse(object, recurse ? CoreNotice.DEEP : CoreNotice.SHALLOW));
        return this;
    }

    /**
     * Add a list to the list and return a list builder to build the child list.
     * When the child builder terminates, it will return this list builder as
     * the parent.
     * 
     * @return A list builder to build the child list.
     */
    public Lister<Lister<T>> list() {
        List<Object> subList = new ArrayList<Object>();
        list.add(subList);
        return new CoreLister<Lister<T>>(diffuser, this, subList);
    }


    /**
     * Add a map to the list and return a map builder to build the child map.
     * When the child builder terminates, it will return this map builder as
     * the parent.
     * 
     * @return A map builder to build the child map.
     */
    public Mapper<Lister<T>> map() {
        Map<String, Object> subMap = new LinkedHashMap<String, Object>();
        list.add(subMap);
        return new CoreMapper<Lister<T>>(diffuser, this, subMap);
    }

    /**
     * Terminate the builder and return the parent builder.
     * 
     * @return The parent builder.
     */
    public T end() {
        return parent;
    }
}
