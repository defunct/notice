package com.goodworkalan.prattle.json;

import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonEntry {
    private final static int DATE = 0;
    
    private final static int LOGGER = 1;
    
    private final static int NAME = 2;
    
    private final static int LEVEL = 3;
    
    private final static int THREAD_ID = 4;
    
    private final static int JSON = 5;
    
    private final String[] line;
    
    public JsonEntry(String[] line) {
        this.line = line;
    }

    public Date getDate() {
        return new Date(Long.parseLong(line[DATE]));
    }
    
    public String getLogger() {
        return line[LOGGER];
    }
    
    public String getName() {
        return line[NAME];
    }
    
    public String getLevel() {
        return line[LEVEL];
    }
    
    public int getThreadId() {
        return Integer.parseInt(line[THREAD_ID]);
    }
    
    public String getJsonString() { 
        return line[JSON];
    }
    
    public JSONObject getJson() throws ParseException { 
        return (JSONObject) new JSONParser().parse(line[JSON]);
    }
}
