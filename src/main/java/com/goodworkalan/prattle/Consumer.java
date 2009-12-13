package com.goodworkalan.prattle;

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
     * @param message
     *            A message.
     */
    public void consume(Message message);

    /**
     * Shutdown the consumer thread, waiting for it to finish.
     */
    public void shutdown();
}
