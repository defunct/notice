package com.goodworkalan.notice;

import java.net.URI;

import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.goodworkalan.notice.NoticeFactory;
import com.goodworkalan.notice.Sink;

public class LoggerTest {
    @Test
    public void test() {
        NoticeFactory notices =  new NoticeFactory(LoggerFactory.getLogger(LoggerTest.class));
        notices.debug("test1").send();
        notices.info("test2").send();
        notices.info("test3")
               .put("greeting", "Hello, World!")
               .put("url", URI.create("http://blogometer.com"))
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
