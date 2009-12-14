package com.goodworkalan.minimal.controller;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.google.inject.Singleton;

/**
 * Looks up the JNDI configuration values and exposes them as bean properties.
 * 
 * @author Alan Gutierrez
 */
@Singleton
public class NamedValues {
    /**
     * The key indicating whether he application should collect analytics 3rd
     * party analytics tools.
     */
    public static final String JNDI_ANALYZING_NAME = "java:comp/env/minimal/analyzing";

    /** Add analytics if true. */
    private final boolean analyzing;

    /**
     * Lookup all of the JNDI bound objects.
     * 
     * @throws NamingException
     *             For any JNDI error.
     */
    public NamedValues() throws NamingException {
        InitialContext context = new InitialContext();

        this.analyzing = (Boolean) context.lookup(JNDI_ANALYZING_NAME);
    }

    /**
     * Return true if the application should collect analytics using 3rd party
     * analytics tools.
     * 
     * @return True if the application should collect analytics.
     */
    public boolean isAnalyzing() {
        return analyzing;
    }
}