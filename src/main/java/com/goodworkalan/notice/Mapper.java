package com.goodworkalan.notice;

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
     * key excluding the given object paths from the recursive diffusion.
     * 
     * @param key
     *            The map key.
     * @param object
     *            The object to diffuse and add to map.
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
     * Create a new list builder that will build a map list will be stored in
     * this map at the given name index. Subsequent calls to this method with
     * the name name will replace the previously created list.
     * 
     * @param name
     *            The index name.
     * @return A map builder.
     */
    public Lister<Mapper<T>> list(String name);

    /**
     * Create a new map builder that will build a map that will be stored in
     * this map at the given name index. Subsequent calls to this method with
     * the name name will replace the previously created map.
     * 
     * @param name
     *            The index name.
     * @return A map builder.
     */
    public Mapper<Mapper<T>> map(String name);

    /**
     * Terminate the builder and return the parent builder.
     * 
     * @return The parent builder.
     */
    public T end();
}