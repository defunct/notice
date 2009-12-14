package com.goodworkalan.prattle;

import java.util.Set;

public interface Converter {
    public Object convert(Object object, StringBuilder path, Set<String> includes);
    
    public boolean isContainer();
}