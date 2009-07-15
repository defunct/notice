package com.goodworkalan.prattle;

public interface Log
{
    public void send();
    
    public Dump dump(Object object);
    
    public Log message(String format, Object...args);
}
