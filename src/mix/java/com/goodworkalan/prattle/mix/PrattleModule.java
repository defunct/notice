package com.goodworkalan.go.go.mix;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.BasicJavaModule;

public class PrattleModule extends BasicJavaModule {
    public PrattleModule() {
        super(new Artifact("com.goodworkalan", "prattle", "0.1"));
        addDependency(new Artifact("org.slf4j", "slf4j-api", "1.4.2"));
        addTestDependency(new Artifact("org.slf4j", "slf4j-log4j12", "1.4.2"));
        addTestDependency(new Artifact("org.testng", "testng", "5.10"));
    }
}
