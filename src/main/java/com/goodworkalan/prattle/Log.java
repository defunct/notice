package com.goodworkalan.prattle;

public interface Log
{
    public void send();
    
    public Log bean(String id, Object object);
    
    public Log string(String id, Object object);
    
    public Mapper<Log> map(String id);
    
    public Lister<Log> list(String id);
    
    public Log message(String format, Object...args);
}
