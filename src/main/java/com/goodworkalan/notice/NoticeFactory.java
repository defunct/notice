package com.goodworkalan.notice;

import java.util.HashMap;
import java.util.Map;

import com.goodworkalan.verbiage.Message;

/**
 * A factory that builds notice messages whose levels are governed by an SLF4J
 * logger.
 * 
 * @author Alan Gutierrez
 */
public class NoticeFactory {
    /** The SLF4J logger. */
    private final org.slf4j.Logger logger;

    /**
     * Create a notice factory that will write message to the given SLF4j logger
     * and use the SLF4J logger to determine what logging levels are enabled.
     * 
     * @param logger
     *            The SLF4J logger.
     */
    public NoticeFactory(org.slf4j.Logger logger) {
        this.logger = logger;
    }
    
    // TODO Document.
    public Notice trace(String messageKey) {
        if (logger.isTraceEnabled()) {
            return createNotice("TRACE", messageKey, 1);
        }
        return NullNotice.INSTANCE;
    }
    
    // TODO Document.
    public Notice debug(String messageKey) {
        if (logger.isDebugEnabled()) {
            return createNotice("DEBUG", messageKey, 2);
        }
        return NullNotice.INSTANCE;   
    }

    // TODO Document.
    public Notice info(String messageKey) {
        if (logger.isInfoEnabled()) {
            return createNotice("INFO", messageKey, 3);
        }
        return NullNotice.INSTANCE;
    }    

    // TODO Document.
    public Notice warn(String messageKey) {
        if (logger.isWarnEnabled()) {
            return createNotice("WARN", messageKey, 4);
        }
        return NullNotice.INSTANCE;
    }
    
    public Notice error(String messageKey) {
        if (logger.isErrorEnabled()) {
            return createNotice("ERROR", messageKey, 5);
        }
        return NullNotice.INSTANCE;
    }

    /**
     * Create a notice populating the common notice fields.
     * 
     * @param levelName
     *            The notice level name.
     * @param messageKey
     *            The message key.
     * @param level
     *            The magic number indicating the level.
     * @return A newly created notice.
     */
    private Notice createNotice(String levelName, String messageKey, int level) {
        String qualifiedMessageKey = getMessageKey(logger.getName(), messageKey);
        Thread thread = Thread.currentThread();

        Map<String, Object> data = new HashMap<String, Object>();
        
        String className = logger.getName();

        data.put("$threadId", thread.getId());
        data.put("$threadName", thread.getName());
        data.put("$logger", className);
        data.put("$level", levelName);
        data.put("$messageKey", qualifiedMessageKey);
        data.put("$date", System.currentTimeMillis());
        
        Message message = new Message(className, "notice", getMessageKey(className, messageKey), data);
        
        return new CoreNotice(message, data, logger, level);
    }
    
    // TODO Document.
    static String getMessageKey(String className, String code) {
        int index = className.lastIndexOf('.');
        if (index > -1) {
            className = className.substring(index + 1);
        }
        return className + "/" + code;
    }
}
