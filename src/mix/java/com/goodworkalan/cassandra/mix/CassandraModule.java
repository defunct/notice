package com.goodworkalan.cassandra.mix;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.BasicJavaModule;

public class CassandraModule extends BasicJavaModule {
    public CassandraModule() {
        super(new Artifact("com.goodworkalan", "cassandra", "0.7"));
        addTestDependency(new Artifact("org.testng", "testng", "5.10"));
    }
}
