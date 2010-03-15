package com.goodworkalan.notice.exception;

import java.util.Map;
import java.util.Set;

import com.goodworkalan.diffuse.Diffuse;
import com.goodworkalan.diffuse.ThrowableConverter;

public class NoticeExceptionConverter extends ThrowableConverter {
    /**
     * Add the variables to the exception.
     */
    @Override
    protected Map<String, Object> modifiable(Object object, StringBuilder path, Set<String> includes) {
        Map<String, Object> map = super.modifiable(object, path, includes);
        NoticeException e = (NoticeException) object;
        map.put("vars", Diffuse.flatten(e.get("vars"), true));
        return map;
    }
    
    public boolean isContainer() {
        return false;
    }
}
