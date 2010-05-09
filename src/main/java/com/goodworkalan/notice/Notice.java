package com.goodworkalan.notice;

import com.goodworkalan.diffuse.Diffuser;
import com.goodworkalan.diffuse.ObjectDiffuser;

/**
 * A structured program instrumentation message.
 *
 * @author Alan Gutierrez
 */
public abstract class Notice {
    /** The static diffuser used by all notice instances. */
    final static Diffuser diffuser = new Diffuser();
    
    /**
     * Assign the given diffuse object converter to the given object type. The
     * assignment will be inherited by any subsequently created child class
     * loaders of the associated class loader, but not by existing child class
     * loaders.
     * <p>
     * The converter will be assigned to a map of converters that is associated
     * with the <code>ClassLoader</code> of the given object type through a weak
     * reference so that the converter can be collected if an application
     * container reloads an application's libraries.
     * 
     * @param type
     *            The object type.
     * @param converter
     *            The object converter.
     */
    public static void setObjectDiffuser(Class<?> type, ObjectDiffuser converter) {
        diffuser.setConverter(type, converter);
    }

    /**
     * Put a recursive diffusion of the given object into the notice with the
     * given key including only the given object paths in the recursive
     * diffusion.
     * 
     * @param key
     *            The map key.
     * @param object
     *            The object to diffuse and add to map.
     * @param includes
     *            The paths to include in the recursive diffusion.
     * @return This notice to continue to build the notice.
     */
    public abstract Notice put(String name, Object object);

    public abstract Notice put(String name, Object object, String... paths);

    public abstract Notice put(String name, Object object, boolean recurse);

    /**
     * Create a map building language element to build a map that will be
     * written to the log flagged with the given property name.
     * 
     * @param name
     *            The property name.
     * @return A map building language element to build a map to log.
     */
    public abstract Mapper<Notice> map(String key);

    /**
     * Create a map building language element to build a map that will be
     * written to the log flagged with the given property name.
     * 
     * @param name
     *            The property name.
     * @return A map building language element to build a map to log.
     */
    public abstract Lister<Notice> list(String key);

    /**
     * Write the notice to the given message sink.
     */
    public abstract void send(Sink sink);

    /**
     * Start a stop watch with the given name. The stop watch will run until
     * either the stop method is called with the stop watch name or the notice
     * is sent to a sink.
     * 
     * @param name
     *            The stop watch name.
     * @return This notice in order to chain method calls.
     */
    public abstract Notice start(String name);
    
    public abstract Notice stop(String name);

    /**
     * Write the notice to the default message sink.
     */
    public abstract void send();
}