package com.goodworkalan.prattle.viewer.paste;

import com.goodworkalan.paste.Connector;
import com.goodworkalan.paste.Router;
import com.goodworkalan.paste.Startup;
import com.goodworkalan.paste.forward.Forward;
import com.goodworkalan.paste.paths.ControllerClassName;
import com.goodworkalan.paste.redirect.Redirect;
import com.goodworkalan.paste.redirect.Redirection;
import com.goodworkalan.paste.stream.Stream;
import com.goodworkalan.prattle.viewer.controller.ColumnAdd;
import com.goodworkalan.prattle.viewer.controller.ColumnSave;
import com.goodworkalan.prattle.viewer.controller.ColumnsGet;
import com.goodworkalan.prattle.viewer.controller.EntryGet;
import com.goodworkalan.prattle.viewer.controller.FilterAdd;
import com.goodworkalan.prattle.viewer.controller.FreemarkerController;
import com.goodworkalan.prattle.viewer.controller.GridView;
import com.goodworkalan.prattle.viewer.controller.LandingView;
import com.goodworkalan.prattle.viewer.controller.LoginView;
import com.goodworkalan.prattle.viewer.controller.LogoutGet;
import com.goodworkalan.prattle.viewer.controller.MigrateSchema;
import com.goodworkalan.prattle.viewer.controller.VerifyLogin;

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
                .path("")
                    .or().path("/")
                    .or().path("/(base ^(?!^login|logout$)[^.]+$)/(rest)*")
                    .to(VerifyLogin.class)
                    .end()
                .end()
            .connect()
                .path("").or().path("/").to(LandingView.class).end()
                .path("/login").to(LoginView.class).end()
                .path("/logout").to(LogoutGet.class).end()
                .path("/entries/(id)").to(EntryGet.class).end()
                .path("/grid/(grid[id] [0-9]+)").to(GridView.class).end()
                .path("/columns/(grid[id] [0-9]+)").to(ColumnsGet.class).end()
                .path("/columns/save").to(ColumnSave.class).end()
                .path("/columns/add").to(ColumnAdd.class).end()
                .path("/filters/add").to(FilterAdd.class).end()
                .end()
            .render()
                .exception(Redirection.class).priority(1).with(Redirect.class).end()
            .render()
                .controller(FreemarkerController.class)
                .with(Forward.class).format("/freemarker.directory/%s.ftl", ControllerClassName.class)
                .end()
            .render()
                .controller(EntryGet.class)
                    .with(Stream.class)
                    .contentType("text/plain")
                .end()
            .render()
                .controller(ColumnsGet.class)
                .controller(ColumnSave.class)
                .controller(ColumnAdd.class)
                .controller(FilterAdd.class)
                    .with(Stream.class)
                    .contentType("application/json")
                .end();
    }
}
