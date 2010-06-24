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
     * Assign the given object diffuser to the given class.
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
     * Put a diffused copy of the given object into the notice using the given
     * key, including all of the object paths given in includes in a recursive
     * copy of the diffused object graph. If not paths are given, the copy is
     * not recursive, a shallow diffusion of only the immediate object is added
     * to the notice. If any of the include paths given is the special path "*",
     * a recursive copy is performed that includes all objects in the object
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
    public abstract Notice put(String name, Object object, String... includes);

    /**
     * Add a map to the notice using the given key and return a map builder to
     * build the child map. When the child builder terminates, it will return
     * this notice as the parent.
     * 
     * @param key
     *            The notice entry key.
     * @return A map builder to build the child map.
     */
    public abstract Mapper<Notice> map(String key);

    /**
     * Add a list to the notice using the given key and return a list builder to
     * build the child list. When the child builder terminates, it will return
     * this notice as the parent.
     * 
     * @param key
     *            The notice entry key.
     * @return A list builder to build the child list.
     */
    public abstract Lister<Notice> list(String key);

    /**
     * Write the notice to the given message sink.
     * 
     * @param sink
     *            The sink to write to.
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
    
    // TODO Document.
    public abstract Notice stop(String name);

    /**
     * Write the notice to the default message sink.
     */
    public abstract void send();
}