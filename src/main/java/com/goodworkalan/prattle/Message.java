package com.goodworkalan.prattle;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Message
{
    private final String text;
    
    private final Level level;

    private final Map<String, Object> objects;
    
    private final long threadId;

    public Message(String text, Level level, Map<String, Object> objects)
    {
        this.threadId = Thread.currentThread().getId();
        this.text = text;
        this.level = level;
        this.objects = objects;
    }
    
    public boolean isTerminal()
    {
        return false;
    }
    
    public Map<String, Object> toMap()
    {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("date", new Date());
        map.put("level", level.toString());
        map.put("thread", threadId);
        map.put("message", text);
        if (objects != null)
        {
            map.put("objects", objects);
        }
        return map;
    }
}
