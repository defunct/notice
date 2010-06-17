package com.goodworkalan.notice;

import com.goodworkalan.danger.CodedDanger;

/**
 * A general purpose exception that indicates that an error occurred in one 
 * of the classes in the notice package.
 *   
 * @author Alan Gutierrez
 */
public final class NoticeException extends CodedDanger {
    /** The serial version id. */
    private static final long serialVersionUID = 20080620L;
    
    /** A variable substitution in the property file creates an infinite loop. */
    public final static int PROPERTY_LOOP = 101;
    
    /** A property value ends with a backslash character, does not actually escape anything. */
    public final static int TERMINAL_BACKSLASH = 102;

    /**
     * Create a Sheaf exception with the given error code.
     * 
     * @param code
     *            The error code.
     * @param arguments
     *            The positioned format arguments.
     */
    public NoticeException(int code, Object...arguments) {
        super(code, null, arguments);
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
        super(code, cause, arguments);
    }
}