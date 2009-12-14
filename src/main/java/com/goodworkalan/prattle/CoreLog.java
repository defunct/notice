package com.goodworkalan.prattle;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.goodworkalan.cassandra.Message;

public class CoreLog extends Entry {
    /** Cache of resource bundles. */
    private final static ConcurrentMap<String, ResourceBundle> bundles = new ConcurrentHashMap<String, ResourceBundle>();
    
    /** The SLF4J logger. */
    private final org.slf4j.Logger logger;

    /** The level for this entry. */
    private final Level level;

    /** The log message. */
    private final String message;

    private Map<String, Object> objects;

    public CoreLog(org.slf4j.Logger logger, Level level, String message) {
        this.logger = logger;
        this.level = level;
        this.message = message;
    }

    private Map<String, Object> getObjects() {
        if (objects == null) {
            objects = new LinkedHashMap<String, Object>();
        }
        return objects;
    }

    public Entry object(String id, Object object) {
        getObjects().put(id, object);
        return this;
    }

    public Entry put(String name, Object object) {
        getObjects().put(name, flatten(object, SHALLOW));
        return this;
    }

    public Entry put(String name, Object object, String...paths) {
        getObjects().put(name, flatten(object, new HashSet<String>(Arrays.asList(paths))));
        return this;
    }

    @Override
    public Entry put(String name, Object object, boolean recurse) {
        getObjects().put(name, flatten(object, recurse ? DEEP : SHALLOW));
        return this;
    }

    public Entry serialize(String name, Serializable serializable) {
        try {
            getObjects().put(name, Serialization.getInstance(serializable));
        } catch (IOException e) {
            throw new PrattleException(0, e);
        }
        return this;
    }

    public Lister<Entry> list(String id) {
        List<Object> list = new ArrayList<Object>();
        getObjects().put(id, list);
        return new CoreLister<Entry>(this, list);
    }

    public Mapper<Entry> map(String id) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        getObjects().put(id, map);
        return new CoreMapper<Entry>(this, map);
    }

    public void send() {
        String name = logger.getName();
        int lastDot = name.lastIndexOf(".");
        String packageName = name.substring(0, lastDot);
        ResourceBundle bundle = bundles.get(packageName);
        if (bundle == null) {
            try {
                bundle = ResourceBundle.getBundle(packageName + ".prattle");
            } catch (MissingResourceException e) {
                bundle = ResourceBundle.getBundle("com.goodworkalan.prattle.missing");
            }
            bundles.put(packageName, bundle);
        }
        String className = name.substring(lastDot + 1);

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        
        Thread thread = Thread.currentThread();
        
        map.put("logger", logger);
        map.put("name", message);
        map.put("date", new Date());
        map.put("level", level.toString());
        map.put("threadId", thread.getId());
        map.put("threadName", thread.getPriority());

        if (objects != null) {
            map.put("vars", objects);
        }

        String msg = new Message(bundle, map, className + "/" + message, "name~%s").toString();
        map.put("message", msg);
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
        Sink.getInstance().send(map);
    }
}
