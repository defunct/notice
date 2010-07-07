package com.goodworkalan.notice;

import java.util.Map;

import com.goodworkalan.madlib.VariableProperties;
import com.goodworkalan.notice.Recorder;

/**
 * A test recorder implementation.
 *
 * @author Alan Gutierrez
 */
public class TestRecorder implements Recorder {
    /**
     * Does nothing.
     * 
     * @param properties
     *            The properties file.
     * @param prefix
     *            The property key prefix.
     */
    public void initialize(VariableProperties properties, String prefix) {
    }

    /**
     * Does nothing.
     * 
     * @param map
     *            The notice data.
     */
    public void record(Map<String, Object> map) {
    }
    
    /** Does nothing. */
    public void flush() {
    }

    /** Does nothing. */
    public void close() {
    }
}