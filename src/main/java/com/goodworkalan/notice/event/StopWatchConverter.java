package com.goodworkalan.notice.event;

import java.util.Set;

import com.goodworkalan.diffuse.ObjectDiffuser;
import com.goodworkalan.diffuse.Diffuser;

public class StopWatchConverter implements ObjectDiffuser {
    public Object convert(Diffuser diffuser, Object object, StringBuilder path, Set<String> includes) {
        StopWatch stopWatch = (StopWatch) object;
        stopWatch.stop();
        return stopWatch.getDuration();
    }

    public boolean isContainer() {
        return false;
    }
}
