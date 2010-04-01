package com.goodworkalan.notice.viewer.controller;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.json.simple.JSONValue;

import com.goodworkalan.diffuse.Diffuser;
import com.goodworkalan.notice.viewer.model.Filter;
import com.goodworkalan.paste.Actors;
import com.goodworkalan.paste.infuse.InfusionActor;
import com.goodworkalan.paste.stream.Output;
import com.google.inject.Inject;

@Actors(InfusionActor.class)
public class FilterAdd {
    private Filter filter;
    
    private final EntityManager em;
    
    @Inject
    public FilterAdd(EntityManager em) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
        this.em = em;
    }
    
    public Filter getFilter() {
        return filter;
    }
    
    public void setFilter(Filter column) {
        this.filter = column;
    }
    
    @Output(contentType = "application/json")
    public String add() {
        em.persist(filter);
        em.flush();
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("status", "success");
        response.put("filter", new Diffuser().flatten(filter));
        return JSONValue.toJSONString(response);
    }
}
