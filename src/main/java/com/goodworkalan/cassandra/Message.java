package com.goodworkalan.cassandra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

public class Message {
    private final Map<String, Object> map;
    
    private final ResourceBundle bundle;
    
    private final String key;
    
    private final String missing;
    
    public Message(ResourceBundle bundle, Map<String, Object> map, String key, String missing) {
        this.bundle = bundle;
        this.map = map;
        this.key = key;
        this.missing = missing;
    }

    /**
     * Determine whether a string represents an integer.
     * 
     * @param name
     *            The integer string.
     * @return True if the string represents an integer.
     */
    static boolean isInteger(String name) {
        for (int i = 0, stop = name.length(); i < stop; i++) {
            if (!Character.isDigit(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determine if the given name is a valid Java identifier.
     * 
     * @param name
     *            The name to check.
     * @return True if the name is valid Java identifier.
     * @exception NullPointerException
     *                If the name is null.
     */
    static boolean checkJavaIdentifier(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (name.length() == 0) {
            return false;
        }
        if (!Character.isJavaIdentifierStart(name.charAt(0))) {
            return false;
        }
        for (int i = 1, stop = name.length(); i < stop; i++) {
            if (!Character.isJavaIdentifierPart(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    /**
     * Evaluate the given path against the report structure.
     * 
     * @param path
     *            The path.
     * @return The value found by navigating the path or null if the path does
     *         not exist.
     * @exception IllegalArgumentException
     *                If any part of the given path is not a valid Java
     *                identifier or list index.
     */
    private Object getValue(String path) {
        String[] parts = path.trim().split("\\.");
        Object current = map;
        for (int j = 0; j < parts.length; j++) {
            if (current == null) {
                throw new NoSuchElementException();
            }
            if (current instanceof List<?>) {
                if (!isInteger(parts[j])) {
                    if (!checkJavaIdentifier(parts[j])) {
                        throw new IllegalArgumentException();
                    }
                    throw new NoSuchElementException();
                }
                int index = Integer.parseInt(parts[j], 10);
                List<?> list = (List<?>) current;
                if (index >= list.size()) {
                    throw new NoSuchElementException();
                }
                current = list.get(index);
            } else if (current instanceof Map<?, ?>) {
                if (!checkJavaIdentifier(parts[j])) {
                    throw new IllegalArgumentException();
                }
                current = ((Map<?, ?>) current).get(parts[j]);
            } else {
                throw new NoSuchElementException();
            }
        }
        return current;
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
        try {
            return getValue(path);
        } catch (NoSuchElementException e) {
            return null;
        }
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
        Object object;
        try {
            object = getValue(path);
        } catch (NoSuchElementException e) {
            return null;
        }
        if (object == null) {
            return null;
        }
        if (object instanceof List<?>) {
            // The copy constructor satisfies my aversion to @SuppressWarnings.
            return Collections.unmodifiableList(new ArrayList<Object>((List<?>) object));
        }
        return null;
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
        Object object;
        try {
            object = getValue(path);
        } catch (NoSuchElementException e) {
            return null;
        }
        if (object == null) {
            return null;
        }
        if (object instanceof Map<?, ?>) {
            // We will have no @SuppressWarnings.
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
                map.put(entry.getKey().toString(), entry.getValue());
            }
            return Collections.unmodifiableMap(map);
        }
        return null;
    }

    @Override
    public String toString() {
        String format;
        try {
            format = bundle.getString(key);
        } catch (MissingResourceException e) {
            format = missing;
        }
        format = format.trim();
        if (format.length() == 0) {
            return key;
        }
        if (!format.contains("~")) {
            return format;
        }
        String[] record = format.split("~", 2);
        String[] paths = record[0].split(",");
        Object[] arguments = new Object[paths.length];
        for (int i = 0, stop = paths.length; i < stop; i++) {
            Object argument;
            try {
                argument = getValue(paths[i]);
            } catch (IllegalArgumentException e) {
                return key;
            } catch (NoSuchElementException e) {
                return key;
            }
            arguments[i] = argument;
        }
        try {
            return String.format(record[1], arguments);
        } catch (Throwable e) {
            return key;
        }
    }
}
