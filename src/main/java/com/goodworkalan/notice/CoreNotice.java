package com.goodworkalan.notice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.goodworkalan.verbiage.Message;

/**
 * Implementation of the notice class. 
 * 
 * @author Alan Gutierrez
 */
class CoreNotice extends Notice {
    /**
     * An set to indicate deep copy. An empty set because no includes means
     * include all.
     */
    final static Set<String> DEEP = Collections.emptySet();

    /**
     * A set to indicate a shallow copy. The set contains a single include,
     * which means check the set for container paths, but the null string will
     * match no path.
     */
    final static Set<String> SHALLOW = Collections.singleton("\0");
    
    /** The notice variables. */
    private final Map<String, Object> data;
    
    /** The message format. */
    private final Message message;
    
    private final Logger logger;
    
    /**
     * The logging level. This is a value of 1 through 5 for the levels TRACE
     * through ERROR.
     */
    private final int level;

    // TODO Document.
    public CoreNotice(Message message, Map<String, Object> data, Logger logger, int level) {
        this.message = message;
        this.data = data;
        this.logger = logger;
        this.level = level;
    }
    
    /**
     * Return this object, but cast to the type needed for the chained variable
     * map population method. Descendant classes implement this method and
     * simply return the <code>this</code> object.
     * <p>
     * TODO Nice descirption.
     * 
     * @return This object.
     */
//    public abstract Self getSelf();

    /**
     * Get the map of diagnostic data.
     * 
     * @return The map of diagnostic data.
     */
    public Map<String, Object> getData() {
        return data;
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
    public Notice put(String name, Object object, String... includes) {
        data.put(name, diffuser.diffuse(object, includes));
        return this;
    }

    /**
     * Add a map to the notice using the given key and return a map builder to
     * build the child map. When the child builder terminates, it will return
     * this notice as the parent.
     * 
     * @param key
     *            The notice entry key.
     * @return A map builder to build the child map.
     */
    public Mapper<Notice> map(String name) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        data.put(name, Collections.unmodifiableMap(map));
        return new CoreMapper<Notice>(diffuser, this, map);
    }
    

    /**
     * Add a list to the notice using the given key and return a list builder to
     * build the child list. When the child builder terminates, it will return
     * this notice as the parent.
     * 
     * @param key
     *            The notice entry key.
     * @return A list builder to build the child list.
     */
    public Lister<Notice> list(String name) {
        List<Object> list = new ArrayList<Object>();
        data.put(name, Collections.unmodifiableList(list));
        return new CoreLister<Notice>(diffuser, this, list);
    }

    /**
     * Get the value in the report structure at the given path.
     * 
     * @param path
     *            The path.
     * @return The value found by navigating the path or null if the path does
     *         not exist.
     * @exception IllegalArgumentException
     *                If any part of the given path is not a valid Java
     *                identifier or list index.
     */
    public Object get(String path) {
        return message.get(path);
    }

    /**
     * Get the message context string, used to locate a message bundle.
     * 
     * @return The message context string.
     */
    public String getContext() {
        return message.getContext();
    }

    /**
     * Send the notice to the given sink. Stop watches are stopped and the error
     * message is formatted using the diagnostic data gathered.
     * 
     * @param sink
     *            The sink.
     */
    public void send(Sink sink) {
        for (Map.Entry<String, StopWatch> stopWatch : stopWatches.entrySet()) {
            put(stopWatch.getKey(), stopWatch.getValue());
        }
        String formatted = message.toString();
        data.put("$message", formatted);
        switch (level) {
        case 1:
            logger.trace(formatted);
            break;
        case 2:
            logger.debug(formatted);
            break;
        case 3:
            logger.info(formatted);
            break;
        case 4:
            logger.warn(formatted);
            break;
        case 5:
            logger.error(formatted);
        }
        sink.send(data);
    }

    /** The log entry stop watches. */
    private final Map<String, StopWatch> stopWatches = new HashMap<String, StopWatch>();

    /**
     * Start the stop watch with the given name if it is not already running. A
     * stop watch will write a duration in milliseconds into the log entry at
     * the given log entry variable name.
     * <p>
     * The stop watch can be stopped by calling the {@link #stop(String) stop}
     * method. Any stop watches running when the entry is written by the
     * {@link #send() send} method are stopped before recording.
     * 
     * @param name
     *            The stop watch name.
     * @return This entry to chain variable recording method calls.
     */
    public Notice start(String name) {
        StopWatch stopWatch = stopWatches.get(name);
        if (stopWatch == null) {
            stopWatch = new StopWatch();
            stopWatches.put(name, stopWatch);
        }
        stopWatch.start();
        return this;
    }

    /**
     * Stop the stop watch with the given name if the stop watch is running. If
     * the stop watch does does not exist, if it has not been created by calling
     * {@link #start(String) start} a {@link NullPointerException} is raised.
     * 
     * @param name
     *            The stop watch name.
     * @return This entry to chain variable recording method calls.
     * @see #start(String)
     */
    public Notice stop(String name) {
        stopWatches.get(name).stop();
        return this;
    }

    /**
     * Send the notice to the default sink instance.
     */
    public void send() {
        send(Sink.getInstance());
    }
}
