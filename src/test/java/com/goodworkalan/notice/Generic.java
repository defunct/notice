package com.goodworkalan.notice;

public abstract class Generic<Self> {
    
    protected abstract Self getSelf();

    public Self foo() {
        return getSelf();
    }
}
