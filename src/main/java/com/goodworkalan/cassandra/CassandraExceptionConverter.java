package com.goodworkalan.cassandra;

import java.util.Map;
import java.util.Set;

import com.goodworkalan.notice.Notice;
import com.goodworkalan.notice.ThrowableConverter;

public class CassandraExceptionConverter extends ThrowableConverter {
    /**
     * Add the variables to the exception.
     */
    @Override
    protected Map<String, Object> modifiable(Object object, StringBuilder path, Set<String> includes) {
        Map<String, Object> map = super.modifiable(object, path, includes);
        CassandraException e = (CassandraException) object;
        map.put("vars", Notice.flatten(e.get("vars"), true));
        return map;
    }
    
    public boolean isContainer() {
        return false;
    }
}
