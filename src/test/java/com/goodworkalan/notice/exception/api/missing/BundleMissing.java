package com.goodworkalan.notice.exception.api.missing;

import com.goodworkalan.notice.exception.Clue;
import com.goodworkalan.notice.exception.NoticeException;

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
