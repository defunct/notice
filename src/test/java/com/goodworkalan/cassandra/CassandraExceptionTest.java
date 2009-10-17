package com.goodworkalan.cassandra;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

/**
 * Test for the CassandraException class.
 * 
 * @author Alan Gutierrez
 */
public class CassandraExceptionTest {
    /**
     * Check a valid Java identifier.
     */
    @Test
    public void validJavaIdentifier() {
        assertTrue(CassandraException.checkJavaIdentifier("a"));
        assertTrue(CassandraException.checkJavaIdentifier("ab"));
    }

    /**
     * Check invalid Java identifiers.
     */
    @Test
    public void invalidJavaIdentifier() {
        assertFalse(CassandraException.checkJavaIdentifier(""));
        assertFalse(CassandraException.checkJavaIdentifier("1"));
        assertFalse(CassandraException.checkJavaIdentifier("a!"));
    }

    /**
     * Check a null Java identifier.
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void nullJavaIdentifier() {
        CassandraException.checkJavaIdentifier(null);
    }

    /**
     * Test the integer determination method.
     */
    @Test
    public void isInteger() {
        assertTrue(CassandraException.isInteger("10"));
        assertFalse(CassandraException.isInteger("!"));
    }

    /**
     * Test the wrapper constructor.
     */
    @Test
    public void wrapperConstructor() {
        NullPointerException cause = new NullPointerException();
        CassandraException e = new TestException(101, cause);
        assertSame(e.getCause(), cause);
    }

    /**
     * Test list and map building.
     */
    @Test
    public void report() {
        CassandraException e = new TestException(100)
            .put("one", 1)
            .list("two")
                .add("a")
                .list()
                    .add("b")
                    .map()
                        .put("three", 3)
                        .end()
                    .end()
                .add(null)
                .end()
            .map("four")
                .list("five")
                    .add(1)
                    .add(2)
                    .add(3)
                    .end()
                .map("six")
                    .put("seven", 7)
                    .end()
                .end();
        Map<String, Object> report = e.getReport();
        assertEquals(report.size(), 3);
        assertEquals(report.get("one"), (Integer) 1);
        List<?> list = (List<?>) report.get("two");
        assertEquals(list.size(), 3);
        assertEquals(list.get(0), "a");
        List<?> subList = (List<?>) list.get(1);
        assertEquals(subList.size(), 2);
        assertEquals(subList.get(0), "b");
        assertEquals(list.get(2), null);
        Map<?, ?> subMap = (Map<?, ?>) subList.get(1);
        assertEquals(subMap.size(), 1);
        assertEquals(subMap.get("three"), (Integer) 3);
        Map<?, ?> map = (Map<?, ?>) report.get("four");
        assertEquals(map.size(), 2);
        assertEquals(map.get("five"), Arrays.asList(1, 2, 3));
        subMap = (Map<?, ?>) map.get("six");
        assertEquals(subMap.size(), 1);
        assertEquals(subMap.get("seven"), (Integer) 7);
        
        assertEquals(e.get("four.six.seven"),  (Integer) 7);
        assertNull(e.get("six.seven"));

        assertEquals(e.getMap("four.six").get("seven"),  (Integer) 7);
        assertNull(e.getMap("four.six.seven"));
        assertNull(e.getMap("four.six.eight"));
        assertNull(e.getMap("four.six.eight.nine"));

        assertEquals(e.get("two.1.0"), "b");
        assertEquals(e.getList("two.1").get(0),  "b");
        assertNull(e.getList("two.1.0"));
        assertNull(e.getList("two.2"));
        assertNull(e.getList("two.1.10"));
    }

    /**
     * Test putting an object with an invalid name into an exception report.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void badExceptionPutName() {
        new TestException(101).put("!", 1);
    }

    /**
     * Test putting a list with invalid name into an exception report.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void badExceptionListName() {
        new TestException(101).list("!");
    }
    
    /**
     * Test putting a map with an invalid name into an exception report.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void badExceptionMapName() {
        new TestException(101).map("!");
    }
    

    /**
     * Test putting an object with an invalid name into a map.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void badMapPutName() {
        new TestException(101).map("map").put("!", 1);
    }

    /**
     * Test putting a list with invalid name into a map.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void badMapListName() {
        new TestException(101).map("map").list("!");
    }
    
    /**
     * Test putting a map with an invalid name into a map.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void badMapMapName() {
        new TestException(101).map("map").map("!");
    }

    /**
     * Test an exception class with no message bundle.
     */
    @Test
    public void noBundle() {
        assertEquals(new BundleMissing(101).getMessage(), "101");
    }

    /**
     * Test a message generated from a CassandraException derived from an
     * anonymous class.
     */
    @Test
    public void noCanonicalClassName() {
        assertEquals(new CassandraException(101, new Report()) {
            private static final long serialVersionUID = 1L;
        }.getMessage(), "101");
    }

    /**
     * Test a message generated from a missing message string.
     */
    @Test
    public void missingMessage() {
        assertEquals(new TestException(100).getMessage(), "100");
    }

    /**
     * Test a message generated from an empty message string.
     */
    @Test
    public void blankMessage() {
        assertEquals(new TestException(101).getMessage(), "101");
    }
    
    /**
     * Test a message with no arguments.
     */
    @Test
    public void basicMessage() {
        assertEquals(new TestException(102).getMessage(), "Basic message.");
    }
    
    /**
     * Test a message with a basic argument path.
     */
    @Test
    public void basicArgumentMessage() {
        assertEquals(new TestException(103).map("map").put("name", "value").end().getMessage(), "Value is value.");
    }
    
    /**
     * Test a message with a bad format.
     */
    @Test
    public void badFormat() {
        assertEquals(new TestException(104).map("map").put("name", "value").end().getMessage(), "104");
    }

    /**
     * Test a message with a bad argument path identifier.
     */
    @Test
    public void badPathIdentifier() {
        assertEquals(new TestException(105).map("map").put("name", "value").end().getMessage(), "105");
    }

    /**
     * Test a message with a list path.
     */
    @Test
    public void listPath() {
        assertEquals(new TestException(106).list("list").add("value").end().getMessage(), "Value is value.");
    }

    /**
     * Test a message with a list path.
     */
    @Test
    public void badListPath() {
        assertEquals(new TestException(112).list("list").add("value").end().getMessage(), "112");
    }

    /**
     * Test a message with a bad list path, where instead of an index there
     * is a java identifier.
     */
    @Test
    public void listPathIdentifier() {
        assertEquals(new TestException(107).list("list").add("value").end().getMessage(), "107");
    }

    /**
     * Test a message that attempts to dereference an object that is not a
     * collection.
     */
    @Test
    public void notACollection() {
        assertEquals(new TestException(108).map("map").put("name", "value").end().getMessage(), "108");
    }
    

    /**
     * Test a message that attempts to dereference null.
     */
    @Test
    public void nullReference() {
        assertEquals(new TestException(109).map("map").put("name", "value").end().getMessage(), "109");
    }
    
    /**
     * Test a message that attempts to dereference a list element that does
     * not exist.
     */
    @Test
    public void outOfBounds() {
        assertEquals(new TestException(110).list("list").add("value").end().getMessage(), "110");
    }
    
    
    /**
     * Test a message that references a null value in a format argument.
     */
    @Test
    public void nullInFormat() {
        assertEquals(new TestException(111).getMessage(), "Value is null.");
    }
    
    /**
     * Test the code accessor.
     */
    @Test
    public void getCode() {
        assertEquals(new TestException(101).getCode(), 101);
    }
}
