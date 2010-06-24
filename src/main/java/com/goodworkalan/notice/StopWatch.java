package com.goodworkalan.notice;

// TODO Document.
class StopWatch {
    static {
        Notice.setObjectDiffuser(StopWatch.class, new StopWatchDiffuser());
    }

    // TODO Document.
    private int duration;
    
    // TODO Document.
    private long start = -1;
    
    // TODO Document.
    public void stop() {
        if (start > 0) {
            duration += System.currentTimeMillis() - start;
            start = -1;
        }
    }

    // TODO Document.
    public void start() {
        if (start < 0) {
            start = System.currentTimeMillis();
        }
    }
    
    // TODO Document.
    public int getDuration() {
        return duration;
    }
}
