package com.goodworkalan.notice.message;

import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentMap;

public class Message {
    /** Cache of resource bundles. */
  private final ConcurrentMap<String, ResourceBundle> bundles;
    
    /**
     * The notice context, which is a class name, but it is not a class, because
     * the class name is sometimes obtained from a SLF4J logger, which converts
     * a class name into a string for use as its own name.
     */
    private final String context;
    
    /** The bundle name to be appended to the context package. */
    private final String bundleName;
    
    private final String messageKey;

    private final Object variables;
    
    public Message(ConcurrentMap<String, ResourceBundle> bundles, String context, String bundleName, String key, Object variables) {
        this.bundles = bundles;
        this.context = context;
        this.bundleName = bundleName;
        this.variables = variables;
        this.messageKey = key;
    }

    public String getBundleName() {
        return bundleName;
    }
    
    public String getMessageKey() {
        return messageKey;
    }
    
    public String getContext() {
        return context;
    }
    
    public Object getVariables() {
        return variables;
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
        int start = -1, end, stop = path.length();
        Object current = variables;
        while (start != stop) {
            start++;
            end = path.indexOf('.', start);
            if (end == -1) {
                end = stop;
            }
            String name = path.substring(start, end);
            start = end;
            if (current instanceof Map<?, ?>) {
                if (!checkJavaIdentifier(name)) {
                    throw new IllegalArgumentException();
                }
                current = ((Map<?, ?>) current).get(name);
            } else if (current instanceof List<?>) {
                if (!isInteger(name)) {
                    if (!checkJavaIdentifier(name)) {
                        throw new IllegalArgumentException();
                    }
                    throw new NoSuchElementException();
                }
                int index = Integer.parseInt(name, 10);
                List<?> list = (List<?>) current;
                if (index >= list.size()) {
                    throw new NoSuchElementException();
                }
                current = list.get(index);
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

    public String toString() {
        int lastDot = context.lastIndexOf('.');
        String packageName = context.substring(0, lastDot == -1 ? context.length() : lastDot);
        String bundlePath = packageName + "." + bundleName;
        ResourceBundle bundle = bundles.get(bundlePath);
        if (bundle == null) {
            try {
                bundle = ResourceBundle.getBundle(bundlePath);
            } catch (MissingResourceException e) {
                bundle = ResourceBundle.getBundle("com.goodworkalan.notice.message.missing");
            }
            bundles.put(bundlePath, bundle);
        }
        String key = messageKey;
        String format;
        try {
            format = bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
        format = format.trim();
        if (format.length() == 0) {
            return key;
        }
        int tilde = format.indexOf("~");
        if (tilde == -1) {
            return format;
        }
        String paths = format.substring(0, tilde);
        format = format.substring(tilde + 1);
        int length = 0, index = 0;
        for (;;) {
            length++;
            index = paths.indexOf(',', index);
            if (index == -1) {
                break;
            }
            index += 1;
        }
        Object[] arguments = new Object[length];
        int start = -1, end, stop = paths.length(), position = 0;
        while (start != stop) {
            start++;
            end = paths.indexOf(',', start);
            if (end == -1) {
                end = stop;
            }
            String name = paths.substring(start, end);
            start = end;
            Object argument = "";
            try {
                argument = getValue(name);
            } catch (IllegalArgumentException e) {
                return key;
            } catch (NoSuchElementException e) {
                return key;
            }
            arguments[position++] = argument;
        }
        try {
            return String.format(format, arguments);
        } catch (RuntimeException e) {
            return key;
        }
    }
}
