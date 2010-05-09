package com.goodworkalan.notice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goodworkalan.madlib.VariableProperties;
import com.goodworkalan.retry.Retry;

/**
 * A consumer that initializes multiple recorders and writes entries to multiple
 * recorders.
 * 
 * @author Alan Gutierrez
 */
class CoreConsumer implements Consumer, Runnable {
    /** The logger used to record errors. */
    private static final Logger logger = LoggerFactory.getLogger(Sink.class);

    /**
     * The object assigned to the "$termiante" key in an entry map to terminate
     * the the message consumer thread.
     */
    private static final Object TERMINATE = new Object();
    
    /** The list of recorder configurations. */
    private final List< Configuration> configurations;
    
    /** A blocking queue of messages to record. */
    private final BlockingQueue<Map<String, Object>> queue;

    /** The consumer thread. */
    private final Thread thread;

    /**
     * Create a consumer with the given list of recorder configurations.
     * 
     * @param configurations
     *            The list of recorder configurations.
     */
    public CoreConsumer(List<Configuration> configurations) {
        this.configurations = configurations;
        this.queue = new LinkedBlockingQueue<Map<String, Object>>();
        this.thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Attempts to take an entry from the blocking entry queue, retrying
     * if the attempt is interrupted.
     * 
     * @author Alan Gutierrez
     */
    private final class GetFirst implements Retry.Function<Map<String, Object>> {
        /**
         * Attempt to take an entry from the blocking entry queue, retrying if
         * the attempt to take is interrupted.
         * 
         * @return An entry from the entry queue.
         */
        public Map<String, Object> retry() throws InterruptedException {
            return queue.take();
        }
    }

    /**
     * Consumes messages from the blocking queue and feeds them to each of the
     * recorders.
     */
    public void run() {
        List<Map<String, Object>> entries = new ArrayList<Map<String,Object>>();
        final BlockingQueue<Map<String, Object>> queue = this.queue;
        GetFirst getFirst = new GetFirst();
        entries.add(Retry.retry(getFirst));
        Object terminate = TERMINATE;
        // We might terminate immediately if we've lost a data race to be
        // the core consumer with another thread. In this case, we don't
        // initialize our recorders.
        if (!(entries.get(0).get("$terminate") == terminate)) {
            Iterator<Configuration> eachConfiguration = configurations.iterator();
            while (eachConfiguration.hasNext()) {
                Configuration configuration = eachConfiguration.next();
                try {
                    configuration.recorder.initialize(configuration.prefix, new VariableProperties(configuration.properties, true));
                } catch (Throwable e) {
                    // The stack trace will show the culpret.
                    logger.error("Unable to configure recorder.", e); 
                    eachConfiguration.remove();
                }
            }
            // Create an array instead of building an iterator each time.
            Recorder[] recorders = new Recorder[configurations.size()];
            for (int i = 0, stop = configurations.size(); i < stop; i++) {
                recorders[i] = configurations.get(i).recorder;
            }
            int recorderCount = recorders.length;
            CONSUMER: for (;;) {
                try {
                    queue.drainTo(entries);
                    for (int i = 0, stop = entries.size(); i < stop; i++) {
                        Map<String, Object> entry = entries.get(i);
                        if (entry.get("$terminate") == terminate) {
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
                    entries.add(Retry.retry(getFirst));
                } catch (RuntimeException e) {
                    // The stack trace will show the culpret.
                    logger.error("Unable to execute recorder.", e); 
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
        queue.offer(Collections.<String, Object>singletonMap("$terminate", TERMINATE));
        Retry.retry(new Retry.Procedure() {
            public void retry() throws InterruptedException {
                thread.join();
            }
        });
    }
}
