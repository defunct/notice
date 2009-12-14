package com.goodworkalan.minimal.controller;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

/**
 * The home page view.
 * 
 * @author Alan Gutierrez
 */
public class LandingView extends FreemarkerController {
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
    public LandingView(NamedValues namedValues, HttpServletRequest request) {
        super(namedValues, request);
    }
}
