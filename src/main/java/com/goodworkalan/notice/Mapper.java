package com.goodworkalan.notice;

public interface Mapper<T> {
    /**
     * Put a shallow copy of the given object in the map at the given name
     * index. A shallow copy will copy scalar and exception properties of the
     * given object, but no collections, maps or objects.
     * 
     * @param name
     *            The index name.
     * @param object
     *            The object to copy.
     * @return This map builder to continue map creation.
     */
    public Mapper<T> put(String id, Object object);

    public Mapper<T> put(String id, Object object, String... paths);

    public Mapper<T> put(String id, Object object, boolean recurse);

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
     * Terminate map creation and switch the context of the chained method calls
     * to the parent object that created this map builder.
     * 
     * @return The parent object.
     */
    public T end();
}