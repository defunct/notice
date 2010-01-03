package com.goodworkalan.notice.message;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.goodworkalan.notice.message.Message;

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
        assertTrue(Message.checkJavaIdentifier("a"));
        assertTrue(Message.checkJavaIdentifier("ab"));
    }

    /**
     * Check invalid Java identifiers.
     */
    @Test
    public void invalidJavaIdentifier() {
        assertFalse(Message.checkJavaIdentifier(""));
        assertFalse(Message.checkJavaIdentifier("1"));
        assertFalse(Message.checkJavaIdentifier("a!"));
    }

    /**
     * Check a null Java identifier.
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void nullJavaIdentifier() {
        Message.checkJavaIdentifier(null);
    }

    /**
     * Test the integer determination method.
     */
    @Test
    public void isInteger() {
        assertTrue(Message.isInteger("10"));
        assertFalse(Message.isInteger("!"));
    }
}
