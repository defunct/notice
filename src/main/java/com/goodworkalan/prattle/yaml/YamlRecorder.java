package com.goodworkalan.prattle.yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.goodworkalan.madlib.VariableProperties;
import com.goodworkalan.prattle.PrattleException;
import com.goodworkalan.prattle.Recorder;

/**
 * Writes a Prattle message to a rotating YAML log file.
 * 
 * @author Alan Gutierrez
 */
public class YamlRecorder implements Recorder
{
    /** The YAML serailizer and parser. */
    private Yaml yaml;
    
    /** A destination writer. */
    private Writer writer;
    
    /**
     * Default constructor.
     */
    public YamlRecorder()
    {
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
    public void initialize(String prefix, VariableProperties configuration)
    {
        String file = configuration.getProperty(prefix + "file", null);
        if (file == null)
        {
            throw new PrattleException(0);
        }
        DumperOptions options = new DumperOptions();
        options.setExplicitStart(true);
        options.setExplicitEnd(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(new Dumper(new PrattleRepresenter(), options));
        try
        {
            writer = new FileWriter(file, true);
        }
        catch (IOException e)
        {
            throw new PrattleException(0, e);
        }
    }

    /**
     * Write the given Prattle message map to a log file.
     * 
     * @param map
     *            The Prattle message as a map.
     */
    public void record(Map<String, Object> map)
    {
        yaml.dump(map, writer);
    }
    
    /**
     * Close the recorder.
     */
    public void close()
    {
        try
        {
            writer.close();
        }
        catch (IOException e)
        {
        }
    }
}
