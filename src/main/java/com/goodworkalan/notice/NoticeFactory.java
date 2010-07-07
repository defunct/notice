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
    

    /**
     * Create a trace message formatted using the message mapped to the given
     * message key. The message will be emitted if the TRACE level is enabled
     * for the context associated with this factory.
     * 
     * @param messageKey
     *            The message key.
     * @return A notice to build a structured logging message.
     */
    public Notice trace(String messageKey) {
        if (logger.isTraceEnabled()) {
            return createNotice("TRACE", messageKey, 1);
        }
        return NullNotice.INSTANCE;
    }

    /**
     * Create a debug message formatted using the message mapped to the given
     * message key. The message will be emitted if the DEBUG level is enabled
     * for the context associated with this factory.
     * 
     * @param messageKey
     *            The message key.
     * @return A notice to build a structured logging message.
     */
    public Notice debug(String messageKey) {
        if (logger.isDebugEnabled()) {
            return createNotice("DEBUG", messageKey, 2);
        }
        return NullNotice.INSTANCE;   
    }

    /**
     * Create an info message formatted using the message mapped to the given
     * message key. The message will be emitted if the INFO level is enabled for
     * the context associated with this factory.
     * 
     * @param messageKey
     *            The message key.
     * @return A notice to build a structured logging message.
     */
   public Notice info(String messageKey) {
        if (logger.isInfoEnabled()) {
            return createNotice("INFO", messageKey, 3);
        }
        return NullNotice.INSTANCE;
    }

    /**
     * Create a warn message formatted using the message mapped to the given
     * message key. The message will be emitted if the WARN level is enabled for
     * the context associated with this factory.
     * 
     * @param messageKey
     *            The message key.
     * @return A notice to build a structured logging message.
     */
    public Notice warn(String messageKey) {
        if (logger.isWarnEnabled()) {
            return createNotice("WARN", messageKey, 4);
        }
        return NullNotice.INSTANCE;
    }

    /**
     * Create an error message formatted using the message mapped to the given
     * message key. The message will be emitted if the ERROR level is enabled
     * for the context associated with this factory.
     * 
     * @param messageKey
     *            The message key.
     * @return A notice to build a structured logging message.
     */
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

    /**
     * Get the message key for the given class name and message code. A message
     * key is created by joining the simple class name with a slash and the
     * message code.
     * 
     * @param className
     *            The fully qualified class name.
     * @param code
     *            The message code.
     * @return The message key created by joining the simple class with the
     *         code.
     */
    static String getMessageKey(String className, String code) {
        int index = className.lastIndexOf('.');
        if (index > -1) {
            className = className.substring(index + 1);
        }
        return className + "/" + code;
    }
}
