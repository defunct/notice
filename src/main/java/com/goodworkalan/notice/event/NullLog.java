package com.goodworkalan.notice.event;

import com.goodworkalan.notice.Lister;
import com.goodworkalan.notice.Mapper;
import com.goodworkalan.notice.Sink;

public class NullLog implements Entry {
    public final static Entry INSTANCE = new NullLog();

    public Entry start(String name) {
        return this;
    }

    public Entry stop(String name) {
        return this;
    }

    public Entry put(String name, Object object) {
        return this;
    }

    public Entry put(String name, Object object, String... paths) {
        return this;
    }

    public Entry put(String name, Object object, boolean recurse) {
        return this;
    }

    public Entry string(String id, Object object) {
        return this;
    }

    public Lister<Entry> list(String id) {
        return new NullLister<Entry>(this);
    }

    public Mapper<Entry> map(String id) {
        return new NullMapper<Entry>(this);
    }
    
    public void send(Sink sink) {
    }

    public void send() {
    }
}
