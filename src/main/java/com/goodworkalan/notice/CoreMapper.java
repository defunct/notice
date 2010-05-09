package com.goodworkalan.notice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.diffuse.Diffuser;

/**
 * Builds a map that is part of a notice entry.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The type of parent builder.
 */
class CoreMapper<T> implements Mapper<T> {
    /** The diffuser to use to diffuse objects added to the map. */
    private final Diffuser diffuser;
    
    /** The parent builder. */
    private final T parent;

    /** The map. */
    private final Map<String, Object> map;

    /**
     * Create a new map builder with the given diffuser, the given parent
     * builder, and the given map to build.
     * 
     * @param diffuser
     *            The diffuser to use to diffuse objects added to the map.
     * @param parent
     *            The parent builder.
     * @param map
     *            The map.
     */
    public CoreMapper(Diffuser diffuse, T parent, Map<String, Object> map) {
        this.diffuser = diffuse;
        this.parent = parent;
        this.map = map;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.Mapper#put(java.lang.String, java.lang.Object)
     */
    public Mapper<T> put(String id, Object object) {
        map.put(id, diffuser.diffuse(object, CoreNotice.SHALLOW));
        return this;
    }

    /**
     * Put a recursive diffusion of the given object into the map with the given
     * key including only the given object paths in the recursive diffusion.
     * 
     * @param key
     *            The map key.
     * @param object
     *            The object to diffuse and add to map.
     * @param includes
     *            The paths to include in the recursive diffusion.
     * @return This map builder to continue building the map.
     */
    public Mapper<T> put(String key, Object object, String... includes) {
        map.put(key, diffuser.diffuse(object, new HashSet<String>(Arrays.asList(includes))));
        return this;
    }

    /**
     * Put a recursive diffusion of the given object to the list if the given
     * recursive flag is true, shallow if it is false.
     * 
     * @param key
     *            The map key.
     * @param object
     *            The object to diffuse and add to map.
     * @return This map builder to continue building the map.
     */
    public Mapper<T> put(String id, Object object, boolean recurse) {
        map.put(id, diffuser.diffuse(object, recurse ? CoreNotice.DEEP : CoreNotice.SHALLOW));
        return this;
    }

    /**
     * Put a list into the map with the given key and return a list builder to
     * build the child list. When the child builder terminates, it will return
     * this list builder as the parent.
     * 
     * @return A list builder to build the child list.
     */
    public Lister<Mapper<T>> list(String id) {
        List<Object> subList = new ArrayList<Object>();
        map.put(id, subList);
        return new CoreLister<Mapper<T>>(diffuser, this, subList);
    }

    /**
     * Put a list into the map with the given key and return a list builder to
     * build the child list. When the child builder terminates, it will return
     * this list builder as the parent.
     * 
     * @return A list builder to build the child list.
     */
    public Mapper<Mapper<T>> map(String id) {
        Map<String, Object> subMap = new LinkedHashMap<String, Object>();
        map.put(id, subMap);
        return new CoreMapper<Mapper<T>>(diffuser, this, subMap);
    }

    /**
     * Terminate the builder and return the parent builder.
     * 
     * @return The parent builder.
     */
    public T end() {
        return parent;
    }
}
