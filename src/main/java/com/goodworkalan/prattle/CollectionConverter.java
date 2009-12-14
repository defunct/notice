package com.goodworkalan.prattle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

class CollectionConverter implements Converter {
    public Object convert(Object object, StringBuilder path, Set<String> includes) {
        path.append("*.");
        int index = path.length();
        Collection<?> original = (Collection<?>) object;
        List<Object> copy = new ArrayList<Object>();
        for (Object item : original) {
            if (item == null) {
                copy.add(item);
            } else {
                copy.add(Entry.getConverter(item.getClass()).convert(item, path, includes));
                path.setLength(index);
            }
        }
        return copy;
    }
    
    public boolean isContainer() {
        return true;
    }
}
