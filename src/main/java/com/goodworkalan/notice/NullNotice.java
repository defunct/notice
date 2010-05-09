package com.goodworkalan.notice;


class NullNotice extends Notice {
    public final static Notice INSTANCE = new NullNotice();

    public Notice start(String name) {
        return this;
    }

    public Notice stop(String name) {
        return this;
    }

    public Notice put(String name, Object object) {
        return this;
    }

    public Notice put(String name, Object object, String... paths) {
        return this;
    }

    public Notice put(String name, Object object, boolean recurse) {
        return this;
    }

    public Notice string(String id, Object object) {
        return this;
    }

    public Lister<Notice> list(String id) {
        return new NullLister<Notice>(this);
    }

    public Mapper<Notice> map(String id) {
        return new NullMapper<Notice>(this);
    }
    
    public void send(Sink sink) {
    }

    public void send() {
    }
}
