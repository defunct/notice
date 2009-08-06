package com.goodworkalan.prattle.yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.goodworkalan.prattle.Configuration;
import com.goodworkalan.prattle.PrattleException;
import com.goodworkalan.prattle.Recorder;

public class YamlRecorder implements Recorder
{
    private Yaml yaml;
    
    private Writer writer;
    
    public YamlRecorder()
    {
    }
    
    public void initialize(String prefix, Configuration configuration)
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
            writer = new FileWriter(file);
        }
        catch (IOException e)
        {
            throw new PrattleException(0);
        }
    }
    
    public void record(Map<String, Object> map)
    {
        yaml.dump(map, writer);
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
