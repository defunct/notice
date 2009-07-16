package com.goodworkalan.prattle;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class CoreConsumer implements Consumer, Runnable
{
    private final Recorder[] recorders;
    
    private final BlockingQueue<Message> queue;
    
    private final Thread thread;
    
    public CoreConsumer(List<Recorder> recorders)
    {
        this.recorders = recorders.toArray(new Recorder[recorders.size()]);
        this.queue = new LinkedBlockingQueue<Message>();
        this.thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }
    
    public void join()
    {
        queue.offer(new Terminate());
        try
        {
            thread.join();
        }
        catch (InterruptedException e)
        {
        }
    }

    public void consume(Message message)
    {
        queue.offer(message);
    }

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
}
