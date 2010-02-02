package com.goodworkalan.notice.viewer.controller;

import java.io.Serializable;

import com.goodworkalan.paste.SessionScoped;

/**
 * An instance of a visit by a single visitor to the website.
 * 
 * @author Alan Gutierrez
 */
@SessionScoped
public class Visit implements Serializable {
    /** The serialized version id. */
    private static final long serialVersionUID = 1L;

    /** Whether or not the visit is authenticated. */
    private boolean authenticated;
    
    /** The desired landing page. */
    private String landing;

    /**
     * Get whether or not the visit is authenticated.
     * 
     * @return True if the visit is authenticated.
     */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * Whether or not the visit is authenticated.
     * 
     * @param authenticated
     *            If true, the visit is authenticated.
     */
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
    
    /**
     * Get the desired landing page.
     * 
     * @return The desired landing page.
     */
    public String getLanding() {
        return landing;
    }

    /**
     * Set the desired landing page.
     * 
     * @param landing
     *            The desired landing page.
     */
    public void setLanding(String landing) {
        this.landing = landing;
    }
}
