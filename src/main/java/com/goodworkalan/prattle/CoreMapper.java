package com.goodworkalan.prattle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CoreMapper<T> implements Mapper<T>
{
    private final T parent;
    
    private final Map<String, Object> map;
    
    public CoreMapper(T parent, Map<String, Object> map)
    {
        this.parent = parent;
        this.map = map;
    }
    
    public Mapper<T> put(String id, Object object)
    {
        map.put(id, Entry.flatten(object, Entry.SHALLOW));
        return this;
    }
    
    public Mapper<T> put(String id, Object object, String...paths) {
        map.put(id, Entry.flatten(object, new HashSet<String>(Arrays.asList(paths))));
        return this;
    }
    
    public Mapper<T> put(String id, Object object, boolean recurse) {
        map.put(id, Entry.flatten(object, recurse ? Entry.DEEP : Entry.SHALLOW));
        return this;
    }
    
    public Mapper<T> string(String id, Object object)
    {
        map.put(id, object.toString());
        return this;
    }
    
    public Lister<Mapper<T>> list(String id)
    {
        List<Object> subList = new ArrayList<Object>();
        map.put(id, subList);
        return new CoreLister<Mapper<T>>(this, subList);
    }
    
    public Mapper<Mapper<T>> map(String id)
    {
        Map<String, Object> subMap = new LinkedHashMap<String, Object>();
        map.put(id, subMap);
        return new CoreMapper<Mapper<T>>(this, subMap);
    }
    
    public T end()
    {
        return parent;
    }
}
