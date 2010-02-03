package com.goodworkalan.notice.message;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.annotations.Test;

/**
 * Test for the CassandraException class.
 * 
 * @author Alan Gutierrez
 */
public class MessageTest {
    /** Create a message with the given variables. */
    private Message makeMessage(Object variables) {
        return new Message(new ConcurrentHashMap<String, ResourceBundle>(), MessageTest.class.getCanonicalName(), "test_messages", "key", variables);
    }

    /**
     * Check the attribute accessors.
     */
    @Test
    public void accessors() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("a", "b");
        Message message = makeMessage(map);
        assertEquals(message.getBundleName(), "test_messages");
        assertEquals(message.getMessageKey(), "key");
        assertEquals(message.getContext(), "com.goodworkalan.notice.message.MessageTest");
        assertEquals(((Map<?, ?>) message.getVariables()).get("a"), "b");
    }
    
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
