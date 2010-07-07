package com.goodworkalan.notice;

import java.util.Map;

import com.goodworkalan.madlib.VariableProperties;

/**
 * A configurable structured entry recorder.
 * 
 * @author Alan Gutierrez
 */
public interface Recorder {
    /**
     * Record the given message.
     * 
     * @param entry
     *            The message to record.
     */
    public void record(Map<String, Object> entry);

    /**
     * Initialize the recorder using the properties read from the given
     * properties map using keys prepended with the given property key prefix.
     * 
     * @param properties
     *            The properties map.
     * @param prefix
     *            The property key prefix.
     */
    public void initialize(VariableProperties properties, String prefix);

    /**
     * Flush all written entries to persistent storage. Used to ensure that the
     * entries are committed to disk after a batch of entries are recorded for
     * those recorders that write to file.
     */
    public void flush();

    /**
     * Terminate the recorder releasing any system resources.
     */
    public void close();
}
