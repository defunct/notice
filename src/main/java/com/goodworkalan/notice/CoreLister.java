package com.goodworkalan.notice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.diffuse.Diffuse;

public class CoreLister<T> implements Lister<T> {
    /**
     * The parent element in the domain-specific language use to create
     * messages.
     */
    private final T parent;
    
    /** The list to build. */
    private final List<Object> list;

    /**
     * Create a new list builder with the given parent langauge element and the
     * given list to build.
     * 
     * @param parent
     *            The parent element in the domain-specific language use to
     *            create messages.
     * @param list
     *            The list to build.
     */
    public CoreLister(T parent, List<Object> list) {
        this.parent = parent;
        this.list = list;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Lister#add(java.lang.Object)
     */
    public Lister<T> add(Object object) {
        list.add(Diffuse.flatten(object, Notice.SHALLOW));
        return this;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Lister#add(java.lang.Object, java.lang.String)
     */
    public Lister<T> add(Object object, String... paths) {
        list.add(Diffuse.flatten(object, new HashSet<String>(Arrays.asList(paths))));
        return this;
    }
    
    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Lister#add(java.lang.Object, boolean)
     */
    public Lister<T> add(Object object, boolean recurse) {
        list.add(Diffuse.flatten(object, recurse ? Notice.DEEP : Notice.SHALLOW));
        return this;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Lister#list()
     */
    public Lister<Lister<T>> list() {
        List<Object> subList = new ArrayList<Object>();
        list.add(subList);
        return new CoreLister<Lister<T>>(this, subList);
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Lister#map()
     */
    public Mapper<Lister<T>> map() {
        Map<String, Object> subMap = new LinkedHashMap<String, Object>();
        list.add(subMap);
        return new CoreMapper<Lister<T>>(this, subMap);
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Lister#end()
     */
    public T end() {
        return parent;
    }
}
