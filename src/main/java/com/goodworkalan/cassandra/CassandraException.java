package com.goodworkalan.cassandra;

import java.io.PrintWriter;
import java.util.Date;

import com.goodworkalan.notice.Lister;
import com.goodworkalan.notice.ListerLister;
import com.goodworkalan.notice.Mapper;
import com.goodworkalan.notice.MapperMapper;
import com.goodworkalan.notice.Notice;
import com.goodworkalan.notice.Noticeable;
import com.goodworkalan.notice.Sink;

/**
 * Base class of a more verbose exception mechanism.
 * 
 * @author Alan Gutierrez
 */
public  class CassandraException extends RuntimeException implements Noticeable<CassandraException> {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;

    /** The error code. */
    private final int code;
    
    /** The structured error report. */
    private final Clue clue;

    // Register the CassandraException converter. 
    static {
        Notice.setConverter(CassandraException.class, CassandraExceptionConverter.INSTANCE);
    }
    
    /**
     * Create an exception with the given error code and the given initial
     * report structure.
     * 
     * @param code
     *            The error code.
     * @param clue
     *            The initial structured error report.
     */
    public CassandraException(int code, Clue clue) {
        this(code, clue, null);
    }

    /**
     * Create an exception with the given error code and the given initial
     * report structure that wraps the given cause exception.
     * 
     * @param code
     *            The error code.
     * @param clue
     *            The initial structured error report.
     * @param cause
     *            The cause.
     */
    public CassandraException(int code, Clue clue, Throwable cause) {
        super(null, cause);
        this.code = code;
        this.clue = new Clue(clue, getClass(), code);
    }

    public Date getDate() {
        return new Date((Long) get("date"));
    }

    /**
     * Get the error code.
     * 
     * @return The error code.
     */
    public int getCode() {
        return code;
    }
    
    public String getUuid() {
        return (String) get("uuid");
    }

    public long getThreadId() {
        return (Long) get("threadId");
    }
    
    public String getThreadName() {
        return (String) get("threadName");
    }

    /**
     * Put a named value into the exception report structure. The given name
     * must be valid Java identifier.
     * 
     * @param name
     *            The property name.
     * @param object
     *            The property value.
     * @return This exception to allow for chained invocation of report builder
     *         methods.
     */
    public CassandraException put(String name, Object object) {
        clue.put(name, object);
        return this;
    }
    
    public CassandraException put(String name, Object object, boolean recurse) {
        clue.put(name, object, recurse);
        return this;
    }

    public CassandraException put(String name, Object object, String... paths) {
        clue.put(name, object, paths);
        return this;
    }

    /**
     * Create a list builder for a named list in the report structure. The given
     * name must be valid Java identifier.
     * 
     * @param name
     *            The list name.
     * @return A list builder to specify the list contents.
     */
    public Lister<CassandraException> list(String name) {
        return new ListerLister<CassandraException>(this, clue.list(name));
    }

    /**
     * Create a map builder for a named map in the report structure. The given
     * name must be valid Java identifier.
     * 
     * @param name
     *            The map name.
     * @return A map builder to specify the map contents.
     */
    public Mapper<CassandraException> map(String name) {
        return new MapperMapper<CassandraException>(this, clue.map(name));
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
        return clue.get(path);
    }

    /**
     * Create an detail message from the error message format associated with
     * the error code.
     * 
     * @return The exception message.
     */
    @Override
    public String getMessage() {
        return clue.toString();
    }
    
    public void send(Sink sink) {
        printStackTrace(new PrintWriter(clue.getStackTrace()));
        clue.send(sink);
    }
}
