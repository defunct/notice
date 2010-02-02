package com.goodworkalan.notice.viewer.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.notice.viewer.model.Grid;
import com.google.inject.Inject;

/**
 * The home page view.
 * 
 * @author Alan Gutierrez
 */
public class LandingView extends FreemarkerController {
    private final List<Grid> logs;
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
        this.logs = Grid.toList(em.createQuery("select grid from Grid as grid where grid.id in (select min(grid.id) from Grid group by grid.log.id)").getResultList());
    }
    
    public List<Grid> getGrids() {
        return logs;
    }
}
