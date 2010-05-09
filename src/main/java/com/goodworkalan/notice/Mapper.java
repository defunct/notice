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
     * Put a shallow diffusion of the given object in the map with the given
     * key.
     * 
     * @param key
     *            The map key.
     * @param object
     *            The object to diffuse and add to map.
     * @return This map builder to continue building the map.
     */
    public Mapper<T> put(String key, Object object);

    /**
     * Put a recursive diffusion of the given object into the map with the given
     * key including only the given object paths in the recursive diffusion.
     * 
     * @param key
     *            The map key.
     * @param object
     *            The object to diffuse and add to map.
     * @param includes
     *            The paths to include in the recursive diffusion.
     * @return This map builder to continue building the map.
     */
    public Mapper<T> put(String key, Object object, String... paths);

    /**
     * Put a recursive diffusion of the given object to the list if the given
     * recursive flag is true, shallow if it is false.
     * 
     * @param key
     *            The map key.
     * @param object
     *            The object to diffuse and add to map.
     * @return This map builder to continue building the map.
     */
    public Mapper<T> put(String key, Object object, boolean recurse);

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