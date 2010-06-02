package com.goodworkalan.notice.smtp.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

/**
 * Builds the project definition for Notice SMTP.
 *
 * @author Alan Gutierrez
 */
public class NoticeSmtpProject implements ProjectModule {
    /**
     * Build the project definition for Notice SMTP.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.notice/notice-smtp/0.+1")
                .depends()
                    .production("com.github.bigeasy.notice/notice/0.+1")
                    .production("com.github.bigeasy.waste/waste/0.+7")
                    .development("org.testng/testng-jdk15/5.10")
                    .end()
                .end()
            .end();
    }
}
