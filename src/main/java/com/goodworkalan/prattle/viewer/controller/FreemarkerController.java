package com.goodworkalan.prattle.viewer.controller;

import javax.servlet.http.HttpServletRequest;

/**
 * A base controller for Freemarker templates.
 * 
 * @author Alan Gutierrez
 */
public class FreemarkerController {
    /** The map of JNDI named values. */
    protected final NamedValues namedValues;

    /** The HTTP request. */
    protected final HttpServletRequest request;

    /**
     * Create a new base Freemarker controller with the given map of JNDI named
     * values and the given HTTP request.
     * 
     * @param namedValues
     *            The map of HTTP named values.
     * @param request
     *            The HTTP request.
     */
    public FreemarkerController(NamedValues namedValues, HttpServletRequest request) {
        this.namedValues = namedValues;
        this.request = request;
    }

    /**
     * Get the context path of the application.
     * 
     * @return The application context path.
     */
    public String getApplicationPath() {
        return request.getContextPath();
    }

    /**
     * Return true if the application should collect analytics using 3rd party
     * analytics tools.
     * 
     * @return True if the application should collect analytics.
     */
    public boolean isAnalyzing() {
        return namedValues.isAnalyzing();
    }
}