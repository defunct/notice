package com.goodworkalan.notice.exception;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.goodworkalan.notice.Notice;

/**
 * A reporting structure that is used to gather information about the state of
 * the program in case an exception is thrown.
 * 
 * @author Alan Gutierrez
 */
public class Clue extends Notice<Clue> {
    /** A place to dump the stack trace. */
    private final StringWriter stackTrace;
    
    /** A list of properties added for each marker. */
    private final Map<Object, List<String>> markers = new HashMap<Object, List<String>>();

    /**
     * Creates a clue to use to gather diagnostic information just in case an
     * exception is thrown.
     */
    public Clue() {
        this(new StringWriter());
    }

    /**
     * This constructor is a trick to get the stack trace
     * <code>StringWriter</code> into a local variable so it can be both passed
     * to the super constructor and assigned to a member variable.
     * 
     * @param stackTrace
     *            The string writer where the stack trace will be written.
     */
    private Clue(StringWriter stackTrace) {
        super(Clue.class.getCanonicalName(), "exceptions", "missing", now("level", "EXCEPTION"), later("stackTrace", stackTrace));
        this.stackTrace = stackTrace;
    }

    /**
     * Copy construct this clue form the given clue, setting the context and
     * error code, and generating a UUID. This is used when a @Clue@ is built
     * outside of a @CassandraException@ and is provided in the exception
     * constructor.
     * 
     * @param clue
     *            The clue to copy.
     * @param context
     *            The logging context.
     * @param code
     *            The error code.
     */
    Clue(Clue clue, Class<?> context, int code) {
        super(clue, context.getCanonicalName() == null ? context.getName() : context.getCanonicalName(), Integer.toString(code), now("code", code), now("uuid", UUID.randomUUID().toString()));
        stackTrace = clue.stackTrace;
    }

    /**
     * Construct a clue that is a wrapper around the given
     * <code>Throwable</code> instance.
     * 
     * @param clue
     *            The clue to copy.
     * @param throwable
     *            The thorwable.
     */
    Clue(Clue clue, Throwable throwable) {
        super(clue, Exceptions.class.getCanonicalName(), "101", now("code", 101), now("e", throwable), now("uuid", UUID.randomUUID().toString()));
        stackTrace = clue.stackTrace;
    }

    /**
     * Return this object cast to <code>Clue</code> to return from chained
     * variable map population methods. Descendant classes implement this method
     * and simply return the <code>this</code> object.
     * 
     * @return This object.
     */
    @Override
    public Clue getSelf() {
        return this;
    }

    /**
     * Record a name put into the report in the marker set.
     * 
     * @param name
     *            The name put into the report.
     */
    protected void added(String name) {
        int size = markers.size();
        if (size != 0) {
            for (List<String> names : markers.values()) {
                names.add(name);
            }
        }
    }

    /**
     * Create a marker that can be used to clear out all subsequently assigned
     * properties.
     * 
     * @return This report to allow for chained invocation of report builder
     *         methods.
     */
    public Object mark() {
        Object key = new Object();
        markers.put(key, new ArrayList<String>());
        return key;
    }

    /**
     * Remove all of the properties assigned since the last call to
     * {@link #mark()}.
     */
    public void clear(Object object) {
        if (markers.isEmpty()) {
            throw new IllegalStateException();
        }
        for (String marker : markers.get(object)) {
            remove(marker);
        }
        markers.remove(object);
    }
    
    StringWriter getStackTrace() {
        return stackTrace;
    }
}
