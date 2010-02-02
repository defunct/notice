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

public class JsonEntryReader implements Iterable<JsonEntry> {
    private final File directory;
    
    private final Date start;
    
    private final String prefix;
    
    private final DateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
    
    private final Pattern pattern;
    
    public JsonEntryReader(File directory, String prefix, Date start) {
        this.directory = directory;
        this.start = start;
        this.prefix = prefix;
        this.pattern = Pattern.compile(prefix + "_(\\d{4}_\\d{2}_\\d{2}_\\d{2}_\\d{2})$"); 
    }
    
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
