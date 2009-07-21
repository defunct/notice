package com.goodworkalan.prattle;

import java.io.Serializable;

public class NullLog implements Log
{
    public final static Log INSTANCE = new NullLog();
    
    public Log message(String format, Object... args)
    {
        return this;
    }
    
    public Log object(String id, Object object)
    {
        return this;
    }
    
    public Log freeze(String name, Object object, Class<?>...freeze)
    {
        return this;
    }
    
    public Log serialize(String name, Serializable serializable)
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
