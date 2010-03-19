package com.goodworkalan.notice.event;

import java.util.Set;

import com.goodworkalan.diffuse.Converter;

public class StopWatchConverter implements Converter {
    public Object convert(Object object, StringBuilder path, Set<String> includes) {
        StopWatch stopWatch = (StopWatch) object;
        stopWatch.stop();
        return stopWatch.getDuration();
    }

    public boolean isContainer() {
        return false;
    }
}
