package com.goodworkalan.prattle;

public class NullMapper<T> implements Mapper<T>
{
    private final T parent;
    
    public NullMapper(T parent)
    {
        this.parent = parent;
    }

    public Mapper<T> put(String id, Object object)
    {
        return this;
    }
    
    public Mapper<T> string(String id, Object object)
    {
        return this;
    }

    public Lister<Mapper<T>> list(String id)
    {
        return new NullLister<Mapper<T>>(this);
    }

    public Mapper<Mapper<T>> map(String id)
    {
        return new NullMapper<Mapper<T>>(this);
    }

    public T end()
    {
        return parent;
    }
}
