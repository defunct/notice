package com.goodworkalan.prattle;

public class NullLog implements Log
{
    public final static Log INSTANCE = new NullLog();
    
    public Log message(String format, Object... args)
    {
        return this;
    }
    
    public Log bean(String id, Object object)
    {
        return this;
    }
    
    public Log string(String id, Object object)
    {
        return this;
    }
    
    public Lister<Log> list(String id)
    {
        return new NullLister<Log>(this);
    }
    
    public Mapper<Log> map(String id)
    {
        return new NullMapper<Log>(this);
    }

    public void send()
    {
    }
}
