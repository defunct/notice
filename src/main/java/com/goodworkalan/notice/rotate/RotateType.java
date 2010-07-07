package com.goodworkalan.notice.rotate;

/**
 * An enumerated type for rotation intervals.
 * 
 * @author Alan Gutierrez
 */
public enum RotateType {
    /** Indicates hourly rotation at the top of the hour. */
    HOURLY,
    /** Indicates daily rotation at midnight. */
    DAILY,
    /** Indicates weekly rotation at midnight on Sunday. */
    WEEKLY,
    /** Indicates no rotation. */
    NEVER
}
