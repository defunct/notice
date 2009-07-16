package com.goodworkalan.prattle;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class YamlRecorder implements Recorder
{
    private Yaml yaml;
    
    private Writer writer;
    
    public YamlRecorder()
    {
    }
    
    public void initialize(String prefix, Properties properties)
    {
        String file = properties.getProperty(prefix + "file", null);
        if (file == null)
        {
            throw new PrattleException();
        }
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(options);
        try
        {
            writer = new FileWriter(file);
            writer.write(yaml.dump(new Message("Starting YAML recorder.", Level.INFO, null).toMap()));
        }
        catch (IOException e)
        {
            throw new PrattleException();
        }
    }
    
    public void record(Map<String, Object> map)
    {
        try
        {
            writer.write("---\n");
            writer.write(yaml.dump(map));
            writer.flush();
        }
        catch (IOException e)
        {
        }
    }
    
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
