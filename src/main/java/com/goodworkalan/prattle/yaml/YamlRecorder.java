package com.goodworkalan.prattle.yaml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Map;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.goodworkalan.madlib.VariableProperties;
import com.goodworkalan.prattle.PrattleException;
import com.goodworkalan.prattle.Recorder;
import com.goodworkalan.prattle.Rotator;

/**
 * Writes a Prattle message to a rotating YAML log file.
 * 
 * @author Alan Gutierrez
 */
public class YamlRecorder implements Recorder {
    /** The YAML serializer and parser. */
    private Yaml yaml;
    
    /** The rotation tracker. */
    private Rotator rotator;

    /** A destination writer. */
    private Writer writer;
    
    /** The log file or log directory. */
    private String file;
    
    /**
     * Default constructor.
     */
    public YamlRecorder() {
    }

    /**
     * Initialize the YAML recorder reading the properties with the given prefix
     * from the given configuration.
     * 
     * @param prefix
     *            The property prefix.
     * @param configuration
     *            The configuration.
     */
    public void initialize(String prefix, VariableProperties configuration) {
        file = configuration.getProperty(prefix + "file", null);
        if (file == null) {
            throw new PrattleException(0);
        }
        rotator = new Rotator(configuration, prefix);
        DumperOptions options = new DumperOptions();
        options.setExplicitStart(true);
        options.setExplicitEnd(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(new Dumper(options));
        try {
            writer = new BufferedWriter(new FileWriter(file + rotator.getSuffix(), true));
        } catch (IOException e) {
            throw new PrattleException(0, e);
        }
    }

    /**
     * Write the given Prattle message map to a log file.
     * 
     * @param map
     *            The Prattle message as a map.
     */
    public void record(Map<String, Object> map) {
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
        
        yaml.dump(map, writer);
    }

    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            throw new PrattleException(0, e);
        }
    }
    
    /**
     * Close the recorder.
     */
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
        }
    }
}
