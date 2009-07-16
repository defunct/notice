package com.goodworkalan.prattle.api;

import java.net.URI;

import org.testng.annotations.Test;

import com.goodworkalan.prattle.Logger;
import com.goodworkalan.prattle.LoggerFactory;
import com.goodworkalan.prattle.Prattle;

public class LoggerTest
{
    @Test
    public void test()
    {
        Logger logger = LoggerFactory.getLogger(LoggerTest.class);
        logger.debug("Test.").send();
        logger.info("Hello, World!").send();
        logger.info("Create a brand new foo.")
              .bean("greeting", "Hello, World!")
              .bean("url", URI.create("http://blogometer.com"))
              .bean("person", new Person("Alan", "Guiterrez"))
              .map("map")
                  .list("list").add("A").add("B").add("C").end()
                  .map("map").put("one", 1).put("two", 2).end()
                  .put("bean", new Person("Geroge", "Washington"))
                  .end()
              .list("list")
                  .add("one")
                  .add("two")
                  .list().add("two.one").add("two.two").end()
                  .add("three")
                  .end()
              .send();
        Prattle.getInstance().shutdown();
    }
}
