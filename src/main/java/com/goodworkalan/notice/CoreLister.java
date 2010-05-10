package com.goodworkalan.notice;

import java.util.ArrayList;
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
     * Add a diffused copy of the given object to the list, incluSecursive copy
     * of the diffused object graph. If not paths are given, the copy is not
     * recursive, a shallow diffusion of only the immediate object is added to
     * the notice. If any of the include paths given is the special path "*", a
     * recursive copy is performed that includes all objects in the object
     * graph.
     * <p>
     * There are no checks to determine if a recursively copied object is
     * visited more than once, so recursive copies of object graphs un-tempered
     * by specific include paths will result in in endless recursion. Object
     * trees present no such problems.
     * 
     * @param object
     *            The object to diffuse and add to the list.
     * @param includes
     *            The paths to include in the recursive diffusion.
     * @return This list builder to continue building the list.
     */
    public Lister<T> add(Object object, String... includes) {
        list.add(diffuser.diffuse(object, includes));
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
