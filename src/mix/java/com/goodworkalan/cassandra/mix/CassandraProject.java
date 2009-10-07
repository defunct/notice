package com.goodworkalan.cassandra.mix;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.BasicJavaModule;

public class CassandraProject extends BasicJavaModule {
    public CassandraProject() {
        super(new Artifact("com.goodworkalan", "cassandra", "0.7-SNAPSHOT"));
        addTestDependency(new Artifact("org.testng", "testng", "5.10"));
    }
}
