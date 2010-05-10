package com.goodworkalan.notice;

/**
 * Builds a list that is part of a notice entry. The list will contain only
 * maps, lists and scalars, where scalars are primitives and strings. Maps and
 * lists added to the list are copied. Objects added to the list are diffused.
 * They treated as beans and their bean properties and public fields are copied
 * to a map of property names to property values.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The type of parent builder.
 */
public interface Lister<T> {
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
    public Lister<T> add(Object object, String... paths);

    /**
     * Add a list to the list and return a list builder to build the child list.
     * When the child builder terminates, it will return this list builder as
     * the parent.
     * 
     * @return A list builder to build the child list.
     */
    public Lister<Lister<T>> list();

    /**
     * Add a map to the list and return a map builder to build the child map.
     * When the child builder terminates, it will return this map builder as
     * the parent.
     * 
     * @return A map builder to build the child map.
     */
    public Mapper<Lister<T>> map();

    /**
     * Terminate the builder and return the parent builder.
     * 
     * @return The parent builder.
     */
    public T end();
}