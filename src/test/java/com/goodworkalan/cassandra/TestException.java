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
        super(code, new Report());
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
        super(code, new Report(), cause);
    }

    /**
     * Create a test exception with the given error code and the given report
     * structure.
     * 
     * @param code
     *            The error code.
     */
    public TestException(int code, Report report) {
        super(code, report);
    }
}
