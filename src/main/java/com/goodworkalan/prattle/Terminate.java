package com.goodworkalan.prattle;

import java.util.Map;

class Terminate implements Message
{
    public Terminate()
    {
    }
    
    public Map<String, Object> toMap()
    {
        return null;
    }
    
    public boolean isTerminal()
    {
        return true;
    }
}
