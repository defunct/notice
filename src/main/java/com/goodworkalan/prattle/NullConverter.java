package com.goodworkalan.prattle;

import java.util.Set;

public class NullConverter implements Converter {
    public final static Converter INSTANCE = new NullConverter();
    
    public Object convert(Object object, StringBuilder path, Set<String> includes) {
        return object;
    }
    
    public boolean isContainer() {
        return false;
    }
}
