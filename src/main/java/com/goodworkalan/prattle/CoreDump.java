package com.goodworkalan.prattle;


class CoreDump implements Dump
{
    private final Log log;
    
    private final Object object;
    
    public CoreDump(Log log, Object object)
    {
        this.log = log;
        this.object = object;
    }

    public Object getObject()
    {
        return object;
    }

    public Log end()
    {
        return log;
    }
}
