package com.goodworkalan.prattle;


public class Logger
{
    private final org.slf4j.Logger logger;
    
    public Logger(org.slf4j.Logger logger)
    {
        this.logger = logger;
    }
    
    public void trace(String format, Object...args)
    {
        trace().message(format, args).send();
    }
    
    public Log trace()
    {
        return logger.isTraceEnabled() ? new CoreLog(logger, Level.TRACE) : NullLog.INSTANCE;
    }
    
    public void debug(String format, Object...args)
    {
        debug().message(format, args).send();
    }

    public Log debug()
    {
        return logger.isDebugEnabled() ? new CoreLog(logger, Level.DEBUG) : NullLog.INSTANCE;
    }

    public void info(String format, Object...args)
    {
        info().message(format, args).send();
    }
    
    public Log info()
    {
        return logger.isInfoEnabled() ? new CoreLog(logger, Level.INFO) : NullLog.INSTANCE;   
    }
}
