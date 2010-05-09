package com.goodworkalan.notice;

import java.util.Map;

import org.slf4j.Logger;

/**
 * Sends the output message to an SLF4J logger before sending it 
 *
 * @author Alan Gutierrez
 */
abstract class Sender {
    /** The SLF4J logger. */
    protected final Logger logger;

    /**
     * Create a sender that will send to the given logger at the given level.
     * 
     * @param logger
     *            The SLF4J logger.
     */
    public Sender(Logger logger) {
        this.logger = logger;
    }
    
    protected abstract void send(String message);
    /**
     * Send the given map to the given sink after writing the formatted message
     * to this logger at this level.
     * 
     * @param data
     *            The data to send.
     * @param sink
     *            The destination for the data.
     */
    public final void send(Map<String, Object> data, Sink sink) {
        send((String) data.get("$message"));
        sink.send(data);
    }
}
