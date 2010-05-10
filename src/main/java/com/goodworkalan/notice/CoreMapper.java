package com.goodworkalan.notice;

import java.util.ArrayList;
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

    /**
     * Put a diffused copy of the given object into the map using the given key,
     * including all of the object paths given in includes in a recursive copy
     * of the diffused object graph. If not paths are given, the copy is not
     * recursive, a shallow diffusion of only the immediate object is added to
     * the notice. If any of the include paths given is the special path "*", a
     * recursive copy is performed that includes all objects in the object
     * graph.
     * <p>
     * There are no checks to determine if a recursively copied object is
     * visited more than once, so recursive copies of object graphs un-tempered
     * by specific include paths will result in in endless recursion. Object
     * trees present no such problems.
     * 
     * @param key
     *            The map key.
     * @param object
     *            The object to diffuse and add to map.
     * @param includes
     *            The paths to include in the recursive diffusion.
     * @return This notice to continue to build the notice.
     */
     public Mapper<T> put(String key, Object object, String... includes) {
        map.put(key, diffuser.diffuse(object, includes));
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
