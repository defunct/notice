package com.goodworkalan.prattle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds an array list of objects to report.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The parent element in the domain-specific language use to create
 *            messages.
 */
public class CoreLister<T> implements Lister<T>
{
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
    public CoreLister(T parent, List<Object> list)
    {
        this.parent = parent;
        this.list = list;
    }

    /**
     * Add an object to the list.
     * 
     * @param object
     *            The object.
     * @return This list building language element to continue the list
     *         construction statement.
     */
    public Lister<T> add(Object object)
    {
        list.add(Entry.flatten(object, Entry.SHALLOW));
        return this;
    }
    
    public Lister<T> add(Object object, String...paths) {
        list.add(Entry.flatten(object, new HashSet<String>(Arrays.asList(paths))));
        return this;
    }
    
    public Lister<T> add(Object object, boolean recurse) {
        list.add(Entry.flatten(object, recurse ? Entry.DEEP : Entry.SHALLOW));
        return this;
    }

    /**
     * Add the string value of the object to the list. This method calls
     * {@link Object#toString()} on the given object.
     * 
     * @param object
     *            The object to convert to a string.
     * @return This list building language element to continue the list
     *         construction statement.
     */
    public Lister<T> string(Object object)
    {
        list.add(object.toString());
        return this;
    }

    /**
     * Add a list to the list. Calls to create lists using the list builder
     * interfaces can be nested. This method will return a list builder whose
     * {@link Lister#end()} method will return this list builder.
     * 
     * @return A list building language element whose parent element is this
     *         list building element.
     */
    public Lister<Lister<T>> list()
    {
        List<Object> subList = new ArrayList<Object>();
        list.add(subList);
        return new CoreLister<Lister<T>>(this, subList);
    }

    /**
     * Add a map to the list. Calls to create lists and maps can be nested. This
     * method will return a mapper whose {@link Lister#end()} method will return
     * this lister.
     * 
     * @return A map building language element whose parent element is this list
     *         building element.
     */
    public Mapper<Lister<T>> map()
    {
        Map<String, Object> subMap = new LinkedHashMap<String, Object>();
        list.add(subMap);
        return new CoreMapper<Lister<T>>(this, subMap);
    }

    /**
     * Terminate the list creation statement by returning the parent language
     * element.
     * 
     * @return The parent language element.
     */
    public T end()
    {
        return parent;
    }
}
