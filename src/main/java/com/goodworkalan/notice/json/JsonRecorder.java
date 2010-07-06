package com.goodworkalan.notice.json;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.json.simple.JSONValue;

import com.goodworkalan.madlib.VariableProperties;
import com.goodworkalan.notice.NoticeException;
import com.goodworkalan.notice.Recorder;
import com.goodworkalan.notice.rotate.Rotator;

/**
 * Implementation of a Prattle recorder the emits a single log file line of JSON
 * encoded data, with an optional record string prefix for easy scanning of the
 * log.
 * <p>
 * Exceptions raised by the recorder are generally unrecoverable, indicating
 * block device hardware failures or full block devices. Reparation of block
 * devices is beyond the scope of this class.
 * 
 * @author Alan Gutierrez
 */
public class JsonRecorder implements Recorder {
    /** The log file rotator. */
    private Rotator rotator;

    /** The log file writer. */
    private BufferedWriter writer;

    /** The log file. */
    private String file;

    /** Create a JSON recorder. */
    public JsonRecorder() {
    }

    // TODO Document.
    public void initialize(String prefix, VariableProperties configuration) {
        file = configuration.getProperty(prefix + "file", null);
        if (file == null) {
            throw new NoticeException(0);
        }
        rotator = new Rotator(configuration, prefix);
        try {
            writer = new BufferedWriter(new FileWriter(file
                    + rotator.getSuffix(), true));
        } catch (IOException e) {
            throw new NoticeException(0, e);
        }
    }

    /**
     * Record the given diffused object tree as a JSON formatted line in a
     * rotated log file.
     * 
     * @param map
     *            The diffused object tree.
     */
    public void record(Map<String, Object> map) {
        Date date = new Date((Long) map.get("date"));

        if (rotator.shouldRotate(date)) {
            try {
                writer.close();
            } catch (IOException e) {
                throw new NoticeException(0, e);
            }

            try {
                writer = new BufferedWriter(new FileWriter(file
                        + rotator.getSuffix(), true));
            } catch (IOException e) {
                throw new NoticeException(0, e);
            }
        }

        StringBuilder builder = new StringBuilder();

        builder.append(map.get("date")).append(" ");

        builder.append(map.get("context")).append(" ");
        builder.append(map.get("code")).append(" ");
        builder.append(map.get("level")).append(" ");
        builder.append(map.get("threadId")).append(" ");

        builder.append(JSONValue.toJSONString(map)).append("\n");

        try {
            writer.append(builder);
        } catch (IOException e) {
            throw new NoticeException(0, e);
        }
    }

    /**
     * Close the underlying log file.
     * 
     * @exception NoticeException
     *                If the file cannot be flushed.
     */
    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            throw new NoticeException(0, e);
        }
    }

    /**
     * Close the underlying log file.
     * 
     * @exception NoticeException
     *                If the file cannot be closed.
     */
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new NoticeException(0, e);
        }
    }
}
