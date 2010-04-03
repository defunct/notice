package com.goodworkalan.notice.exception;

import java.util.Map;
import java.util.Set;

import com.goodworkalan.diffuse.Diffuser;
import com.goodworkalan.diffuse.ThrowableDiffuser;

public class NoticeExceptionDiffuser extends ThrowableDiffuser {
    /**
     * Add the variables to the exception.
     */
    @Override
    protected Map<String, Object> modifiable(Diffuser diffuser, Object object, StringBuilder path, Set<String> includes) {
        Map<String, Object> map = super.modifiable(diffuser, object, path, includes);
        NoticeException e = (NoticeException) object;
        map.put("vars", diffuser.diffuse(e.get("vars"), true));
        return map;
    }
    
    public boolean isContainer() {
        return false;
    }
}
