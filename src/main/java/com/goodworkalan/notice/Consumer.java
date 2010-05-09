package com.goodworkalan.notice;

import java.util.Map;

/**
 * A consumer of notice messages that in turn calls configured recorders. There
 * are only two implementations of this interface, the core consumer if there
 * are one or more recorders configured or a null consumer if no recorders are
 * available.
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
