package com.goodworkalan.notice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ArrayConverter implements Converter {
    public final static Converter INSTANCE = new ArrayConverter();

    public Object convert(Object object, StringBuilder path, Set<String> includes) {
        Object[] original = (Object[]) object;
        List<Object> copy = new ArrayList<Object>();
        path.append("*.");
        int index = path.length();
        for (int i = 0, stop = original.length; i < stop; i++) {
            Object value = original[i];
            if (value == null) {
                copy.add(value);
            } else {
                copy.add(Notice.getConverter(value.getClass()).convert(value, path, includes));
                path.setLength(index);
            }
        }
        return Collections.unmodifiableList(copy);
    }

    public boolean isContainer() {
        return true;
    }
}
