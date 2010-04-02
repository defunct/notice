package com.goodworkalan.notice.event;

import java.util.HashMap;
import java.util.Map;

import com.goodworkalan.notice.Notice;
import com.goodworkalan.notice.Sink;

/**
 * Default implementation of a log entry builder.
 * 
 * @author Alan Gutierrez
 */
class CoreEntry extends Notice<Entry> implements Entry {
    /** The SLF4J logger. */
    private final org.slf4j.Logger logger;

    /** The level for this entry. */
    private final Level level;

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
    public CoreEntry(org.slf4j.Logger logger, Level level, String code) {
        super(logger.getName(), "prattle", getMessageKey(logger.getName(), code), now("level", level.toString()), now("code", code));
        this.logger = logger;
        this.level = level;
    }
    
    public Entry getSelf() {
        return this;
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
    public CoreEntry start(String name) {
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
    public CoreEntry stop(String name) {
        stopWatches.get(name).stop();
        return this;
    }
    
    private static String getMessageKey(String className, String code) {
        int index = className.lastIndexOf('.');
        if (index > -1) {
            className = className.substring(index + 1);
        }
        return className + "/" + code;
    }

    protected void sending() {
        for (Map.Entry<String, StopWatch> stopWatch : stopWatches.entrySet()) {
            put(stopWatch.getKey(), stopWatch.getValue());
        }
    }
    
    protected void sent() {
        String message = (String) get("message");
        switch (level) {
        case TRACE:
            logger.trace(message);
            break;
        case DEBUG:
            logger.debug(message);
            break;
        case INFO:
            logger.info(message);
            break;
        case WARN:
            logger.warn(message);
            break;
        default:
            logger.error(message);
            break;
        }
    }
    
    public void send() {
        send(Sink.getInstance());
    }
}
