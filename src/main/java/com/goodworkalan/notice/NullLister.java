package com.goodworkalan.notice;


// TODO Document.
class NullLister<T> implements Lister<T> {
    // TODO Document.
    private final T parent;

    // TODO Document.
    public NullLister(T parent) {
        this.parent = parent;
    }

    // TODO Document.
    public Lister<T> add(Object object, String... paths) {
        return this;
    }

    // TODO Document.
    public Lister<Lister<T>> list() {
        return new NullLister<Lister<T>>(this);
    }

    // TODO Document.
    public Mapper<Lister<T>> map() {
        return new NullMapper<Lister<T>>(this);
    }

    // TODO Document.
    public T end() {
        return parent;
    }
}
