package com.goodworkalan.prattle;


public class Logger
{
    private final org.slf4j.Logger logger;
    
    public Logger(org.slf4j.Logger logger)
    {
        this.logger = logger;
    }
    
    public void info(String pattern, Object...arguments)
    {
        if (logger.isInfoEnabled())
        {
            String message = String.format(pattern, arguments);
            logger.warn(message);
        }
    }
    
    public void debug(String pattern, Object...arguments)
    {
        if (logger.isDebugEnabled())
        {
            String message = String.format(pattern, arguments);
            logger.warn(message);
        }
    }
}
