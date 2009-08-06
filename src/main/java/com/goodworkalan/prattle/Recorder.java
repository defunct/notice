package com.goodworkalan.prattle;

import java.util.Map;

public interface Recorder
{
    public void record(Map<String, Object> map);
    
    public void initialize(String prefix, Configuration configuration);
    
    public void close();
}
