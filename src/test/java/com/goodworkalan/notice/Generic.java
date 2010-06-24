package com.goodworkalan.notice;

// TODO Document.
public abstract class Generic<Self> {
    // TODO Document.
    protected abstract Self getSelf();

    // TODO Document.
    public Self foo() {
        return getSelf();
    }
}
