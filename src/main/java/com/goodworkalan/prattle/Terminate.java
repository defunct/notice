package com.goodworkalan.prattle;

public class Terminate extends Message
{
    public Terminate()
    {
        super(null, null, null);
    }
    
    @Override
    public boolean isTerminal()
    {
        return true;
    }
}
