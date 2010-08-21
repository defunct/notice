package com.goodworkalan.notice.viewer.controller;

import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.paste.controller.Actors;
import com.goodworkalan.paste.controller.Redirection;
import com.goodworkalan.paste.infuse.InfusionActor;
import com.goodworkalan.paste.invoke.Invoke;
import com.goodworkalan.paste.invoke.InvokeActor;
import com.google.inject.Inject;

/**
 * Display the login page and process the login password.
 *
 * @author Alan Gutierrez
 */
@Actors({InfusionActor.class, InvokeActor.class})
public class LoginView extends FreemarkerController {
    /** The given password. */
    public String password;
    
    /** The visit state. */
    private final Visit visit;

    /**
     * Create a login view with the given named values, the given HTTP request
     * and the given visit state.
     * 
     * @param namedValues
     *            The configuration name value pairs.
     * @param request
     *            The HTTP request.
     * @param visit
     *            The visit.
     */
    @Inject
    public LoginView(NamedValues namedValues, HttpServletRequest request, Visit visit) {
        super(namedValues, request);
        this.visit = visit;
    }

    /**
     * If the password was posted, check it and redirect to the desired landing
     * page if the password is correct, otherwise show the password page.
     */
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
