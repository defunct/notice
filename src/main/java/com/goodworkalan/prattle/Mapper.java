package com.goodworkalan.prattle;

public interface Mapper<T>
{
    public Mapper<T> put(String id, Object object);
    
    public Mapper<T> string(String id, Object object);
    
    public Mapper<Mapper<T>> map(String id);
    
    public Lister<Mapper<T>> list(String id);
    
    public T end();
}
