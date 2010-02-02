package com.goodworkalan.notice.yaml;

import java.io.IOException;

import org.testng.annotations.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

public class YamlErrorTest
{
    @Test
    public void testException()
    {
        try
        {
            try
            {
                throw new IOException("I/O error.");
            }
            catch (IOException e)
            {
                throw new RuntimeException("Caught I/O error.", e);
            }
        }
        catch (RuntimeException e)
        {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(FlowStyle.BLOCK);
            Yaml yaml = new Yaml();
            System.out.println(yaml.dump(e));
        }
    }
}
