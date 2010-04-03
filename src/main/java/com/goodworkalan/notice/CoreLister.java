package com.goodworkalan.notice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.diffuse.Diffuser;

public class CoreLister<T> implements Lister<T> {
    /** Strategy for converting objects into maps, collections and primatives. */
    private final Diffuser diffuser;
    
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
    public CoreLister(Diffuser diffuse, T parent, List<Object> list) {
        this.diffuser = diffuse;
        this.parent = parent;
        this.list = list;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Lister#add(java.lang.Object)
     */
    public Lister<T> add(Object object) {
        list.add(diffuser.diffuse(object, Notice.SHALLOW));
        return this;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Lister#add(java.lang.Object, java.lang.String)
     */
    public Lister<T> add(Object object, String... paths) {
        list.add(diffuser.diffuse(object, new HashSet<String>(Arrays.asList(paths))));
        return this;
    }
    
    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Lister#add(java.lang.Object, boolean)
     */
    public Lister<T> add(Object object, boolean recurse) {
        list.add(diffuser.diffuse(object, recurse ? Notice.DEEP : Notice.SHALLOW));
        return this;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Lister#list()
     */
    public Lister<Lister<T>> list() {
        List<Object> subList = new ArrayList<Object>();
        list.add(subList);
        return new CoreLister<Lister<T>>(diffuser, this, subList);
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Lister#map()
     */
    public Mapper<Lister<T>> map() {
        Map<String, Object> subMap = new LinkedHashMap<String, Object>();
        list.add(subMap);
        return new CoreMapper<Lister<T>>(diffuser, this, subMap);
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Lister#end()
     */
    public T end() {
        return parent;
    }
}
