package com.goodworkalan.notice.rotate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.goodworkalan.madlib.VariableProperties;

/**
 * Rotates a log file in a log file directory at an hourly, daily or weekly
 * interval.
 * 
 * @author Alan Gutierrez
 */
public class Rotator {
    /** The file format. */
    private DateFormat fileFormat;
    
    /** The next rotate time. */
    private Date rotateAfter;
    
    /** The rotation interval. */
    private RotateType rotateType;
    
    /**
     * Create a rotator using the given properties file reading properties
     * prefixed with the given key prefix.
     * 
       * @param properties
     *            The properties map.
     * @param prefix
     *            The property key prefix.
     */
    public Rotator(VariableProperties properties, String prefix) {
        Calendar calendar = getCalendar(new Date());
        String rotate = properties.getProperty(prefix + "rotate", "NEVER").trim().toUpperCase();
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

    /**
     * Create a calendar to do date math that is set to the given date.
     * 
     * @param date
     *            The date.
     * @return A calendar.
     */
    private Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTime(date);
        return calendar;
    }

    /**
     * Round the given calendar off to the nearest hour and to midnight as well,
     * if the interval is not hourly.
     * 
     * @param calendar
     *            The calendar.
     * @return The calendar set to the top of the hour, possibly midnight.
     */
    private Calendar getRotation(Calendar calendar) {
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        if (rotateType == RotateType.DAILY) {
            calendar.set(Calendar.HOUR, 0);
        }
        return calendar;
    }

    /**
     * Calculate the next rotation time by incrementing the time in the given
     * calendar.
     * 
     * @param calendar
     *            The calendar.
     * @return The next rotate date.
     */
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

    /**
     * Determine if the log file should be rotated at the given time.
     * 
     * @param date
     *            The current time.
     * @return True if the log file should be rotated.
     */
    public boolean shouldRotate(Date date) {
        return date.after(rotateAfter);
    }
    
    /**
     * Get the file name suffix which is based on the rotation time.
     * 
     * @return The file name suffix.
     */
    public String getSuffix() {
        Calendar calendar = getRotation(getCalendar(new Date()));
        String suffix = fileFormat.format(calendar.getTime());
        rotateAfter = getNextRotation(calendar);
        return suffix;
    }
}
