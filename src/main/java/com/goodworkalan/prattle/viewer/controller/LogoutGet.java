package com.goodworkalan.prattle.viewer.controller;

import com.goodworkalan.paste.Routes;
import com.goodworkalan.paste.redirect.Redirection;
import com.google.inject.Inject;

public class LogoutGet {
    @Inject
    public LogoutGet(Visit visit, Routes routes) {
        visit.setAuthenticated(false);
        throw new Redirection(routes.path(LoginView.class));
    }
}
