package com.goodworkalan.notice.json;

import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * An entry in a Notice JSON formatted log file. 
 *
 * @author Alan Gutierrez
 */
public class JsonEntry {
    /** The index of the create time. */
    private final static int DATE = 0;
    
    /** The index of the context class name. */
    private final static int CONTEXT = 1;
    
    /** The index of the name of the notice code. */ 
    private final static int CODE = 2;
    
    /** The index of the logging level. */
    private final static int LEVEL = 3;
    
    /** The index of the thread id. */
    private final static int THREAD_ID = 4;
    
    /** The index of the JSON string. */
    private final static int JSON = 5;
    
    /** The log file line split by whitespace. */
    private final String[] line;
    
    /**
     * Create a JSON entry from the given log file line.
     * 
     * @param line
     *            The line.
     */
    public JsonEntry(String[] line) {
        this.line = line;
    }

    /**
     * Get the create time.
     * 
     * @return The create time.
     */
    public Date getDate() {
        return new Date(Long.parseLong(line[DATE]));
    }
    
    /**
     * Get the name of the context class.
     * 
     * @return The context class name.
     */
    public String getContext() {
        return line[CONTEXT];
    }
    
    /**
     * Get the notice code name.
     * 
     * @return The notice code.
     */
    public String getCode() {
        return line[CODE];
    }
    
    /**
     * Get the debugging level.
     * 
     * @return The debugging level.
     */
    public String getLevel() {
        return line[LEVEL];
    }
    
    /**
     * Get the thread id.
     * @return The thread id.
     */
    public int getThreadId() {
        return Integer.parseInt(line[THREAD_ID]);
    }
    
    /**
     * Get the JSON string.
     * 
     * @return The JSON string.
     */
    public String getJsonString() { 
        return line[JSON];
    }

    /**
     * Get the JSON string as JSON.
     * 
     * @return The JSON string.
     * @throws ParseException
     *             For any JSON parsing error.
     */
    public JSONObject getJson() throws ParseException { 
        return (JSONObject) new JSONParser().parse(line[JSON]);
    }
}
