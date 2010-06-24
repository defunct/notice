package com.goodworkalan.notice;

// TODO Document.
class NullMapper<T> implements Mapper<T> {
    // TODO Document.
    private final T parent;

    // TODO Document.
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

    // TODO Document.
    public T end() {
        return parent;
    }
}
