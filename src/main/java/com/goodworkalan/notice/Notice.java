package com.goodworkalan.notice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.goodworkalan.diffuse.Diffuse;
import com.goodworkalan.diffuse.MapConverter;
import com.goodworkalan.verbiage.Message;

/**
 * Abstract implementation of a structured logging or error message.
 * 
 * @author Alan Gutierrez
 * 
 * @param <Self>
 *            The type of the descendant class, used to return the descendant
 *            type from the chained methods that build the structure.
 */
public abstract class Notice<Self> implements Noticeable<Self> {
    /** Cache of resource bundles. */
    private final static ConcurrentMap<String, ResourceBundle> bundles = new ConcurrentHashMap<String, ResourceBundle>();

    /**
     * An set to indicate deep copy. An empty set because no includes means
     * include all.
     */
    final static Set<String> DEEP = Collections.emptySet();
    
    final static Set<String> IGNORE = new HashSet<String>(Arrays.asList(new String[]{
            "context", "threadId", "threadName", "vars", "date", "message"
    }));

    /**
     * A set to indicate a shallow copy. The set contains a single include,
     * which means check the set for container paths, but the null string will
     * match no path.
     */
    final static Set<String> SHALLOW = Collections.singleton("\0");

    /**
     * The top level map that contains properties set by the notice
     * implementation. Properties added by the <code>put</code>,
     * <code>map</code> and <code>list</code> methods are assigned to the
     * <code>vars</code> map.
     */
    private final Map<String, Object> line;
    
    /** The notice variables. */
    private final Map<String, Object> vars;
    
    private final List<Property> deferred;
    
    private final Message message;



    protected static Property now(String name, Object value) {
        return new Property(name, value, SHALLOW, false);
    }
    
    protected static Property later(String name, Object value) {
        return new Property(name, value, SHALLOW, true);
    }
    
    /**
     * Construct a copy of the given notice that will use the given context to
     * find the message bundle and add the given extras to the top level map.
     * 
     * @param notice
     *            The notice to copy.
     * @param context
     *            The context.
     * @param extras
     *            An array of extra properties to add to the top level map.
     */
    protected Notice(Notice<?> notice, String context, String messageKey, Property...properties) {
        this.vars = MapConverter.INSTANCE.modifiable(notice.vars, new StringBuilder(), DEEP);
        this.line = notice.line;
        this.message = new Message(bundles, context, notice.message.getBundleName(), messageKey, notice.message.getVariables());
        this.deferred = notice.deferred;
        this.initialize(properties);
        this.line.put("context", context);
        this.line.put("threadId", notice.get("threadId"));
        this.line.put("threadName", notice.get("threadName"));
        this.line.put("date", notice.get("date"));
    }
    
    protected Notice(String context, String bundleName, String messageKey, Property...properties) {
        Thread thread = Thread.currentThread();
        
        this.vars = new LinkedHashMap<String, Object>();
        this.line = new LinkedHashMap<String, Object>();
        this.message = new Message(bundles, context, bundleName, messageKey, this.line);
        this.deferred = new ArrayList<Property>();
        this.initialize(properties);
        this.line.put("context", context);
        this.line.put("threadId", thread.getId());
        this.line.put("threadName", thread.getName());
        this.line.put("date", new Date().getTime());
    }
    
    private void initialize(Property...properties) {
        for (Property property : properties) {
            if (property.isDeferred()) {
                deferred.add(property);
            } else {
                property.put(line);
            }
        }
        line.put("vars", Collections.unmodifiableMap(vars));
    }
    
    /**
     * Return this object, but cast to the type needed for the chained variable
     * map population method. Descendant classes implement this method and
     * simply return the <code>this</code> object.
     * 
     * @return This object.
     */
    public abstract Self getSelf();

    /**
     * Called when an object is added to the notice. Descendant classes can take
     * actions based on the name. This used by <code>Clue</code> in Cassandra to
     * record the name as part of the mark and clear facility.
     * 
     * @param name
     *            The name.
     */
    protected void added(String name) {
    }

    /**
     * Remove the object with the given name from the variable map.Descendant
     * classes can take can remove objects from the variable map. This used by
     * <code>Clue</code> in Cassandra to record the name as part of the mark and
     * clear facility.
     * 
     * 
     * @param name
     *            The object name.
     */
    protected void remove(String name) {
        vars.remove(name);
    }

    public Map<String, Object> getVars() {
        return vars;
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#put(java.lang.String, java.lang.Object)
     */
    public Self put(String name, Object object) {
        added(name);
        vars.put(name, Diffuse.flatten(object, SHALLOW));
        return getSelf();
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#put(java.lang.String, java.lang.Object, java.lang.String)
     */
    public Self put(String name, Object object, String...paths) {
        added(name);
        vars.put(name, Diffuse.flatten(object, new HashSet<String>(Arrays.asList(paths))));
        return getSelf();
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#put(java.lang.String, java.lang.Object, boolean)
     */
    public Self put(String name, Object object, boolean recurse) {
        added(name);
        vars.put(name, Diffuse.flatten(object, recurse ? DEEP : SHALLOW));
        return getSelf();
    }

    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#map(java.lang.String)
     */
    public Mapper<Self> map(String name) {
        added(name);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        vars.put(name, Collections.unmodifiableMap(map));
        return new CoreMapper<Self>(getSelf(), map);
    }
    
    /* (non-Javadoc)
     * @see com.goodworkalan.prattle.entry.MetaEntry#list(java.lang.String)
     */
    public Lister<Self> list(String name) {
        added(name);
        List<Object> list = new ArrayList<Object>();
        vars.put(name, Collections.unmodifiableList(list));
        return new CoreLister<Self>(getSelf(), list);
    }

    /**
     * Get the value in the report structure at the given path.
     * 
     * @param path
     *            The path.
     * @return The value found by navigating the path or null if the path does
     *         not exist.
     * @exception IllegalArgumentException
     *                If any part of the given path is not a valid Java
     *                identifier or list index.
     */
    public Object get(String path) {
        return message.get(path);
    }
    
    public String getContext() {
        return message.getContext();
    }
    
    protected void sending() {
    }
    
    protected void sent() {
    }
    
    public void send(Sink sink) {
        if (!deferred.isEmpty()) {
            for (Property property : deferred) {
                property.put(line);
            }
        }
        sending();
        line.put("message", toString());
        sink.send(line);
        sent();
    }
    
    public String toString() {
        return message.toString();
    }
    
    protected final static class Property {
        private final String name;
        
        private final Object object;
        
        private final Set<String> includes;
        
        private final boolean deferred;

        public Property(String name, Object object, Set<String> includes, boolean deferred) {
            this.name = name;
            this.object = object;
            this.includes = includes;
            this.deferred = deferred;
        }
        
        protected void put(Map<String, Object> map) {
            if (!IGNORE.contains(name)) {
                map.put(name, Diffuse.flatten(object, includes));
            }
        }
        
        public boolean isDeferred() {
            return deferred;
        }
    }
}
