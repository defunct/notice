package com.goodworkalan.notice;

public class NullSpecific extends Generic<NullSpecific> {
    @Override
    protected NullSpecific getSelf() {
        return this;
    }
    
    @Override
    public NullSpecific foo() {
        return this;
    }
}
