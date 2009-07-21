package com.goodworkalan.prattle.yaml;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;

import com.goodworkalan.prattle.model.Employee;

public class PrattleRepresenterTest
{
    @Test
    public void test() throws IOException
    {
        Yaml yaml = new Yaml();
        
        Employee person = new Employee("Alan", "Gutierrez", 9);
        assertEquals(person, yaml.load(yaml.dump(person)));
        assertEquals(person, yaml.load(yaml.dump(person)));
    }
}
