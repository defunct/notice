package com.goodworkalan.prattle;

import java.util.Set;

public class ArrayConverter implements Converter {
    public final static Converter INSTANCE = new ArrayConverter();

    public Object convert(Object object, StringBuilder path, Set<String> includes) {
        Object[] original = (Object[]) object;
        Object[] copy = new Object[original.length];
        path.append("*.");
        int index = path.length();
        for (int i = 0, stop = original.length; i < stop; i++) {
            Object value = original[i];
            if (value == null) {
                copy[i] = value;
            } else {
                copy[i] = Entry.getConverter(value.getClass()).convert(value, path, includes);
                path.setLength(index);
            }
        }
        return copy;
    }

    public boolean isContainer() {
        return true;
    }
}
