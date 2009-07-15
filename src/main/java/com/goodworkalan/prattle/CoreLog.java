package com.goodworkalan.prattle;

import java.util.ArrayList;
import java.util.List;


public class CoreLog implements Log
{
    private final org.slf4j.Logger logger;
    
    private final Level level;
    
    private final StringBuilder message;
    
    private final List<CoreDump> dumps;
    
    public CoreLog(org.slf4j.Logger logger, Level level)
    {
        this.logger = logger;
        this.level = level;
        this.message = new StringBuilder();
        this.dumps = new ArrayList<CoreDump>();
    }
    
    public Log message(String format, Object...args)
    {
        if (message.length() != 0)
        {
            message.append("\n");
        }
        message.append(String.format(format, args));
        return this;
    }
    
    public Dump dump(Object object)
    {
        CoreDump dump = new CoreDump(this, object);
        dumps.add(dump);
        return dump;
    }
    
    public void send()
    {
        switch (level)
        {
        case TRACE:
            logger.trace(message.toString());
            break;
        case DEBUG:
            logger.debug(message.toString());
            break;
        case INFO:
            logger.info(message.toString());
            break;
        case WARN:
            logger.warn(message.toString());
            break;
        default:
            logger.error(message.toString());
            break;
        }
    }
}
