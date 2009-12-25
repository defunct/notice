package com.goodworkalan.cassandra;

import java.util.Arrays;

import org.testng.annotations.Test;
import static org.testng.Assert.*;
/**
 * Tests for the report structure.
 *
 * @author Alan Gutierrez
 */
public class ReportTest {
    /**
     * Test putting an invalid Java identifier.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void putInvalidName() {
        Clue report = new Clue();
        report.put("!", null);
    }
    
    /**
     * Test adding a list with an invalid Java identifier.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void listInvalidName() {
        new Clue().list("!");
    }
    
    /**
     * Test adding a map with an invalid Java identifier.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mapInvalidName() {
        new Clue().map("!");
    }
    
    /**
     * Test mark and clear.
     */
    @Test
    public void markAndClear() {
        Clue report = new Clue();
        report.put("a", 1);
        Object mark1 = report.mark();
        report
            .list("b")
                .add(1).add(2).add(3)
                .end()
            .map("c")
                .put("d", 1)
                .end();
        report.mark();
        report
            .put("e", 3)
            .list("f")
                .add(4).add(5)
                .end();
        Object mark2 = report.mark();
        report
            .put("g", 4);
        report.clear(mark1);
        report.clear(mark2);
        report.put("g", 5);
        CassandraException e = new TestException(101, report);
        assertEquals(e.get("a"), 1);
        assertEquals(e.get("b"), Arrays.asList(1, 2, 3));
        assertEquals(e.get("c.d"), 1);
        assertNull(e.get("e"));
        assertNull(e.get("f"));
        assertEquals(e.get("g"), 5);
    }

    /**
     * Test calling clear on a report with no markers.
     */
    @Test(expectedExceptions = IllegalStateException.class)
    public void clearNegative() {
        new Clue().clear(new Object());
    }
}
