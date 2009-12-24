package com.goodworkalan.notice;

import java.util.Properties;

class Configuration {
    private final Recorder recorder;
    
    private final String prefix;
    
    private final Properties properties;
    
    public Configuration(Recorder recorder, String prefix, Properties properties) {
        this.recorder = recorder;
        this.prefix = prefix;
        this.properties = properties;
    }
    
    public Recorder getRecorder() {
        return recorder;
    }

    public String getPrefix() {
        return prefix;
    }
    
    public Properties getProperties() {
        return properties;
    }
}
