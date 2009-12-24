package com.goodworkalan.notice;

public interface Mapper<T> {

    public Mapper<T> put(String id, Object object);

    public Mapper<T> put(String id, Object object, String... paths);

    public Mapper<T> put(String id, Object object, boolean recurse);

    public Lister<Mapper<T>> list(String id);

    public Mapper<Mapper<T>> map(String id);

    public T end();

}