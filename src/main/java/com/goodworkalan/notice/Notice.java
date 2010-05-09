package com.goodworkalan.notice;

import com.goodworkalan.diffuse.Diffuser;
import com.goodworkalan.diffuse.ObjectDiffuser;

public abstract class Notice {
    /** The global diffuser. */
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
     * Flatten the given object, referencing the Entry configuration to
     * determine if the object should be converted to a <code>Map</code>
     * or converted to a <code>String</code> using <code>toString</code>.
     * <p>
     * The given object graph must be a tree. Logging cyclical graphs will
     * result in endless recursion.
     * <p>
     * Object in the tree are turned into maps of their properites, unless
     * the object has been marked as one that will be converted using either
     * the toString method or a string converter.  
     * 
     * @param name
     *            The property name.
     * @param object
     *            The object property.
     * @return This log language element to continue the logging statement.
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
    public abstract Mapper<Notice> map(String id);

    /**
     * Create a map building language element to build a map that will be
     * written to the log flagged with the given property name.
     * 
     * @param name
     *            The property name.
     * @return A map building language element to build a map to log.
     */
    public abstract Lister<Notice> list(String id);

    /**
     * Write the log message to the default message log and send the output
     * properties to the consumer tread.
     */
    public abstract void send(Sink sink);
    
    public abstract Notice start(String name);
    
    public abstract Notice stop(String name);

    public abstract void send();
}