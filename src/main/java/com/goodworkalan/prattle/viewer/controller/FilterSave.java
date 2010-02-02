package com.goodworkalan.prattle.viewer.controller;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.json.simple.JSONValue;

import com.goodworkalan.notice.Notice;
import com.goodworkalan.paste.Actors;
import com.goodworkalan.paste.infuse.InfusionActor;
import com.goodworkalan.paste.stream.Output;
import com.goodworkalan.prattle.viewer.model.Filter;
import com.google.inject.Inject;

@Actors(InfusionActor.class)
public class FilterSave {
    private final EntityManager em;
    
    private Filter filter;
    
    @Inject
    public FilterSave(EntityManager em) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        this.em = em;
    }
    
    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
    
    @Output(contentType = "application/json")
    public String text() {
        em.flush();
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("status", "success");
        response.put("filter", Notice.flatten(filter));
        return JSONValue.toJSONString(response);
    }
}
