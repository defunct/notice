package com.goodworkalan.notice.event;

import com.goodworkalan.diffuse.ClassAsssociation;
import com.goodworkalan.diffuse.ConcurrentClassAssociation;
import com.goodworkalan.diffuse.ObjectDiffuser;

public class Logger {
    private final ClassAsssociation<ObjectDiffuser> cache;
    
    private final org.slf4j.Logger logger;
    
    // FIXME Does Diffuse go here?

    public Logger(org.slf4j.Logger logger) {
        this.cache = new ConcurrentClassAssociation<ObjectDiffuser>();
        this.logger = logger;
    }
    
    public Entry trace(String message) {
        return logger.isTraceEnabled() ? (Entry) new CoreEntry(logger, cache, Level.TRACE, message) : NullLog.INSTANCE;
    }
    
    public Entry debug(String message) {
        return logger.isDebugEnabled() ? (Entry) new CoreEntry(logger, cache, Level.DEBUG, message) : NullLog.INSTANCE;
    }

    public Entry info(String message) {
        return logger.isInfoEnabled() ? (Entry) new CoreEntry(logger, cache, Level.INFO, message) : NullLog.INSTANCE;   
    }
}
