package com.goodworkalan.prattle;


public class Logger
{
    private final org.slf4j.Logger logger;
    
    public Logger(org.slf4j.Logger logger)
    {
        this.logger = logger;
    }
    
    public Entry trace(String message)
    {
        return logger.isTraceEnabled() ? new CoreLog(logger, Level.TRACE, message) : NullLog.INSTANCE;
    }
    
    public Entry debug(String message)
    {
        return logger.isDebugEnabled() ? new CoreLog(logger, Level.DEBUG, message) : NullLog.INSTANCE;
    }

    public Entry info(String message)
    {
        return logger.isInfoEnabled() ? new CoreLog(logger, Level.INFO, message) : NullLog.INSTANCE;   
    }
}
