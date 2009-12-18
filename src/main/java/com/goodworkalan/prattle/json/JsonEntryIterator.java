package com.goodworkalan.prattle.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

class JsonEntryIterator implements Iterator<JsonEntry> {
    private final BufferedReader reader;

    private String line;
    
    public JsonEntryIterator(BufferedReader reader) {
        this.reader = reader;
        this.line = iterate();
    }
    
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
    
    public boolean hasNext() {
        return line != null;
    }
    
    public JsonEntry next() {
        if (line == null) {
            throw new NoSuchElementException();
        }
        JsonEntry entry = new JsonEntry(line.split("\\s", 6));
        line = iterate();
        return entry;
    }
    
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
