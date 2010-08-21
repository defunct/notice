package com.goodworkalan.notice.viewer.controller;

import com.goodworkalan.paste.X;
import com.goodworkalan.paste.controller.Redirection;
import com.google.inject.Inject;

/**
 * Logout the visitor and redirect them to the login page.
 *
 * @author Alan Gutierrez
 */
public class LogoutGet {
    /**
     * Logout the given visit and redirect to the login page found using the
     * given routes.
     * 
     * @param visit
     *            The visit.
     * @param routes
     *            The application routes.
     */
    @Inject
    public LogoutGet(Visit visit, X routes) {
        visit.setAuthenticated(false);
        throw new Redirection(routes.path(LoginView.class));
    }
}
