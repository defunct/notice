package com.goodworkalan.notice.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

/**
 * Iterates over the JSON formatted Notice log entries contained in a collection
 * of JSON formatted Notice log files.
 * 
 * @author Alan Gutierrez
 */
class JsonLogFileIterator implements Iterator<JsonEntry> {
    /** The log directory. */
    private final File directory;

    /** The pattern used to filter out file names that are not log files. */
    private final Pattern pattern;

    /** The iterator over a list of log file names contained in the directory. */ 
    private final Iterator<String> files;
    
    /** The current, individual log file reader. */
    private Iterator<JsonEntry> entries;

    /**
     * Create a log file iterator that iterates a list of Notice log entries
     * within JSON formatted log files in a log file directory filtering out
     * files that do not match a given log file name pattern. The list of files
     * maybe a subset of all files in the directory that eliminates log files
     * containing entries before the start time filter.
     * 
     * @param directory
     *            The log directory.
     * @param pattern
     *            The pattern used to filter out file names that are not log
     *            files.
     * @param files
     *            The iterator over a list of log file names contained in the
     *            directory.
     */
    public JsonLogFileIterator(File directory, Pattern pattern, Iterator<String> files) {
        this.directory = directory;
        this.files = files;
        this.pattern = pattern;
        this.entries = iterate();
    }
    
    // TODO Document.
    private Iterator<JsonEntry> iterate() {
        while (files.hasNext()) {
            File file = new File(directory, files.next());
            if (pattern.matcher(file.getName()).matches()) {
                Iterator<JsonEntry> entries;
                try {
                    entries = new JsonEntryIterator(new BufferedReader(new FileReader(file)));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                if (entries.hasNext()) {
                    return entries;
                }
            }
        }
        return null;
    }

    // TODO Document.
    public boolean hasNext() {
        return entries != null;
    }
    
    // TODO Document.
    public JsonEntry next() {
        if (entries == null) {
            throw new NoSuchElementException();
        }
        JsonEntry entry = entries.next();
        if (!entries.hasNext()) {
            entries = iterate();
        }
        return entry;
    }
    
    // TODO Document.
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
