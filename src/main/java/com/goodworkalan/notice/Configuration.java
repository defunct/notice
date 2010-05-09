package com.goodworkalan.notice;

import java.util.Properties;

/**
 * Configuration for differed construction of a recorder. Configuration of a
 * recorder takes place in the sink thread, if the sink is activated. It is
 * differed since two difference client threads may both attempt to initialize a
 * sink at the same time. They would both attempt to create the sink and
 * register it as the sink for a given sink name. Only the thread that
 * successfully adds the sink to the concurrent map of sink names to sinks will
 * start the sink thread. If initialization took place before the data race
 * was resolved, then identical configurations for a recorder would be initialized
 * twice during the execution of a process, possibly confusing the initialization
 * method of the recorders. 
 * 
 * @author Alan Gutierrez
 */
class Configuration {
    /** The recorder. */
    public final Recorder recorder;
    
    /** The configuration property name prefix. */
    public final String prefix;
    
    /** The configuration properties. */
    public final Properties properties;

    /**
     * Create a configuration with the given recorder, the given property name
     * prefix, and the given properties.
     * 
     * @param recorder
     *            The recorder.
     * @param prefix
     *            The property name prefix.
     * @param properties
     *            The properties.
     */
    public Configuration(Recorder recorder, String prefix, Properties properties) {
        this.recorder = recorder;
        this.prefix = prefix;
        this.properties = properties;
    }
}
