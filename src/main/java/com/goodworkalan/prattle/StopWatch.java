package com.goodworkalan.prattle;

public class StopWatch {
    private int duration;
    
    private long start = -1;
    
    public void stop() {
        if (start > 0) {
            duration += System.currentTimeMillis() - start;
            start = -1;
        }
    }

    public void start() {
        if (start < 0) {
            start = System.currentTimeMillis();
        }
    }
    
    public int getDuration() {
        return duration;
    }
}
