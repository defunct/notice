package com.goodworkalan.notice;

public interface Lister<T> {

    /**
     * Add an object to the list.
     * 
     * @param object
     *            The object.
     * @return This list building language element to continue the list
     *         construction statement.
     */
    public Lister<T> add(Object object);

    public Lister<T> add(Object object, String... paths);

    public Lister<T> add(Object object, boolean recurse);

    /**
     * Add a list to the list. Calls to create lists using the list builder
     * interfaces can be nested. This method will return a list builder whose
     * {@link CoreLister#end()} method will return this list builder.
     * 
     * @return A list building language element whose parent element is this
     *         list building element.
     */
    public Lister<Lister<T>> list();

    /**
     * Add a map to the list. Calls to create lists and maps can be nested. This
     * method will return a mapper whose {@link CoreLister#end()} method will return
     * this lister.
     * 
     * @return A map building language element whose parent element is this list
     *         building element.
     */
    public Mapper<Lister<T>> map();

    /**
     * Terminate the list creation statement by returning the parent language
     * element.
     * 
     * @return The parent language element.
     */
    public T end();

}