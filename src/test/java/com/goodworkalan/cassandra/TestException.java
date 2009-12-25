package com.goodworkalan.cassandra;

/**
 * A test exception derived from CassandraException.
 * 
 * @author Alan Gutierrez
 */
public class TestException extends CassandraException {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a test exception with the given error code.
     * 
     * @param code
     *            The error code.
     */
    public TestException(int code) {
        super(code, new Clue());
    }

    /**
     * Create a test exception with the given error code that wraps the given
     * cause exception.
     * 
     * @param code
     *            The error code.
     * @param cause
     *            The cause.
     */
    public TestException(int code, Throwable cause) {
        super(code, new Clue(), cause);
    }

    /**
     * Create a test exception with the given error code and the given report
     * structure.
     * 
     * @param code
     *            The error code.
     */
    public TestException(int code, Clue report) {
        super(code, report);
    }
}
