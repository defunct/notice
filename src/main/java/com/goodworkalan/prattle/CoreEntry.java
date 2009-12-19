package com.goodworkalan.prattle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.goodworkalan.cassandra.Message;

/**
 * Default implementation of a log entry builder.
 * 
 * @author Alan Gutierrez
 */
class CoreEntry extends Entry {
    /** Cache of resource bundles. */
    private final static ConcurrentMap<String, ResourceBundle> bundles = new ConcurrentHashMap<String, ResourceBundle>();
    
    /** The SLF4J logger. */
    private final org.slf4j.Logger logger;

    /** The level for this entry. */
    private final Level level;

    /** The log message. */
    private final String message;

    /** The log entry variables. */
    private final Map<String, Object> objects = new LinkedHashMap<String, Object>();
    
    /** The log entry stop watches. */
    private final Map<String, StopWatch> stopWatches = new HashMap<String, StopWatch>();

    /**
     * Create a log entry with the gven name that will use the given SLF4J
     * logger to record the formatted log message generated by this entry.
     * 
     * @param logger
     *            The SLF4J logger.
     * @param level
     *            The log level.
     * @param name
     *            The name of the entry.
     */
    public CoreEntry(org.slf4j.Logger logger, Level level, String name) {
        this.logger = logger;
        this.level = level;
        this.message = name;
    }

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
    @Override
    public Entry start(String name) {
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
    @Override
    public Entry stop(String name) {
        stopWatches.get(name).stop();
        return this;
    }

    public Entry put(String name, Object object) {
        objects.put(name, flatten(object, SHALLOW));
        return this;
    }

    public Entry put(String name, Object object, String...paths) {
        objects.put(name, flatten(object, new HashSet<String>(Arrays.asList(paths))));
        return this;
    }

    @Override
    public Entry put(String name, Object object, boolean recurse) {
        objects.put(name, flatten(object, recurse ? DEEP : SHALLOW));
        return this;
    }

    public Lister<Entry> list(String id) {
        List<Object> list = new ArrayList<Object>();
        objects.put(id, list);
        return new CoreLister<Entry>(this, list);
    }

    public Mapper<Entry> map(String id) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        objects.put(id, map);
        return new CoreMapper<Entry>(this, map);
    }

    public void send() {
        for (Map.Entry<String, StopWatch> stopWatch : stopWatches.entrySet()) {
            put(stopWatch.getKey(), stopWatch.getValue());
        }
        String name = logger.getName();
        int lastDot = name.lastIndexOf(".");
        String packageName = name.substring(0, lastDot);
        ResourceBundle bundle = bundles.get(packageName);
        if (bundle == null) {
            try {
                bundle = ResourceBundle.getBundle(packageName + ".prattle");
            } catch (MissingResourceException e) {
                bundle = ResourceBundle.getBundle("com.goodworkalan.prattle.missing");
            }
            bundles.put(packageName, bundle);
        }
        String className = name.substring(lastDot + 1);

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        
        Thread thread = Thread.currentThread();
        
        map.put("date", new Date().getTime());
        map.put("logger", name);
        map.put("name", message);
        map.put("level", level.toString());
        map.put("threadId", thread.getId());
        map.put("threadName", thread.getPriority());

        if (objects != null) {
            map.put("vars", objects);
        }

        String msg = new Message(bundle, map, className + "/" + message, "name~%s").toString();
        map.put("message", msg);
        switch (level) {
        case TRACE:
            logger.trace(msg);
            break;
        case DEBUG:
            logger.debug(msg);
            break;
        case INFO:
            logger.info(msg);
            break;
        case WARN:
            logger.warn(msg);
            break;
        default:
            logger.error(msg);
            break;
        }
        Sink.getInstance().send(map);
    }
}
