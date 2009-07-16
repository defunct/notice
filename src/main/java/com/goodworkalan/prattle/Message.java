package com.goodworkalan.prattle;

import java.util.Map;

interface Message
{
    public Map<String, Object> toMap();
    
    public boolean isTerminal();
}
