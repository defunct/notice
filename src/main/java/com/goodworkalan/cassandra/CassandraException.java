package com.goodworkalan.cassandra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Base class of a more verbose exception mechanism.
 * 
 * @author Alan Gutierrez
 */
public abstract class CassandraException extends RuntimeException {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;

    /** The map of properties. */
    private final Map<String, Object> map = new LinkedHashMap<String, Object>();

    /** The error code. */
    private final int code;
    
    /** The message structure. */
    private final Message message;

    /**
     * Create an exception with the given error code and the given initial
     * report structure.
     * 
     * @param code
     *            The error code.
     * @param report
     *            An initial report structure.
     */
    public CassandraException(int code, Report report) {
        this(code, report, null);
    }

    /**
     * Create an exception with the given error code and the given initial
     * report structure that wraps the given cause exception.
     * 
     * @param code
     *            The error code.
     * @param report
     *            An initial report structure.
     * @param cause
     *            The cause.
     */
    public CassandraException(int code, Report report, Throwable cause) {
        super(null, cause);
        String key = Integer.toString(code);

        ResourceBundle exceptions = null;
        
        String name = getClass().getCanonicalName();
        if (name != null) {
            try {
                exceptions = ResourceBundle.getBundle(getClass().getCanonicalName());
            } catch (MissingResourceException e) {
            }
        }
        
        if (exceptions == null) {
            exceptions = ResourceBundle.getBundle(CassandraException.class.getPackage().getName() + ".empty");
        }
        
        this.code = code;
        this.map.putAll(report.getReportMap());
        this.message = new Message(exceptions, map, key, "%s");
    }

    /**
     * Get the error code.
     * 
     * @return The error code.
     */
    public int getCode() {
        return code;
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
        if (!Message.checkJavaIdentifier(name)) {
            throw new IllegalArgumentException();
        }
        map.put(name, object);
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
    public ListBuilder<CassandraException> list(String name) {
        if (!Message.checkJavaIdentifier(name)) {
            throw new IllegalArgumentException();
        }
        List<Object> list = new ArrayList<Object>();
        map.put(name, Collections.unmodifiableList(list));
        return new ListBuilder<CassandraException>(this, list);
    }

    /**
     * Create a map builder for a named map in the report structure. The given
     * name must be valid Java identifier.
     * 
     * @param name
     *            The map name.
     * @return A map builder to specify the map contents.
     */
    public MapBuilder<CassandraException> map(String name) {
        if (!Message.checkJavaIdentifier(name)) {
            throw new IllegalArgumentException();
        }
        Map<String, Object> subMap = new LinkedHashMap<String, Object>();
        map.put(name, Collections.unmodifiableMap(subMap));
        return new MapBuilder<CassandraException>(this, subMap);
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
    
    /**
     * Get the list in the report structure at the given path.
     * 
     * @param path
     *            The path.
     * @return The list found by navigating the path or null if the path does
     *         not exist.
     * @exception IllegalArgumentException
     *                If any part of the given path is not a valid Java
     *                identifier or list index.
     */
    public List<Object> getList(String path) {
        return message.getList(path);
    }

    /**
     * Get the map in the report structure at the given path.
     * 
     * @param path
     *            The path.
     * @return The map found by navigating the path or null if the path does not
     *         exist.
     * @exception IllegalArgumentException
     *                If any part of the given path is not a valid Java
     *                identifier or list index.
     */
    public Map<String, Object> getMap(String path) {
        return message.getMap(path);
    }

    /**
     * Get the report structure.
     * 
     * @return The report structure.
     */
    public Map<String, Object> getReport() {
        return Collections.unmodifiableMap(map);
    }

    /**
     * Create an detail message from the error message format associated with
     * the error code.
     * 
     * @return The exception message.
     */
    @Override
    public String getMessage() {
        return message.toString();
    }
}
