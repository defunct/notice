package com.goodworkalan.prattle;

import java.util.Set;

public class ClassConverter implements Converter {
    public Object convert(Object object, StringBuilder path, Set<String> includes) {
        return ((Class<?>) object).getCanonicalName();
    }
    
    public boolean isContainer() {
        return false;
    }
}
