package com.goodworkalan.notice.yaml.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.cookbook.JavaProject;

/**
 * Builds the project definition for Notice JSON.
 *
 * @author Alan Gutierrez
 */
public class NoticeYamlProject implements ProjectModule {
    /**
     * Build the project definition for Notice JSON.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.notice/notice-yaml/0.1")
                .depends()
                    .production("com.github.bigeasy.notice/notice-rotate/0.+1")
                    .production("SnakeYAML/SnakeYAML/1.3")
                    .development("org.slf4j/slf4j-log4j12/1.4.2")
                    .development("log4j/log4j/1.2.14")
                    .development("org.testng/testng-jdk15/5.10")
                    .development("org.mockito/mockito-core/1.6")
                    .end()
                .end()
            .end();
    }
}
