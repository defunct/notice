package com.goodworkalan.prattle.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.goodworkalan.madlib.VariableProperties;

public class Rotator {
    private DateFormat fileFormat;
    
    private Date rotateAfter;
    
    private RotateType rotateType;
    
    public Rotator(VariableProperties configuration, String prefix) {
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
    
    public boolean shouldRotate(Date date) {
        return date.after(rotateAfter);
    }
    
    public String getSuffix() {
        Calendar calendar = getRotation(getCalendar(new Date()));
        String suffix = fileFormat.format(calendar.getTime());
        rotateAfter = getNextRotation(calendar);
        return suffix;
    }
}
