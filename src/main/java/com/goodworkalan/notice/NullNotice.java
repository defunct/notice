package com.goodworkalan.notice;

// TODO Document.
class NullNotice extends Notice {
    // TODO Document.
    public final static Notice INSTANCE = new NullNotice();

    // TODO Document.
    public Notice start(String name) {
        return this;
    }

    // TODO Document.
    public Notice stop(String name) {
        return this;
    }

    // TODO Document.
    public Notice put(String name, Object object) {
        return this;
    }

    // TODO Document.
    public Notice put(String name, Object object, String... paths) {
        return this;
    }

    // TODO Document.
    public Notice put(String name, Object object, boolean recurse) {
        return this;
    }

    // TODO Document.
    public Notice string(String id, Object object) {
        return this;
    }

    // TODO Document.
    public Lister<Notice> list(String id) {
        return new NullLister<Notice>(this);
    }

    // TODO Document.
    public Mapper<Notice> map(String id) {
        return new NullMapper<Notice>(this);
    }
    
    // TODO Document.
    public void send(Sink sink) {
    }

    // TODO Document.
    public void send() {
    }
}
