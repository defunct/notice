package com.goodworkalan.prattle.json;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.json.simple.JSONValue;

import com.goodworkalan.madlib.VariableProperties;
import com.goodworkalan.prattle.PrattleException;
import com.goodworkalan.prattle.Recorder;

/**
 * Implementation of a Prattle recorder the emits a single log file line
 * of JSON encoded data, with an optional record string prefix for easy
 * scanning of the log. 
 * 
 * @author Alan Gutierrez
 */
public class JsonRecorder implements Recorder {
    /** The date format used to format log entry dates. */
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
    
    private DateFormat fileFormat;

    private BufferedWriter writer;
    
    private Date rotateAfter;
    
    private RotateType rotateType;
    
    private String file;
    
    public JsonRecorder() {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    
    private Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTime(date);
        return calendar;
    }
    
    private Calendar getRotation(Calendar calendar) {
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        if (rotateType == RotateType.DAILY) {
            calendar.set(Calendar.HOUR, 0);
        }
        return calendar;
    }

    private Date getNextRotation(Calendar calendar) {
        switch (rotateType) {
        case HOURLY:
            calendar.add(Calendar.HOUR, 1);
            break;
        default:
            calendar.add(Calendar.DATE, 1);
        }
        return calendar.getTime();
    }

    public void initialize(String prefix, VariableProperties configuration) {
        file = configuration.getProperty(prefix + "file", null);
        if (file == null) {
            throw new PrattleException(0);
        }
        
        Calendar calendar = getCalendar(new Date());
        String rotate = configuration.getProperty(prefix + "rotate", "NEVER").trim().toUpperCase();
        rotateType = RotateType.valueOf(rotate);
        if (rotateType == RotateType.NEVER) {
            rotateAfter = new Date(Long.MAX_VALUE);
            fileFormat = new SimpleDateFormat("");
        } else {
            calendar = getRotation(calendar);
            rotateAfter = getNextRotation(getRotation(getCalendar(calendar.getTime())));
            fileFormat = new SimpleDateFormat("_yyyy_MM_dd_HH_mm");
        }
        try {
            writer = new BufferedWriter(new FileWriter(file + fileFormat.format(calendar.getTime()), true));
        } catch (IOException e) {
            throw new PrattleException(0, e);
        }
    }
    
    public void record(Map<String,Object> map) {
        Date date = new Date((Long) map.get("date"));
        
        if (date.after(rotateAfter)) {
            try {
                writer.close();
            } catch (IOException e) {
                throw new PrattleException(0, e);
            }

            Calendar calendar = getRotation(getCalendar(new Date()));
            try {
                writer = new BufferedWriter(new FileWriter(file + fileFormat.format(calendar.getTime()), true));
            } catch (IOException e) {
                throw new PrattleException(0, e);
            }
            rotateAfter = getNextRotation(calendar);
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
