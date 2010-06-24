package com.goodworkalan.notice;

import java.util.Set;

import com.goodworkalan.diffuse.ObjectDiffuser;
import com.goodworkalan.diffuse.Diffuser;

// TODO Document.
class StopWatchDiffuser implements ObjectDiffuser {
    // TODO Document.
    public Object diffuse(Diffuser diffuser, Object object, StringBuilder path, Set<String> includes) {
        StopWatch stopWatch = (StopWatch) object;
        stopWatch.stop();
        return stopWatch.getDuration();
    }

    // TODO Document.
    public boolean isContainer() {
        return false;
    }
}
