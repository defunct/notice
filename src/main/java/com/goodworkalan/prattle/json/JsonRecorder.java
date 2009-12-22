package com.goodworkalan.prattle.json;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.json.simple.JSONValue;

import com.goodworkalan.madlib.VariableProperties;
import com.goodworkalan.prattle.PrattleException;
import com.goodworkalan.prattle.Recorder;
import com.goodworkalan.prattle.Rotator;

/**
 * Implementation of a Prattle recorder the emits a single log file line
 * of JSON encoded data, with an optional record string prefix for easy
 * scanning of the log. 
 * 
 * @author Alan Gutierrez
 */
public class JsonRecorder implements Recorder {
    private Rotator rotator;

    private BufferedWriter writer;

    
    private String file;
    
    public JsonRecorder() {
    }
    


    public void initialize(String prefix, VariableProperties configuration) {
        file = configuration.getProperty(prefix + "file", null);
        if (file == null) {
            throw new PrattleException(0);
        }
        rotator = new Rotator(configuration, prefix);
        try {
            writer = new BufferedWriter(new FileWriter(file + rotator.getSuffix(), true));
        } catch (IOException e) {
            throw new PrattleException(0, e);
        }
    }
    
    public void record(Map<String,Object> map) {
        Date date = new Date((Long) map.get("date"));
        
        if (rotator.shouldRotate(date)) {
            try {
                writer.close();
            } catch (IOException e) {
                throw new PrattleException(0, e);
            }

            try {
                writer = new BufferedWriter(new FileWriter(file + rotator.getSuffix(), true));
            } catch (IOException e) {
                throw new PrattleException(0, e);
            }
        }

        StringBuilder builder = new StringBuilder();

        builder.append(map.get("date")).append(" ");
        
        builder.append(map.get("logger")).append(" ");
        builder.append(map.get("name")).append(" ");
        builder.append(map.get("level")).append(" ");
        builder.append(map.get("threadId")).append(" ");
        
        builder.append(JSONValue.toJSONString(map)).append("\n");
        
        try {
            writer.append(builder);
        } catch (IOException e) {
            throw new PrattleException(0, e);
        }
    }
    
    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            throw new PrattleException(0, e);
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new PrattleException(0, e);
        }
    }
}
