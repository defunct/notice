package com.goodworkalan.prattle;

public class NullLog implements Log
{
    public final static Log INSTANCE = new NullLog();
    
    public Log message(String format, Object... args)
    {
        return this;
    }
    
    public Dump dump(Object object)
    {
        return NullDump.INSTACE;
    }

    public void send()
    {
    }
}
