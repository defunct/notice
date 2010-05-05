package com.goodworkalan.notice.exception.api;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.goodworkalan.notice.exception.Clue;
import com.goodworkalan.notice.exception.NoticeException;
import com.goodworkalan.notice.exception.api.missing.BundleMissing;

public class NoticeExceptionTest {
    /**
     * Test the wrapper constructor.
     */
    @Test
    public void wrapperConstructor() {
        NullPointerException cause = new NullPointerException();
        NoticeException e = new TestException(101, cause);
        assertSame(e.getCause(), cause);
    }

    /**
     * Test list and map building.
     */
    @Test
    public void report() {
        NoticeException e = new TestException(100)
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
        Map<?, ?> report = (Map<?, ?>) e.get("vars");
        assertEquals(report.size(), 3);
        assertEquals(report.get("one"), 1);
        List<?> list = (List<?>) report.get("two");
        assertEquals(list.size(), 3);
        assertEquals(list.get(0), "a");
        List<?> subList = (List<?>) list.get(1);
        assertEquals(subList.size(), 2);
        assertEquals(subList.get(0), "b");
        assertEquals(list.get(2), null);
        Map<?, ?> subMap = (Map<?, ?>) subList.get(1);
        assertEquals(subMap.size(), 1);
        assertEquals(subMap.get("three"), 3);
        Map<?, ?> map = (Map<?, ?>) report.get("four");
        assertEquals(map.size(), 2);
        assertEquals(map.get("five"), Arrays.asList(1, 2, 3));
        subMap = (Map<?, ?>) map.get("six");
        assertEquals(subMap.size(), 1);
        assertEquals(subMap.get("seven"), 7);
        
        assertEquals(e.get("vars.four.six.seven"),  7);
        assertNull(e.get("vars.six.seven"));

        assertEquals(((Map<?, ?>) e.get("vars.four.six")).get("seven"),  7);
        assertEquals(e.get("vars.four.six.seven"), 7);
        assertNull(e.get("vars.four.six.eight"));
        assertNull(e.get("vars.four.six.eight.nine"));

        assertEquals(e.get("vars.two.1.0"), "b");
        assertEquals(((List<?>) e.get("vars.two.1")).get(0),  "b");
        assertNull(e.get("vars.two.2"));
        assertNull(e.get("vars.two.1.10"));
    }

    /**
     * Test an exception class with no message bundle.
     */
    @Test
    public void noBundle() {
        assertEquals(new BundleMissing(101).getMessage(), "Missing message bundle [com.goodworkalan.notice.exception.api.missing.exceptions]. Message key is [101]. (This is a meta error message.)");
    }

    /**
     * Test a message generated from a CassandraException derived from an
     * anonymous class.
     */
    @Test
    public void noCanonicalClassName() {
        assertEquals(new NoticeException(102, new Clue()) {
            private static final long serialVersionUID = 1L;
        }.getMessage(), "Basic message.");
    }

    /**
     * Test a message generated from a missing message string.
     */
    @Test
    public void missingMessage() {
        assertEquals(new TestException(100).getMessage(), "The message key [100] cannot be found in bundle [com.goodworkalan.notice.exception.api.exceptions]. (This is a meta error message.)");
    }

    /**
     * Test a message generated from an empty message string.
     */
    @Test
    public void blankMessage() {
        assertEquals(new TestException(101).getMessage(), "The message for message key [101] in bundle [com.goodworkalan.notice.exception.api.exceptions] is blank. (This is a meta error message.)");
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
        assertEquals(new TestException(104).map("map").put("name", "value").end().getMessage(), "Format exception [d != java.lang.String] for message key [104] in bundle [com.goodworkalan.notice.exception.api.exceptions]. (This is a meta error message.)");
    }

    /**
     * Test a message with a bad argument path identifier.
     */
    @Test
    public void badPathIdentifier() {
        assertEquals(new TestException(105).map("map").put("name", "value").end().getMessage(), "Invalid format argument name [vars.map.!] for message key [105] in bundle [com.goodworkalan.notice.exception.api.exceptions]. (This is a meta error message.)");
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
        assertEquals(new TestException(112).list("list").add("value").end().getMessage(), "Cannot find argument named [list.!] for message key [112] in bundle [com.goodworkalan.notice.exception.api.exceptions]. (This is a meta error message.)");
    }

    /**
     * Test a message with a bad list path, where instead of an index there
     * is a java identifier.
     */
    @Test
    public void listPathIdentifier() {
        assertEquals(new TestException(107).list("list").add("value").end().getMessage(), "Cannot find argument named [vars.list.zero] for message key [107] in bundle [com.goodworkalan.notice.exception.api.exceptions]. (This is a meta error message.)");
    }

    /**
     * Test a message that attempts to dereference an object that is not a
     * collection.
     */
    @Test
    public void notACollection() {
        assertEquals(new TestException(108).map("map").put("name", "value").end().getMessage(), "Cannot find argument named [map.name.path] for message key [108] in bundle [com.goodworkalan.notice.exception.api.exceptions]. (This is a meta error message.)");
    }
    

    /**
     * Test a message that attempts to dereference null.
     */
    @Test
    public void nullReference() {
        assertEquals(new TestException(109).map("map").put("name", "value").end().getMessage(), "Cannot find argument named [none.name] for message key [109] in bundle [com.goodworkalan.notice.exception.api.exceptions]. (This is a meta error message.)");
    }
    
    /**
     * Test a message that attempts to dereference a list element that does
     * not exist.
     */
    @Test
    public void outOfBounds() {
        assertEquals(new TestException(110).list("list").add("value").end().getMessage(), "Cannot find argument named [vars.list.1] for message key [110] in bundle [com.goodworkalan.notice.exception.api.exceptions]. (This is a meta error message.)");
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
