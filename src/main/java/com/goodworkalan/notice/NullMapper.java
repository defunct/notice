package com.goodworkalan.notice;

/**
 * A do nothing implementation of <code>Mapper</code> returned from the do
 * nothing implementation of <code>Notice</code> that is returned from
 * <code>NoticeFactory</code> when a level is disabled.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            Type type of the parent builder.
 */
class NullMapper<T> implements Mapper<T> {
    /** The parent builder to return when this builder terminates. */
    private final T parent;

    /**
     * Create a null mapper.
     * 
     * @param parent
     *            The parent builder to return when this builder terminates.
     */
    public NullMapper(T parent) {
        this.parent = parent;
    }

    /**
     * Does nothing.
     * 
     * @param key
     *            The map key.
     * @param object
     *            The object to diffuse and add to map.
     * @param includes
     *            The paths to include in the recursive diffusion.
     * @return This map builder to continue building the map.
     */
    public Mapper<T> put(String key, Object object, String... includes) {
        return this;
    }

    /**
     * A do nothing implementation that returns a do nothing implementation of
     * <code>Lister</code>.
     * 
     * @param key
     *            The map key for the list value.
     * @return A do nothing implementation of <code>Lister</code>.
     */
    public Lister<Mapper<T>> list(String id) {
        return new NullLister<Mapper<T>>(this);
    }

    /**
     * A do nothing implementation that returns a do nothing implementation of
     * <code>Mapper</code>.
     * 
     * @param key
     *            The map key for the map value.
     * @return A do nothing implementation of <code>Mapper</code>.
     */
    public Mapper<Mapper<T>> map(String id) {
        return new NullMapper<Mapper<T>>(this);
    }

    /**
     * Terminate the builder by returning the parent builder to continue
     * building structured messages with chained method calls.
     * 
     * @return The parent builder.
     */
    public T end() {
        return parent;
    }
}
