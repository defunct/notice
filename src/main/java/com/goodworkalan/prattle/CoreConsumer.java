package com.goodworkalan.prattle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The default {@link Consumer} strategy. The alternate is {@link NullConsumer}
 * for when no {@link Recorder} instances are available.
 * 
 * @author Alan Gutierrez
 */
class CoreConsumer implements Consumer, Runnable {
    /** An array of recorders. */
    private final Recorder[] recorders;

    /** A blocking queue of messages to record. */
    private final BlockingQueue<Map<String, Object>> queue;

    /** The consumer thread. */
    private final Thread thread;

    /**
     * Create a consumer with the given list of recorders.
     * 
     * @param recorders
     *            A list of recorders.
     */
    public CoreConsumer(List<Recorder> recorders) {
        this.recorders = recorders.toArray(new Recorder[recorders.size()]);
        this.queue = new LinkedBlockingQueue<Map<String, Object>>();
        this.thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Consumes messages from the blocking queue and feeds them to each of the
     * recorders.
     */
    public void run() {
        Recorder[] recorders = this.recorders;
        int recorderCount = recorders.length;
        BlockingQueue<Map<String, Object>> queue = this.queue;
        List<Map<String, Object>> entries = new ArrayList<Map<String,Object>>();
        CONSUMER: for (;;) {
            try {
                entries.add(queue.take());
                queue.drainTo(entries);
                for (int i = 0, stop = entries.size(); i < stop; i++) {
                    Map<String, Object> entry = entries.get(i);
                    if (entry.containsKey("terminate")) {
                        break CONSUMER;
                    }
                    for (int j = 0; j < recorderCount; j++) {
                        recorders[j].record(entry);
                    }
                }
                for (int i = 0; i < recorderCount; i++) {
                    recorders[i].flush();
                }
                entries.clear();
            } catch (Throwable e) {
            }
        }

        for (int i = 0; i < recorderCount; i++) {
            recorders[i].close();
        }
    }

    /**
     * Consume a message.
     * 
     * @param message
     *            A message.
     */
    public void consume(Map<String, Object> message) {
        queue.offer(message);
    }

    /**
     * Shutdown the consumer thread, blocking until it finishes.
     */
    public void shutdown() {
        queue.offer(Collections.<String, Object>singletonMap("terminate", true));
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
    }
}
