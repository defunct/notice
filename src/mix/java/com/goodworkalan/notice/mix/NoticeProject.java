package com.goodworkalan.notice.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

public class NoticeProject extends ProjectModule {
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.notice/notice/0.1")
                .main()
                    .depends()
                        .include("com.github.bigeasy.verbiage/verbiage/0.+1")
                        .include("com.github.bigeasy.diffuse/diffuse/0.+1")
                        .include("com.github.bigeasy.reflective/reflective/0.+1")
                        .include("com.github.bigeasy.madlib/madlib/0.+1")
                        .include("org.slf4j/slf4j-api/1.4.2")
                        .end()
                    .end()
                .test()
                    .depends()
                        .include("org.slf4j/slf4j-log4j12/1.4.2")
                        .include("log4j/log4j/1.2.14")
                        .include("org.testng/testng-jdk15/5.10")
                        .include("org.mockito/mockito-core/1.6")
                        .end()
                    .end()
                .end()
            .end();
    }
}
