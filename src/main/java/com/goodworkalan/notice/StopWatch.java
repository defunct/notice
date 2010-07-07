package com.goodworkalan.notice;

/**
 * A stop watch to time operations and record them in a Notice entry.
 * 
 * @author Alan Gutierrez
 */
class StopWatch {
    static {
        Notice.setObjectDiffuser(StopWatch.class, new StopWatchDiffuser());
    }

    /** The total duration of all starts and stops in milliseconds. */
    private int duration;

    /**
     * The time in milliseconds since the epoch when the stop watch started
     * running.
     */
    private long start = -1;

    /** Stop the stop watch if it is running. */
    public void stop() {
        if (start > 0) {
            duration += System.currentTimeMillis() - start;
            start = -1;
        }
    }

    /** Start the stop watch if it is not already running. */
    public void start() {
        if (start < 0) {
            start = System.currentTimeMillis();
        }
    }

    /**
     * Get the total duration of all starts and stops in milliseconds.
     * 
     * @return The duration.
     */
    public int getDuration() {
        return duration;
    }
}
