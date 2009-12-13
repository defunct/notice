package com.goodworkalan.prattle;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link Consumer} implementation.
 *
 * @author Alan Gutierrez
 */
class CoreConsumer implements Consumer, Runnable
{
    /** An array of recorders. */
    private final Recorder[] recorders;
    
    /** A blocking queue of messages to record. */
    private final BlockingQueue<Message> queue;
    
    /** The consumer thread. */
    private final Thread thread;

    /**
     * Create a consumer with the given list of recorders.
     * 
     * @param recorders
     *            A list of recorders.
     */
    public CoreConsumer(List<Recorder> recorders)
    {
        this.recorders = recorders.toArray(new Recorder[recorders.size()]);
        this.queue = new LinkedBlockingQueue<Message>();
        this.thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Consumes messages from the blocking queue and feeds them to each of the
     * recorders.
     */
    public void run()
    {
        Recorder[] recorders = this.recorders;
        int recorderCount = recorders.length;
        BlockingQueue<Message> queue = this.queue;
        
        for (;;)
        {
            try
            {
                Message message =  queue.take();
                if (message.isTerminal())
                {
                    break;
                }
                Map<String, Object> map = message.toMap();
                for (int i = 0; i < recorderCount; i++)
                {
                    recorders[i].record(map);
                }
            }
            catch (Throwable e)
            {
            }
        }
        
        for (int i = 0; i < recorderCount; i++)
        {
            recorders[i].close();
        }
    }

    /**
     * Consume a message.
     * 
     * @param message
     *            A message.
     */
    public void consume(Message message)
    {
        queue.offer(message);
    }

    /**
     * Shutdown the consumer thread, blocking until it finishes.
     */
    public void shutdown() {
        queue.offer(new Terminate());
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
    }
}
