package com.goodworkalan.noitce.rotate;

import com.goodworkalan.cafe.ProjectModule;
import com.goodworkalan.cafe.builder.Builder;
import com.goodworkalan.cafe.outline.JavaProject;

/**
 * Builds the project definition for Notice Rotate.
 *
 * @author Alan Gutierrez
 */
public class NoticeRotateProject implements ProjectModule {
    /**
     * Build the project definition for Notice Rotate.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.notice/notice-rotate/0.1")
                .depends()
                    .production("com.github.bigeasy.notice/notice/0.+1")
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
