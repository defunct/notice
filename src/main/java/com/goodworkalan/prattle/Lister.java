package com.goodworkalan.prattle;

public interface Lister<T>
{
    public Lister<T> add(Object object);
    
    public Lister<T> string(Object object);
    
    public Lister<Lister<T>> list();
    
    public Mapper<Lister<T>> map();
    
    public T end();
}
