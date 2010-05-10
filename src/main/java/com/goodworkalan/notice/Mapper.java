package com.goodworkalan.notice;

/**
 * Builds a map that is part of a notice entry. The map will contain only maps,
 * lists and scalars, where scalars are primitives and strings. Maps and lists
 * added to the list are copied. Objects added to the list are diffused. They
 * treated as beans and their bean properties and public fields are copied to a
 * map of property names to property values.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The type of parent builder.
 */
public interface Mapper<T> {
    /**
     * Put a diffused copy of the given object into the map using the given key,
     * including all of the object paths given in includes in a recursive copy
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
     * @param key
     *            The map key.
     * @param object
     *            The object to diffuse and add to map.
     * @param includes
     *            The paths to include in the recursive diffusion.
     * @return This notice to continue to build the notice.
     */
    public Mapper<T> put(String key, Object object, String...includes);

    /**
     * Put a list into the map with the given key and return a list builder to
     * build the child list. When the child builder terminates, it will return
     * this list builder as the parent.
     * 
     * @return A list builder to build the child list.
     */
    public Lister<Mapper<T>> list(String name);

    /**
     * Put a map to the map with the given key and return a map builder to build
     * the child map. When the child builder terminates, it will return this map
     * builder as the parent.
     * 
     * @return A map builder to build the child map.
     */
    public Mapper<Mapper<T>> map(String name);

    /**
     * Terminate the builder and return the parent builder.
     * 
     * @return The parent builder.
     */
    public T end();
}