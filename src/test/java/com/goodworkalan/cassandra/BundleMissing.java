package com.goodworkalan.cassandra;

/**
 * Test exception class for a message with no bundle.
 * 
 * @author Alan Gutierrez
 */
public class BundleMissing extends CassandraException {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;

    /**
     * Create an exception with the given error code.
     * 
     * @param code
     *            The error code.
     */
    public BundleMissing(int code) {
        super(code, new Clue());
    }
}
