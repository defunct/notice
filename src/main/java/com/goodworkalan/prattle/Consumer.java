package com.goodworkalan.prattle;

/**
 * A consumer of prattle message events.
 * 
 * @author Alan Gutierrez
 * 
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
     * Join the consumer thread, waiting for it to finish.
     */
    public void join();
}
