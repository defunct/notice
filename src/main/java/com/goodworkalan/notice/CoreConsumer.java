package com.goodworkalan.notice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.goodworkalan.madlib.VariableProperties;


/**
 * The default {@link Consumer} strategy. The alternate is {@link NullConsumer}
 * for when no {@link Recorder} instances are available.
 * 
 * @author Alan Gutierrez
 */
class CoreConsumer implements Consumer, Runnable {
    /** The list of recorder configurations. */
    private final List< Configuration> configurations;
    
    /** A blocking queue of messages to record. */
    private final BlockingQueue<Map<String, Object>> queue;

    /** The consumer thread. */
    private final Thread thread;

    /**
     * Create a consumer with the given list of recorders.
     * 
     * @param configurations
     *            The list of recorder configurations.
     */
    public CoreConsumer(List< Configuration> configurations) {
        this.configurations = configurations;
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
        List<Map<String, Object>> entries = new ArrayList<Map<String,Object>>();
        BlockingQueue<Map<String, Object>> queue = this.queue;
        try {
            entries.add(queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // We might terminate immediately if we've lost a data race to be
        // the core consumer with another thread. In this case, we don't
        // initialize our recorders.
        if (!entries.get(0).containsKey("terminate")) {
            Iterator<Configuration> eachConfiguration = configurations.iterator();
            while (eachConfiguration.hasNext()) {
                Configuration configuration = eachConfiguration.next();
                try {
                    configuration.getRecorder().initialize(configuration.getPrefix(), new VariableProperties(configuration.getProperties(), true));
                } catch (RuntimeException e) {
                    // FIXME What to do when the logger cannot log itself? SLF4J?
                    e.printStackTrace();
                    eachConfiguration.remove();
                }
            }
            Recorder[] recorders = new Recorder[configurations.size()];
            for (int i = 0, stop = configurations.size(); i < stop; i++) {
                recorders[i] = configurations.get(i).getRecorder();
            }
            int recorderCount = recorders.length;
            CONSUMER: for (;;) {
                try {
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
                    entries.add(queue.take());
                } catch (RuntimeException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            for (int i = 0; i < recorderCount; i++) {
                recorders[i].close();
            }
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
