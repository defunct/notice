package com.goodworkalan.notice.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.cookbook.JavaProject;

/**
 * Builds the project definition for Spawn.
 *
 * @author Alan Gutierrez
 */
public class NoticeProject implements ProjectModule {
    /**
     * Build the project definition for Spawn.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.notice/notice/0.1.0.1")
                .depends()
                    .production("com.github.bigeasy.retry/retry/0.+1")
                    .production("com.github.bigeasy.verbiage/verbiage/0.+1")
                    .production("com.github.bigeasy.diffuse/diffuse/0.+1")
                    .production("com.github.bigeasy.reflective/reflective/0.+1")
                    .production("com.github.bigeasy.madlib/madlib/0.+1")
                    .production("org.slf4j/slf4j-api/1.4.2")
                    .development("org.slf4j/slf4j-log4j12/1.4.2")
                    .development("log4j/log4j/1.2.14")
                    .development("org.testng/testng-jdk15/5.10")
                    .development("org.mockito/mockito-core/1.6")
                    .end()
                .end()
            .end();
    }
}
