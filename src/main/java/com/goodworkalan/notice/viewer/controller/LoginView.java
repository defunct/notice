package com.goodworkalan.notice.viewer.controller;

import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.paste.Actors;
import com.goodworkalan.paste.infuse.InfusionActor;
import com.goodworkalan.paste.invoke.Invoke;
import com.goodworkalan.paste.invoke.InvokeActor;
import com.goodworkalan.paste.redirect.Redirection;
import com.google.inject.Inject;

@Actors({InfusionActor.class, InvokeActor.class})
public class LoginView extends FreemarkerController {
    private String password;
    
    private final Visit visit;
    
    @Inject
    public LoginView(NamedValues namedValues, HttpServletRequest request, Visit visit) {
        super(namedValues, request);
        this.visit = visit;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Invoke
    public void checkPassword() {
        if (request.getMethod().equals("POST")) {
            if (namedValues.getPassword().equals(password)) {
                visit.setAuthenticated(true);
                String landing = visit.getLanding();
                if (landing == null) {
                    landing = "/";
                }
                throw new Redirection(landing);
            }
        }
    }
}
