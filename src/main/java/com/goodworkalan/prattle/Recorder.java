package com.goodworkalan.prattle;

import java.util.Map;
import java.util.Properties;

public interface Recorder
{
    public void record(Map<String, Object> map);
    
    public void initialize(String prefix, Properties properties);
    
    public void close();
}
