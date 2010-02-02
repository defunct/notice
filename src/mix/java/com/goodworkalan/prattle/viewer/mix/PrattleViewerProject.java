package com.goodworkalan.minimal.mix;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.go.go.Include;
import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

public class PrattleViewerProject extends ProjectModule {
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces(new Artifact("com.goodworkalan/prattle-viewer/0.1.1"))
                .main()
                    .depends()
                        .artifact(new Artifact("com.goodworkalan/paste/0.2"))
                        .artifact(new Artifact("com.goodworkalan/prattle/0.1.1"))
                        .artifact(new Artifact("com.goodworkalan/prattle-json/0.1.1"))
                        .artifact(new Artifact("com.goodworkalan/infuse-jpa/0.1"))
                        .artifact(new Artifact("com.goodworkalan/infuse-guice/0.1"))
                        .artifact(new Artifact("com.goodworkalan/addendum-jpa/0.1"))
                        .artifact(new Artifact("com.goodworkalan/addendum-mysql/0.1"))
                        .artifact(new Artifact("log4j/log4j/1.2.13"))
                        .artifact(new Artifact("org.slf4j/slf4j-api/1.4.2"))
                        .artifact(new Artifact("org.slf4j/slf4j-log4j12/1.4.2"))
                        .artifact(new Artifact("mysql/mysql-connector-java/5.1.10"))
                        .artifact(new Artifact("c3p0/c3p0/0.9.1.2"))
                        .artifact(new Artifact("com.google.inject/guice/2.0"))
                        .artifact(new Artifact("com.google.inject/guice-multibindings/2.0"))
                        .artifact(new Artifact("org.freemarker/freemarker/2.3.13"))
                        .artifact(new Artifact("org.hibernate/hibernate-core/3.3.1.GA"))
                        .artifact(new Artifact("org.hibernate/hibernate-annotations/3.4.0.GA"))
                        .artifact(new Artifact("org.hibernate/hibernate-entitymanager/3.4.0.GA"))
                        .artifact(new Artifact("com.mallardsoft/tuple/2.0"))
                        .artifact(new Artifact("javax.servlet/servlet-api/2.5"))
                        .end()
                    .end()
                .test()
                    .depends()
                        .artifact(new Artifact("org.eclipse.jetty/jetty-xml/7.0.0.RC3"))
                        .artifact(new Artifact("org.eclipse.jetty/jetty-webapp/7.0.0.RC3"))
                        .artifact(new Artifact("org.eclipse.jetty/jetty-plus/7.0.0.RC3"))
                        .artifact(new Artifact("org.eclipse.jetty/jetty-jndi/7.0.0.RC3"))
                        .artifact(new Artifact("org.testng/testng/5.10/jdk15"))
                        .artifact(new Artifact("args4j/args4j/2.0.8"))
                        .artifact(new Artifact("org.mockito/mockito-core/1.6"))
                        .end()
                    .end()
                .end()
            .end();
    }
}
