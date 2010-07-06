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
     * @param prefix
     *            The property key prefix.
     * @param configuration
     *            The properties file.
     */
    public void initialize(String prefix, VariableProperties configuration) {
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