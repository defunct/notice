package com.goodworkalan.notice.exception.api;

import com.goodworkalan.cassandra.NoticeException;
import com.goodworkalan.cassandra.Clue;

/**
 * Test exception class for a message with no bundle.
 * 
 * @author Alan Gutierrez
 */
public class BundleMissing extends NoticeException {
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
