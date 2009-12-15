package com.goodworkalan.prattle;


public class NullLog extends Entry
{
    public final static Entry INSTANCE = new NullLog();
    
    public Entry message(String format, Object... args)
    {
        return this;
    }
    
    @Override
    public Entry start(String name) {
        return this;
    }
    
    @Override
    public Entry stop(String name) {
        return this;
    }
    
    @Override
    public Entry put(String name, Object object) {
        return this;
    }
    
    @Override
    public Entry put(String name, Object object, String... paths) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Entry put(String name, Object object, boolean recurse) {
        // TODO Auto-generated method stub
        return null;
    }

    public Entry string(String id, Object object)
    {
        return this;
    }
    
    public Lister<Entry> list(String id)
    {
        return new NullLister<Entry>(this);
    }
    
    public Mapper<Entry> map(String id)
    {
        return new NullMapper<Entry>(this);
    }

    public void send()
    {
    }
}
