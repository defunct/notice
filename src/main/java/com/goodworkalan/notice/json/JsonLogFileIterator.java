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

    /**
     * Create an iterator over the entries in the next log file in the log file
     * collection, or return null if there are no more log files to read.
     * 
     * @return An iterator over the next log file in the log file iterator.
     */
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

    /**
     * Determine if there is another log file entry in the log file collection.
     * 
     * @return True if there is another log file entry in the log file
     *         collection.
     */
    public boolean hasNext() {
        return entries != null;
    }

    /**
     * Get the next entry in the lot file collection.
     * 
     * @return The next entry in the log file collection.
     * @exception NoSuchElementException
     *                If there are no more entries in the log file.
     */
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
    
    /**
     * This method is unsupported by this implementation.
     * 
     * @exception UnsupportedOperationException
     *                Entries cannot be removed.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
