package com.goodworkalan.notice.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates the entries in a JSON formatted Notice log file.
 *
 * @author Alan Gutierrez
 */
class JsonEntryIterator implements Iterator<JsonEntry> {
    /** The log file reader. */
    private final BufferedReader reader;

    /** The next line. */
    private String line;
    
    /**
     * Create an entry iterator using the given log file reader.
     * 
     * @param reader
     *            The reader.
     */
    public JsonEntryIterator(BufferedReader reader) {
        this.reader = reader;
        this.line = iterate();
    }

    /**
     * Advance to the next line.
     * 
     * @return The next line.
     */
    private String iterate() {
        try {
            String line = reader.readLine();
            if (line == null) {
                reader.close();
            }
            return line;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Return true if there is another entry in the log file.
     * 
     * @return True if there are more entries.
     */
    public boolean hasNext() {
        return line != null;
    }

    /**
     * Return the next entry in the log file.
     * 
     * @exception NoSuchElementException
     *                If there are no more entries in the log file.
     */
    public JsonEntry next() {
        if (line == null) {
            throw new NoSuchElementException();
        }
        JsonEntry entry = new JsonEntry(line.split("\\s", 6));
        line = iterate();
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
