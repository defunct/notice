package com.goodworkalan.prattle.api;

import java.net.URI;

import org.testng.annotations.Test;

import com.goodworkalan.prattle.Logger;
import com.goodworkalan.prattle.LoggerFactory;
import com.goodworkalan.prattle.Sink;

public class LoggerTest
{
    @Test
    public void test()
    {
        Logger logger = LoggerFactory.getLogger(LoggerTest.class);
        logger.debug("test1").send();
        logger.info("test2").send();
        logger.info("test3")
              .string("greeting", "Hello, World!")
              .string("url", URI.create("http://blogometer.com"))
              .put("person", new Person("Alan", "Guiterrez"))
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
        Sink.getInstance().shutdown();
    }
}
