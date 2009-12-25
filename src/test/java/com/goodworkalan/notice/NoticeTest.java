package com.goodworkalan.notice;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * Test for the CassandraException class.
 * 
 * @author Alan Gutierrez
 */
public class NoticeTest {
    /**
     * Check a valid Java identifier.
     */
    @Test
    public void validJavaIdentifier() {
        assertTrue(Notice.checkJavaIdentifier("a"));
        assertTrue(Notice.checkJavaIdentifier("ab"));
    }

    /**
     * Check invalid Java identifiers.
     */
    @Test
    public void invalidJavaIdentifier() {
        assertFalse(Notice.checkJavaIdentifier(""));
        assertFalse(Notice.checkJavaIdentifier("1"));
        assertFalse(Notice.checkJavaIdentifier("a!"));
    }

    /**
     * Check a null Java identifier.
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void nullJavaIdentifier() {
        Notice.checkJavaIdentifier(null);
    }

    /**
     * Test the integer determination method.
     */
    @Test
    public void isInteger() {
        assertTrue(Notice.isInteger("10"));
        assertFalse(Notice.isInteger("!"));
    }
}
