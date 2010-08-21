package com.goodworkalan.notice.viewer.controller;

import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.paste.controller.Actors;
import com.goodworkalan.paste.controller.Redirection;
import com.goodworkalan.paste.infuse.InfusionActor;
import com.goodworkalan.paste.invoke.Invoke;
import com.goodworkalan.paste.invoke.InvokeActor;
import com.google.inject.Inject;

/**
 * Verify that a visitor has authenticated.
 *
 * @author Alan Gutierrez
 */
@Actors({InfusionActor.class, InvokeActor.class})
public class VerifyLogin {
    /** The HTTP request. */
    private final HttpServletRequest request;
    
    /** The visit. */
    private final Visit visit;
    
    /** The first path part in the URL path. */
    public String base;

    /**
     * Create a login verifier with the given HTTP request and visit state.
     * 
     * @param request
     *            The HTTP request.
     * @param visit
     *            The visit state.
     */
    @Inject
    public VerifyLogin(HttpServletRequest request, Visit visit) {
        this.request = request;
        this.visit = visit;
    }

    /**
     * Get the desired landing page with query parameters from the HTTP request.
     * 
     * @return The desired URL.
     */
    private String getLanding() {
        String q = request.getQueryString();
        q = q == null ? "" : "?" + q;
        return request.getRequestURI() + q;
    }

    /**
     * Verify that the visitor is authenticated.
     */
    @Invoke
    public void verify() {
        if (!"login".equals(base) && !visit.isAuthenticated()) {
            visit.setLanding(getLanding());
            throw new Redirection("/login");
        }
    }
}
