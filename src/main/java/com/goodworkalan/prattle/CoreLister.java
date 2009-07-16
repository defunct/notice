package com.goodworkalan.prattle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CoreLister<T> implements Lister<T>
{
    private final T parent;
    
    private final List<Object> list;
    
    public CoreLister(T parent, List<Object> list)
    {
        this.parent = parent;
        this.list = list;
    }
    
    public Lister<T> add(Object object)
    {
        list.add(object);
        return this;
    }
    
    public Lister<T> string(Object object)
    {
        list.add(object.toString());
        return this;
    }
    
    public Lister<Lister<T>> list()
    {
        List<Object> subList = new ArrayList<Object>();
        list.add(subList);
        return new CoreLister<Lister<T>>(this, subList);
    }
    
    public Mapper<Lister<T>> map()
    {
        Map<String, Object> subMap = new LinkedHashMap<String, Object>();
        list.add(subMap);
        return new CoreMapper<Lister<T>>(this, subMap);
    }
    
    public T end()
    {
        return parent;
    }
}
