package com.goodworkalan.notice.yaml.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

public class NoticeYamlProject extends ProjectModule {
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.notice/notice-yaml/0.1")
                .main()
                    .depends()
                        .include("com.github.bigeasy.notice/notice-rotate/0.+1")
                        .include("SnakeYAML/SnakeYAML/1.3")
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
