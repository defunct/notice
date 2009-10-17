package com.goodworkalan.cassandra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A reporting structure that is used to gather information about the state of
 * the program in case an exception is thrown.
 * 
 * @author Alan Gutierrez
 */
public class Report {
    /** The map of properties. */
    private final Map<String, Object> map = new LinkedHashMap<String, Object>();

    /** A list of properties added for each marker. */
    private final List<List<String>> markers = new ArrayList<List<String>>();

    /**
     * Create a marker that can be used to clear out all subsequently assigned
     * properties.
     * 
     * @return This report to allow for chained invocation of report builder
     *         methods.
     */
    public Report mark() {
        markers.add(new ArrayList<String>());
        return this;
    }

    /**
     * Get an unmodifiable copy of the underlying report map.
     * 
     * @return An unmodifiable copy of the underlying report map.
     */
    public Map<String, Object> getReportMap() {
        return Collections.unmodifiableMap(map);
    }

    /**
     * Record a name put into the report in the marker set.
     * 
     * @param name
     *            The name put into the report.
     */
    public void recordName(String name) {
        int size = markers.size();
        if (size != 0) {
            markers.get(size - 1).add(name);
        }
    }

    /**
     * Put a named value into the exception report structure. The given name
     * must be valid Java identifier.
     * 
     * @param name
     *            The property name.
     * @param object
     *            The property value.
     * @return This report to allow for chained invocation of report builder
     *         methods.
     */
    public Report put(String name, Object object) {
        if (!CassandraException.checkJavaIdentifier(name)) {
            throw new IllegalArgumentException();
        }
        recordName(name);
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
    public ListBuilder<Report> list(String name) {
        if (!CassandraException.checkJavaIdentifier(name)) {
            throw new IllegalArgumentException();
        }
        recordName(name);
        List<Object> list = new ArrayList<Object>();
        map.put(name, Collections.unmodifiableList(list));
        return new ListBuilder<Report>(this, list);
    }

    /**
     * Create a map builder for a named map in the report structure. The given
     * name must be valid Java identifier.
     * 
     * @param name
     *            The map name.
     * @return A map builder to specify the map contents.
     */
    public MapBuilder<Report> map(String name) {
        if (!CassandraException.checkJavaIdentifier(name)) {
            throw new IllegalArgumentException();
        }
        recordName(name);
        Map<String, Object> subMap = new LinkedHashMap<String, Object>();
        map.put(name, Collections.unmodifiableMap(subMap));
        return new MapBuilder<Report>(this, subMap);
    }

    /**
     * Remove all of the properties assigned since the last call to
     * {@link #mark()}.
     */
    public void clear() {
        if (markers.isEmpty()) {
            throw new IllegalStateException();
        }
        int index = markers.size() - 1;
        for (String marker : markers.get(index)) {
            map.remove(marker);
        }
        markers.remove(index);
    }
}
