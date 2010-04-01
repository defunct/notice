package com.goodworkalan.notice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.diffuse.Diffuser;

public class CoreMapper<T> implements Mapper<T> {
    private final Diffuser diffuser;
    
    private final T parent;

    private final Map<String, Object> map;

    public CoreMapper(Diffuser diffuse, T parent, Map<String, Object> map) {
        this.diffuser = diffuse;
        this.parent = parent;
        this.map = map;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Mapper#put(java.lang.String, java.lang.Object)
     */
    public Mapper<T> put(String id, Object object) {
        map.put(id, diffuser.flatten(object, Notice.SHALLOW));
        return this;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Mapper#put(java.lang.String, java.lang.Object, java.lang.String)
     */
    public Mapper<T> put(String id, Object object, String... paths) {
        map.put(id, diffuser.flatten(object, new HashSet<String>(Arrays.asList(paths))));
        return this;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Mapper#put(java.lang.String, java.lang.Object, boolean)
     */
    public Mapper<T> put(String id, Object object, boolean recurse) {
        map.put(id, diffuser.flatten(object, recurse ? Notice.DEEP : Notice.SHALLOW));
        return this;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Mapper#list(java.lang.String)
     */
    public Lister<Mapper<T>> list(String id) {
        List<Object> subList = new ArrayList<Object>();
        map.put(id, subList);
        return new CoreLister<Mapper<T>>(diffuser, this, subList);
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Mapper#map(java.lang.String)
     */
    public Mapper<Mapper<T>> map(String id) {
        Map<String, Object> subMap = new LinkedHashMap<String, Object>();
        map.put(id, subMap);
        return new CoreMapper<Mapper<T>>(diffuser, this, subMap);
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Mapper#end()
     */
    public T end() {
        return parent;
    }
}
