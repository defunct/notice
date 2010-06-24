package com.goodworkalan.notice;

// TODO Document.
public class NullSpecific extends Generic<NullSpecific> {
    // TODO Document.
    @Override
    protected NullSpecific getSelf() {
        return this;
    }
    
    // TODO Document.
    @Override
    public NullSpecific foo() {
        return this;
    }
}
