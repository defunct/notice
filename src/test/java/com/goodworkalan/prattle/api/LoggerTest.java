package com.goodworkalan.prattle.api;

import com.goodworkalan.prattle.Logger;
import com.goodworkalan.prattle.LoggerFactory;

public class LoggerTest
{
    public void test()
    {
        Logger logger = LoggerFactory.getLogger(LoggerTest.class);
        logger.debug("Test.").send();
        logger.info("Hello, World!").send();
        logger.info("Create a brand new foo.")
              .dump(new Object()).end()
              .send();
    }
}
