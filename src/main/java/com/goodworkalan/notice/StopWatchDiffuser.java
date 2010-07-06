package com.goodworkalan.notice;

import java.util.Set;

import com.goodworkalan.diffuse.ObjectDiffuser;
import com.goodworkalan.diffuse.Diffuser;

/**
 * Converts a stop watch into a the duration in milliseconds as an integer.
 *
 * @author Alan Gutierrez
 */
class StopWatchDiffuser implements ObjectDiffuser {
    /**
     * Diffuse the given stop watch object converting it into the duration
     * in milliseconds as an integer.
     * 
    * @param diffuser
     *            The root diffuser to use to diffuse nested objects.
     * @param object
     *            The object to diffuse.
     * @param path
     *            The current object path in the object graph.
     * @param includes
     *            A set of paths to include in the diffusion.
     *            @return The 
     */
    public Object diffuse(Diffuser diffuser, Object object, StringBuilder path, Set<String> includes) {
        StopWatch stopWatch = (StopWatch) object;
        stopWatch.stop();
        return stopWatch.getDuration();
    }

    /**
     * Return false indicating that this is a diffuser for a scalar object.
     * 
     * @return False to indicate that this is a scalar diffuser.
     */
    public boolean isContainer() {
        return false;
    }
}
