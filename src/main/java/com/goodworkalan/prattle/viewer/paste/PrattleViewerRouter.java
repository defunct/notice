package com.goodworkalan.prattle.viewer.paste;

import com.goodworkalan.paste.Connector;
import com.goodworkalan.paste.Router;
import com.goodworkalan.paste.Startup;
import com.goodworkalan.paste.forward.Forward;
import com.goodworkalan.paste.paths.ControllerClassName;
import com.goodworkalan.paste.redirect.Redirect;
import com.goodworkalan.paste.redirect.Redirection;
import com.goodworkalan.prattle.viewer.controller.FreemarkerController;
import com.goodworkalan.prattle.viewer.controller.LandingView;
import com.goodworkalan.prattle.viewer.controller.MigrateSchema;

/**
 * Routes URL paths to controllers and specifies renderers for controllers.
 * 
 * @author Alan Gutierrez
 */
public class PrattleViewerRouter implements Router {
    /**
     * Route the applications URL paths to their controllers and specify
     * renderers for controllers.
     * 
     * @param connector
     *            The URL to controller connector.
     */
    public void connect(Connector connector) {
        connector
            .react()
                .to(Startup.class)
                .with(MigrateSchema.class)
            .connect()
                .path("").or().path("/").to(LandingView.class).end()
                .end()
            .render()
                .exception(Redirection.class).with(Redirect.class).end()
            .render()
                .controller(FreemarkerController.class)
                .with(Forward.class).format("/freemarker.directory/%s.ftl", ControllerClassName.class)
                .end();
    }
}
