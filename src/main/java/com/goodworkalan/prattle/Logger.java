package com.goodworkalan.prattle;


public class Logger
{
    private final org.slf4j.Logger logger;
    
    public Logger(org.slf4j.Logger logger)
    {
        this.logger = logger;
    }
    
    public Log info(String format, Object...args)
    {
        return logger.isInfoEnabled() ? new CoreLog(logger, Level.INFO).message(format, args) : NullLog.INSTANCE;
    }
    
    public Log debug(String format, Object...args)
    {
        return logger.isDebugEnabled() ? new CoreLog(logger, Level.DEBUG).message(format, args) : NullLog.INSTANCE;
    }
}
