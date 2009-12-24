package com.goodworkalan.notice;

public class ListerLister<T> implements Lister<T> {
    private final T parent;

    private final Lister<?> lister;

    public ListerLister(T parent, Lister<?> lister) {
        this.parent = parent;
        this.lister = lister;
    }

    public Lister<T> add(Object object) {
        lister.add(object);
        return this;
    }

    public Lister<T> add(Object object, boolean recurse) {
        lister.add(object, recurse);
        return this;
    }

    public Lister<T> add(Object object, String... paths) {
        lister.add(object, paths);
        return this;
    }

    public Lister<Lister<T>> list() {
        return new ListerLister<Lister<T>>(this, lister.list());
    }

    public Mapper<Lister<T>> map() {
        return new MapperMapper<Lister<T>>(this, lister.map());
    }

    public T end() {
        return parent;
    }
}
