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
     * Add a shallow diffusion of the given object to the list.
     * 
     * @param object
     *            The object to copy and add to list.
     * @return This list builder to continue building the list.
     */
    public Lister<T> add(Object object);

    /**
     * Add a recursive diffusion of the given object to the list excluding the
     * given object paths from the recursive copy.
     * 
     * @param object
     *            The object to copy and add to list.
     * @return This list builder to continue building the list.
     */
    public Lister<T> add(Object object, String... paths);

    /**
     * Add a recursive diffusion of the given object to the list if the given
     * recursive flag is true, shallow if it is false.
     * 
     * @param object
     *            The object to copy and add to list.
     * @return This list builder to continue building the list.
     */
    public Lister<T> add(Object object, boolean recurse);

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