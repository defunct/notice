package com.goodworkalan.prattle.json;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

public class JsonEntryReaderTest {
    @Test
    public void read() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date start = format.parse("2009-12-15 21:01");
        Iterable<JsonEntry> entries = new JsonEntryReader(new File("src/test/resources"), "prattle_json", start);
        for (JsonEntry entry : entries) { 
            assertTrue(start.before(entry.getDate()));
            entry.getThreadId();
        }
    }
}
