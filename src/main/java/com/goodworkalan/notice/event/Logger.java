package com.goodworkalan.notice.event;

public class Logger {
    private final org.slf4j.Logger logger;

    public Logger(org.slf4j.Logger logger) {
        this.logger = logger;
    }
    
    public Entry trace(String message) {
        return logger.isTraceEnabled() ? (Entry) new CoreEntry(logger, Level.TRACE, message) : NullLog.INSTANCE;
    }
    
    public Entry debug(String message) {
        return logger.isDebugEnabled() ? (Entry) new CoreEntry(logger, Level.DEBUG, message) : NullLog.INSTANCE;
    }

    public Entry info(String message) {
        return logger.isInfoEnabled() ? (Entry) new CoreEntry(logger, Level.INFO, message) : NullLog.INSTANCE;   
    }
}
