package com.goodworkalan.notice;

import java.util.Map;

/**
 * A no operation consumer used for sinks that have no recorders configured.
 * 
 * @author Alan Gutierrez
 */
class NullConsumer implements Consumer {
    /**
     * Does nothing.
     * 
     * @param message
     *            The message to consume.
     */
    public void consume(Map<String, Object> message) {
    }

    /**
     * Does nothing.
     */
    public void shutdown() {
    }
}
