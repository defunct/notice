package com.goodworkalan.prattle;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CoreLog implements Log {
    private final org.slf4j.Logger logger;

    private final Level level;

    private final StringBuilder message;

    private Map<String, Object> objects;

    public CoreLog(org.slf4j.Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
        this.message = new StringBuilder();
    }

    public Log message(String format, Object... args) {
        if (message.length() != 0) {
            message.append("\n");
        }
        message.append(String.format(format, args));
        return this;
    }

    private Map<String, Object> getObjects() {
        if (objects == null) {
            objects = new LinkedHashMap<String, Object>();
        }
        return objects;
    }

    public Log object(String id, Object object) {
        getObjects().put(id, object);
        return this;
    }

    public Log freeze(String name, Object object, Class<?>... freeze) {
        getObjects().put(name, Bean.freeze(object, freeze));
        return this;
    }

    public Log serialize(String name, Serializable serializable) {
        try {
            getObjects().put(name, Serialization.getInstance(serializable));
        } catch (IOException e) {
            throw new PrattleException(0, e);
        }
        return this;
    }

    public Log string(String id, Object object) {
        getObjects().put(id, object.toString());
        return this;
    }

    public Lister<Log> list(String id) {
        List<Object> list = new ArrayList<Object>();
        getObjects().put(id, list);
        return new CoreLister<Log>(this, list);
    }

    public Mapper<Log> map(String id) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        getObjects().put(id, map);
        return new CoreMapper<Log>(this, map);
    }

    public void send() {
        String msg = message.toString();
        switch (level) {
        case TRACE:
            logger.trace(msg);
            break;
        case DEBUG:
            logger.debug(msg);
            break;
        case INFO:
            logger.info(msg);
            break;
        case WARN:
            logger.warn(msg);
            break;
        default:
            logger.error(msg);
            break;
        }

        Sink.getInstance().send(
                new CoreMessage(logger.getName(), msg, level, objects));
    }
}
