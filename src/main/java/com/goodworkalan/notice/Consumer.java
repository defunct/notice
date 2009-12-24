package com.goodworkalan.notice;

import java.util.Map;

/**
 * A consumer strategy for Prattle messages, either the core consumer or a null
 * consumer if no recorders are available.
 * 
 * @author Alan Gutierrez
 */
interface Consumer {
    /**
     * Consume a message.
     * 
     * @param entry
     *            A log entry converted to hash.
     */
    public void consume(Map<String, Object> entry);

    /**
     * Shutdown the consumer thread, waiting for it to finish.
     */
    public void shutdown();
}
