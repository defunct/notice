package com.goodworkalan.notice.message;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.Arrays;
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
    
    private Message makePopulatedMessage() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> subMap = new HashMap<String, Object>();
        map.put("a", "b");
        map.put("b", subMap);
        subMap.put("c", "d");
        subMap.put("e", Arrays.asList("a", "b", "c"));
        return makeMessage(map);
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
     * Check a single map dereference.
     */
    @Test
    public void mapGet() {
        assertEquals(makePopulatedMessage().get("a"), "b");
    }
   
    /**
     * Check getting a value from a nested map.
     */
    @Test
    public void nestedMapGet() {
        assertEquals(makePopulatedMessage().get("b.c"), "d");
    }
    
    /**
     * Check getting a list value.
     */
    @Test
    public void listGet() {
        assertEquals(makePopulatedMessage().get("b.e.1"), "b");
    }
    
    /**
     * Check referencing a null hash element.
     */
    @Test
    public void noSuchKey() {
        assertNull(makePopulatedMessage().get("z"));
    }
    
    /**
     * Check a missing hash element.
     */
    @Test
    public void noSuchHashElement() {
        assertNull(makePopulatedMessage().get("b.c.d"));
    }
    
    /**
     * Check an out of range array element.
     */
    @Test
    public void noSuchListElement() {
        assertNull(makePopulatedMessage().get("b.e.8"));
        assertNull(makePopulatedMessage().get("b.e.3"));
    }

    /**
     * Check referencing an array with a valid Java identifier.
     */
    @Test
    public void noSuchStringElementInList() {
        assertNull(makePopulatedMessage().get("b.e.f"));
    }
}
