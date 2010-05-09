package com.goodworkalan.notice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    
    private final Message message;
    
    private final Sender sender;

    public CoreNotice(Message message, Map<String, Object> data, Sender sender) {
        this.message = message;
        this.data = data;
        this.sender = sender;
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

    public Map<String, Object> getData() {
        return data;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#put(java.lang.String, java.lang.Object)
     */
    public Notice put(String name, Object object) {
        data.put(name, diffuser.diffuse(object, SHALLOW));
        return this;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#put(java.lang.String, java.lang.Object, java.lang.String)
     */
    public Notice put(String name, Object object, String...paths) {
        data.put(name, diffuser.diffuse(object, new HashSet<String>(Arrays.asList(paths))));
        return this;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#put(java.lang.String, java.lang.Object, boolean)
     */
    public Notice put(String name, Object object, boolean recurse) {
        data.put(name, diffuser.diffuse(object, recurse ? DEEP : SHALLOW));
        return this;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#map(java.lang.String)
     */
    public Mapper<Notice> map(String name) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        data.put(name, Collections.unmodifiableMap(map));
        return new CoreMapper<Notice>(diffuser, this, map);
    }
    
    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#list(java.lang.String)
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
    
    public String getContext() {
        return message.getContext();
    }
    
    public void send(Sink sink) {
        for (Map.Entry<String, StopWatch> stopWatch : stopWatches.entrySet()) {
            put(stopWatch.getKey(), stopWatch.getValue());
        }
        data.put("$message", message.toString());
        sender.send(data, sink);
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
    
    public void send() {
        send(Sink.getInstance());
    }
}
