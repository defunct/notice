package com.goodworkalan.prattle.yaml.viewer.mix;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

public class PrattleViewerProject extends ProjectModule {
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces(new Artifact("com.goodworkalan/prattle-viewer/0.1"))
                .main()
                    .depends()
                        .artifact(new Artifact("flexjson/flexjson/2.0a6"))
                        .artifact(new Artifact("com.goodworkalan/prattle-yaml/0.1"))
                        .artifact(new Artifact("com.goodworkalan/paste/0.2"))
                        .artifact(new Artifact("com.goodworkalan/infuse-guice/0.1"))
                        .artifact(new Artifact("org.freemarker/freemarker/2.3.13"))
                        .end()
                    .end()
                .test()
                    .depends()
                        .artifact(new Artifact("org.eclipse.jetty/jetty-xml/7.0.0.RC3"))
                        .artifact(new Artifact("org.eclipse.jetty/jetty-webapp/7.0.0.RC3"))
                        .artifact(new Artifact("org.eclipse.jetty/jetty-plus/7.0.0.RC3"))
                        .artifact(new Artifact("org.eclipse.jetty/jetty-jndi/7.0.0.RC3"))
                        .artifact(new Artifact("org.slf4j/slf4j-log4j12/1.4.2"))
                        .artifact(new Artifact("log4j/log4j/1.2.13"))
                        .artifact(new Artifact("org.testng/testng/5.10/jdk15"))
                        .artifact(new Artifact("org.mockito/mockito-core/1.6"))
                        .artifact(new Artifact("args4j/args4j/2.0.8"))
                        .end()
                    .end()
                .end()
            .end();
    }
}
