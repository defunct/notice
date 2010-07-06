package com.goodworkalan.notice;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * A general purpose exception that indicates that an error occurred in one 
 * of the classes in the notice package.
 *   
 * @author Alan Gutierrez
 */
public final class NoticeException extends RuntimeException {
    /** The serial version id. */
    private static final long serialVersionUID = 20080620L;
    
    /** A variable substitution in the property file creates an infinite loop. */
    public final static int PROPERTY_LOOP = 101;
    
    /** A property value ends with a backslash character, does not actually escape anything. */
    public final static int TERMINAL_BACKSLASH = 102;
    
    /** The error code. */
    private final int code;
    
    /** The error message. */
    private final String message;

    /**
     * Create a Sheaf exception with the given error code.
     * 
     * @param code
     *            The error code.
     * @param arguments
     *            The positioned format arguments.
     */
    public NoticeException(int code, Object...arguments) {
        this(code, null, arguments);
    }

    /**
     * Wrap the given cause exception in an addendum exception with the given
     * error code.
     * 
     * @param code
     *            The error code.
     * @param cause
     *            The cause exception.
     * @param arguments
     *            The positioned format arguments.
     */
    public NoticeException(int code, Throwable cause, Object...arguments) {
        super(null, cause);
        this.message = formatMessage(code, arguments);
        this.code = code;
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
     * Get the error message.
     * 
     * @return The error message.
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Format the exception message using the message arguments to format the
     * message found with the message key in the message bundle found in the
     * package of the given context class.
     * 
     * @param contextClass
     *            The context class.
     * @param code
     *            The error code.
     * @param arguments
     *            The format message arguments.
     * @return The formatted message.
     */
    private String formatMessage(int code, Object...arguments) {
        String baseName = getClass().getPackage().getName() + ".exceptions";
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(baseName, Locale.getDefault(), Thread.currentThread().getContextClassLoader());
            return String.format((String) bundle.getObject(Integer.toString(code)), arguments);
        } catch (Exception e) {
            return String.format("Cannot load message key [%s] from bundle [%s] becuase [%s].", code, baseName, e.getMessage());
        }
    }
}