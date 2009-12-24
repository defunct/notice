package com.goodworkalan.notice;

import java.util.Map;

import com.goodworkalan.madlib.VariableProperties;

public interface Recorder
{
    public void record(Map<String, Object> map);
    
    public void initialize(String prefix, VariableProperties configuration);
    
    public void flush();

    public void close();
}
