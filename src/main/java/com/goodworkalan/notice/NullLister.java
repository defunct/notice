package com.goodworkalan.notice;

/**
 * A do nothing implementation of <code>Lister</code> returned from the do
 * nothing implementation of <code>Notice</code> that is returned from
 * <code>NoticeFactory</code> when a level is disabled.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            Type type of the parent builder.
 */
class NullLister<T> implements Lister<T> {
    /** The parent builder to return when this builder terminates. */
    private final T parent;

    /**
     * Create a null lister.
     * 
     * @param parent
     *            The parent builder to return when this builder terminates.
     */
    public NullLister(T parent) {
        this.parent = parent;
    }

    /**
     * Does nothing.
     * 
     * @param object
     *            The object to diffuse and add to the list.
     * @param paths
     *            The paths to include in the recursive diffusion.
     * @return This list builder to continue building the list.
     */
    public Lister<T> add(Object object, String... paths) {
        return this;
    }

    /**
     * A do nothing implementation that returns a do nothing implementation of
     * <code>Lister</code>.
     * 
     * @return A do nothing implementation of <code>Lister</code>.
     */
    public Lister<Lister<T>> list() {
        return new NullLister<Lister<T>>(this);
    }

    /**
     * A do nothing implementation that returns a do nothing implementation of
     * <code>Mapper</code>.
     * 
     * @return A do nothing implementation of <code>Mapper</code>.
     */
    public Mapper<Lister<T>> map() {
        return new NullMapper<Lister<T>>(this);
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
