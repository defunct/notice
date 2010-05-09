package com.goodworkalan.notice;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.goodworkalan.verbiage.Message;

/**
 * A factory that builds notice messages whose levels are governed by an SLF4J
 * logger.
 * 
 * @author Alan Gutierrez
 */
public class NoticeFactory {
    /** The static cache of exception message resource bundles. */
    private final static ConcurrentMap<String, ResourceBundle> BUNDLES = new ConcurrentHashMap<String, ResourceBundle>();

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
    
    public Notice trace(String messageKey) {
        if (logger.isTraceEnabled()) {
            return createNotice("TRACE", messageKey, new Sender(logger) {
                @Override
                protected void send(String message) {
                    logger.trace(message);
                }
            });
        }
        return NullNotice.INSTANCE;
    }
    
    public Notice debug(String messageKey) {
        if (logger.isDebugEnabled()) {
            return createNotice("DEBUG", messageKey, new Sender(logger) {
                @Override
                protected void send(String message) {
                    logger.debug(message);
                }
            });
        }
        return NullNotice.INSTANCE;   
    }

    public Notice info(String messageKey) {
        if (logger.isInfoEnabled()) {
            return createNotice("INFO", messageKey, new Sender(logger) {
                @Override
                protected void send(String message) {
                    logger.info(message);
                }
            });
        }
        return NullNotice.INSTANCE;
    }    

    public Notice warn(String messageKey) {
        if (logger.isWarnEnabled()) {
            return createNotice("WARN", messageKey, new Sender(logger) {
                @Override
                protected void send(String message) {
                    logger.warn(message);
                }
            });
        }
        return NullNotice.INSTANCE;
    }
    
    public Notice error(String messageKey) {
        if (logger.isErrorEnabled()) {
            return createNotice("ERROR", messageKey, new Sender(logger) {
                @Override
                protected void send(String message) {
                    logger.error(message);
                }
            });
        }
        return NullNotice.INSTANCE;
    }

    private Notice createNotice(String level, String messageKey, Sender sender) {
        String qualifiedMessageKey = getMessageKey(logger.getName(), messageKey);
        Thread thread = Thread.currentThread();

        Map<String, Object> data = new HashMap<String, Object>();
        
        String className = logger.getName();

        data.put("$threadId", thread.getId());
        data.put("$threadName", thread.getName());
        data.put("$logger", className);
        data.put("$level", level);
        data.put("$messageKey", qualifiedMessageKey);
        data.put("$date", System.currentTimeMillis());
        
        Message message = new Message(BUNDLES, className, "notice", getMessageKey(className, messageKey), data);
        
        return new CoreNotice(message, data, sender);
    }
    
    static String getMessageKey(String className, String code) {
        int index = className.lastIndexOf('.');
        if (index > -1) {
            className = className.substring(index + 1);
        }
        return className + "/" + code;
    }
}
