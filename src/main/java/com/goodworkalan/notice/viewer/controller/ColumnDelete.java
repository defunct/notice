package com.goodworkalan.notice.viewer.controller;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.json.simple.JSONValue;

import com.goodworkalan.notice.viewer.model.Column;
import com.goodworkalan.paste.controller.Actors;
import com.goodworkalan.paste.infuse.InfusionActor;
import com.goodworkalan.paste.stream.Output;
import com.google.inject.Inject;

@Actors(InfusionActor.class)
public class ColumnDelete {
    private final EntityManager em;
    
    private Column column;
    
    @Inject
    public ColumnDelete(EntityManager em) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        this.em = em;
    }
    
    public Column getColumn() {
        return column;
    }
    
    public void setColumn(Column column) {
        this.column = column;
    }
    
    @Output(contentType = "application/json")
    public String remove() {
        em.remove(column);
        em.flush();
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("status", "success");
        return JSONValue.toJSONString(response);
    }
}
