package com.goodworkalan.prattle.viewer.controller;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.json.simple.JSONValue;

import com.goodworkalan.notice.Notice;
import com.goodworkalan.paste.Actors;
import com.goodworkalan.paste.infuse.InfusionActor;
import com.goodworkalan.paste.stream.Output;
import com.goodworkalan.prattle.viewer.model.Column;
import com.google.inject.Inject;

@Actors(InfusionActor.class)
public class ColumnSave {
    private final EntityManager em;
    
    private Column column;
    
    @Inject
    public ColumnSave(EntityManager em) {
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
    public String save() {
        em.flush();
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("status", "success");
        response.put("column", Notice.flatten(column));
        return JSONValue.toJSONString(response);
    }
}
