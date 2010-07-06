package com.goodworkalan.notice;

// TODO Document.
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

    // TODO Document.
    public Mapper<T> put(String id, Object object, String... includes) {
        return this;
    }

    // TODO Document.
    public Lister<Mapper<T>> list(String id) {
        return new NullLister<Mapper<T>>(this);
    }

    // TODO Document.
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
