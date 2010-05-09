package com.goodworkalan.notice;


class NullLister<T> implements Lister<T> {
    private final T parent;

    public NullLister(T parent) {
        this.parent = parent;
    }

    public Lister<T> add(Object object) {
        return this;
    }

    public Lister<T> add(Object object, boolean recurse) {
        return this;
    }

    public Lister<T> add(Object object, String... paths) {
        return this;
    }

    public Lister<Lister<T>> list() {
        return new NullLister<Lister<T>>(this);
    }

    public Mapper<Lister<T>> map() {
        return new NullMapper<Lister<T>>(this);
    }

    public T end() {
        return parent;
    }
}
