package com.goodworkalan.notice;

public class MapperMapper<T> implements Mapper<T> {
    private final T parent;

    private final Mapper<?> mapper;

    public MapperMapper(T parent, Mapper<?> mapper) {
        this.parent = parent;
        this.mapper = mapper;
    }

    public Mapper<T> put(String id, Object object) {
        mapper.put(id, object);
        return this;
    }

    public Mapper<T> put(String id, Object object, boolean recurse) {
        mapper.put(id, object, recurse);
        return this;
    }

    public Mapper<T> put(String id, Object object, String... paths) {
        mapper.put(id, object, paths);
        return this;
    }

    public Lister<Mapper<T>> list(String id) {
        return new ListerLister<Mapper<T>>(this, mapper.list(id));
    }

    public Mapper<Mapper<T>> map(String id) {
        return new MapperMapper<Mapper<T>>(this, mapper.map(id));
    }

    public T end() {
        return parent;
    }
}
