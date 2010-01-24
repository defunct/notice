package com.goodworkalan.notice.event;

public class LoggerFactory {
    public static Logger getLogger(Class<?> loggedClass) {
        return new Logger(org.slf4j.LoggerFactory.getLogger(loggedClass));
    }
}
