package com.goodworkalan.notice.exception.api;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.goodworkalan.cassandra.NoticeException;
import com.goodworkalan.cassandra.Clue;

import static org.testng.Assert.*;
/**
 * Tests for the report structure.
 *
 * @author Alan Gutierrez
 */
public class ClueTest {
    /**
     * Test mark and clear.
     */
    @Test
    public void markAndClear() {
        Clue report = new Clue();
        report.put("a", 1);
        report.mark();
        report
            .list("b")
                .add(1).add(2).add(3)
                .end()
            .map("c")
                .put("d", 1)
                .end();
        Object mark2 = report.mark();
        report
            .put("e", 3)
            .list("f")
                .add(4).add(5)
                .end();
        Object mark3 = report.mark();
        report
            .put("g", 4);
        report.clear(mark2);
        report.clear(mark3);
        report.put("g", 5);
        NoticeException e = new TestException(101, report);
        assertEquals(e.get("vars.a"), 1);
        assertEquals(e.get("vars.b"), Arrays.asList(1, 2, 3));
        assertEquals(e.get("vars.c.d"), 1);
        assertNull(e.get("vars.e"));
        assertNull(e.get("vars.f"));
        assertEquals(e.get("vars.g"), 5);
    }

    /**
     * Test calling clear on a report with no markers.
     */
    @Test(expectedExceptions = IllegalStateException.class)
    public void clearNegative() {
        new Clue().clear(new Object());
    }
}
