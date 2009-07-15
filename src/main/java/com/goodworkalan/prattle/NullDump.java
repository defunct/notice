package com.goodworkalan.prattle;

public class NullDump implements Dump
{
    public final static Dump INSTACE = new NullDump();
    
    public Log end()
    {
        return NullLog.INSTANCE;
    }
}
