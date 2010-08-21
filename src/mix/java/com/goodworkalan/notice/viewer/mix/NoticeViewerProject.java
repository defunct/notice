package com.goodworkalan.minimal.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

/**
 * Builds the project definition for Notice Viewer.
 *
 * @author Alan Gutierrez
 */
public class NoticeViewerProject implements ProjectModule {
    /**
     * Build the project definition for Notice Viewer.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.notice/notice-viewer/0.1")
                .depends()
                    .production("com.github.bigeasy.notice/notice-json/0.+1")
                    .production("com.github.bigeasy.paste/paste/0.+2.1")
                    .production("com.github.bigeasy.addendum/addendum-jpa/0.+1")
                    .production("com.github.bigeasy.addendum/addendum-mysql/0.+1")
                    .production("log4j/log4j/1.2.13")
                    .production("org.slf4j/slf4j-api/1.4.2")
                    .production("org.slf4j/slf4j-log4j12/1.4.2")
                    .production("mysql/mysql-connector-java/5.1.10")
                    .production("c3p0/c3p0/0.9.1.2")
                    .production("com.google.inject/guice/2.0")
                    .production("com.google.inject/guice-multibindings/2.0")
                    .production("org.freemarker/freemarker/2.3.13")
                    .production("org.hibernate/hibernate-core/3.3.1.GA")
                    .production("org.hibernate/hibernate-annotations/3.4.0.GA")
                    .production("org.hibernate/hibernate-entitymanager/3.4.0.GA")
                    .production("com.mallardsoft/tuple/2.0")
                    .production("javax.servlet/servlet-api/2.5")
                    .development("org.eclipse.jetty/jetty-xml/7.0.0.RC3")
                    .development("org.eclipse.jetty/jetty-webapp/7.0.0.RC3")
                    .development("org.eclipse.jetty/jetty-plus/7.0.0.RC3")
                    .development("org.eclipse.jetty/jetty-jndi/7.0.0.RC3")
                    .development("org.testng/testng-jdk15/5.10")
                    .development("args4j/args4j/2.0.8")
                    .development("org.mockito/mockito-core/1.6")
                    .end()
                .end()
            .end();
    }
}
