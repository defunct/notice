package com.goodworkalan.prattle;

interface Consumer
{
    public void consume(Message message);
    
    public void join();
}
