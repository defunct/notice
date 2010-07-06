package com.goodworkalan.notice.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Iterates over a collection of rotated JSON formatted Notice logs kept
 * in a single directory.  
 *
 * @author Alan Gutierrez
 */
public class JsonEntryReader implements Iterable<JsonEntry> {
    /** The log file directory. */
    private final File directory;
    
    /** The lower boundary for entry start times to filter. */
    private final Date start;
    
    /** The file prefix for rotated log files. */
    private final String prefix;
    
    /** The date format for create times. */
    private final DateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
    
    /** Matches a rotated Notice log file and extracts the rotate time. */
    private final Pattern pattern;

    /**
     * Create a reader of JSON formatted log entry files.
     * 
     * @param directory
     *            The lot file directory.
     * @param prefix
     *            The log file prefix.
     * @param start
     *            The time of the first log entry to return.
     */
    public JsonEntryReader(File directory, String prefix, Date start) {
        this.directory = directory;
        this.start = start;
        this.prefix = prefix;
        this.pattern = Pattern.compile(prefix + "_(\\d{4}_\\d{2}_\\d{2}_\\d{2}_\\d{2})$"); 
    }

    /**
     * Obtain an iterator over the log files in the directory containing log
     * entries that occurred after the filter start time.
     * 
     * @return An iterator over the log file names.
     */
    private Iterator<String> files() {
        SortedSet<String> sorted = new TreeSet<String>(Arrays.asList(directory.list()));
        Iterator<String> files = sorted.subSet(prefix + "_", prefix + "_9").iterator();
        String previous = null;
        while (files.hasNext()) {
            String file = files.next();
            Matcher matcher = pattern.matcher(file);
            if (matcher.matches()) {
                Date date;
                try {
                    date = format.parse(matcher.group(1));
                } catch (ParseException e) {
                    continue;
                }
                if (date.equals(start)) {
                    return sorted.subSet(file, prefix + "_9").iterator();
                } else if (date.after(start)) {
                    if (previous == null) {
                        return sorted.subSet(file, prefix + "_9").iterator();
                    }
                    return sorted.subSet(previous, prefix + "_9").iterator();
                }
                previous = file;
            }
        }
        return Collections.<String>emptyList().iterator();
    }

    /**
     * Create an iterator over the JSON formatted Notice log entries that moves
     * from file to file in a directory containing rotated JSON formated Notice
     * logs.
     * 
     * @return An iterator over the log entries.
     */
    public Iterator<JsonEntry> iterator() {
        if (directory.isDirectory()) {
            return new JsonLogFileIterator(directory, pattern, files());
        }
        try {
            return new JsonEntryIterator(new BufferedReader(new FileReader(directory)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
