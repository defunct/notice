package com.goodworkalan.prattle.viewer.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.prattle.viewer.model.Log;
import com.google.inject.Inject;

/**
 * The home page view.
 * 
 * @author Alan Gutierrez
 */
public class LandingView extends FreemarkerController {
    private final List<Log> logs;
    /**
     * Construct a landing view using the given JDNI values and the given HTTP
     * request.
     * 
     * @param namedValues
     *            The JNDI specified values.
     * @param request
     *            The HTTP request.
     */
    @Inject
    public LandingView(NamedValues namedValues, HttpServletRequest request, EntityManager em) {
        super(namedValues, request);
        this.logs = Log.toList(em.createQuery("from Log").getResultList());
    }
    
    public List<Log> getLogs() {
        return logs;
    }
}
