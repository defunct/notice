package com.goodworkalan.cassandra.mix;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

public class NoticeSmtpProject extends ProjectModule {
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces(new Artifact("com.goodworkalan/notice-smtp/0.1"))
                .main()
                    .depends()
                        .artifact(new Artifact("com.goodworkalan/notice/0.1"))
                        .artifact(new Artifact("com.goodworkalan/waste/0.7"))
                        .end()
                    .end()
                .test()
                    .depends()
                        .artifact(new Artifact("org.testng/testng/5.10/jdk15"))
                        .end()
                    .end()
                .end()
            .end();
    }
}
