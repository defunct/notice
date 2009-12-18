package com.goodworkalan.prattle.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

class JsonLogFileIterator implements Iterator<JsonEntry> {
    private final File directory;

    private final Pattern pattern;

    private final Iterator<String> files;
    
    private Iterator<JsonEntry> entries;

    public JsonLogFileIterator(File directory, Pattern pattern, Iterator<String> files) {
        this.directory = directory;
        this.files = files;
        this.pattern = pattern;
        this.entries = iterate();
    }
    
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

    public boolean hasNext() {
        return entries != null;
    }
    
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
    
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
