package com.goodworkalan.notice.message;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * Test for the CassandraException class.
 * 
 * @author Alan Gutierrez
 */
public class MessageTest {
    /**
     * Check a valid Java identifier.
     */
    @Test
    public void validJavaIdentifier() {
        assertTrue(Indexes.checkJavaIdentifier("a"));
        assertTrue(Indexes.checkJavaIdentifier("ab"));
    }

    /**
     * Check invalid Java identifiers.
     */
    @Test
    public void invalidJavaIdentifier() {
        assertFalse(Indexes.checkJavaIdentifier(""));
        assertFalse(Indexes.checkJavaIdentifier("1"));
        assertFalse(Indexes.checkJavaIdentifier("a!"));
    }

    /**
     * Check a null Java identifier.
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void nullJavaIdentifier() {
        Indexes.checkJavaIdentifier(null);
    }

    /**
     * Test the integer determination method.
     */
    @Test
    public void isInteger() {
        assertTrue(Indexes.isInteger("10"));
        assertFalse(Indexes.isInteger("!"));
    }
}
