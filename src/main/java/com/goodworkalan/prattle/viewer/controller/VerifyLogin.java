package com.goodworkalan.prattle.viewer.controller;

import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.paste.Actors;
import com.goodworkalan.paste.infuse.InfusionActor;
import com.goodworkalan.paste.invoke.Invoke;
import com.goodworkalan.paste.invoke.InvokeActor;
import com.goodworkalan.paste.redirect.Redirection;
import com.google.inject.Inject;

@Actors({InfusionActor.class, InvokeActor.class})
public class VerifyLogin {
    private final HttpServletRequest request;
    
    private final Visit visit;
    
    private String base;
    
    @Inject
    public VerifyLogin(HttpServletRequest request, Visit visit) {
        this.request = request;
        this.visit = visit;
    }
    
    public void setBase(String any) {
        this.base = any;
    }
    
    private String getLanding() {
        String q = request.getQueryString();
        q = q == null ? "" : "?" + q;
        return request.getRequestURI() + q;
    }

    @Invoke
    public void verify() {
        if (!"login".equals(base) && !visit.isAuthenticated()) {
            visit.setLanding(getLanding());
            throw new Redirection("/login");
        }
    }
}
