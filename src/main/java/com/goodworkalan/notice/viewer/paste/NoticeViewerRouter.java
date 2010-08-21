package com.goodworkalan.notice.viewer.paste;

import com.goodworkalan.notice.viewer.controller.ColumnAdd;
import com.goodworkalan.notice.viewer.controller.ColumnDelete;
import com.goodworkalan.notice.viewer.controller.ColumnSave;
import com.goodworkalan.notice.viewer.controller.ColumnsGet;
import com.goodworkalan.notice.viewer.controller.EntryGet;
import com.goodworkalan.notice.viewer.controller.FilterAdd;
import com.goodworkalan.notice.viewer.controller.FilterDelete;
import com.goodworkalan.notice.viewer.controller.FilterSave;
import com.goodworkalan.notice.viewer.controller.FreemarkerController;
import com.goodworkalan.notice.viewer.controller.GridView;
import com.goodworkalan.notice.viewer.controller.LandingView;
import com.goodworkalan.notice.viewer.controller.LoginView;
import com.goodworkalan.notice.viewer.controller.LogoutGet;
import com.goodworkalan.notice.viewer.controller.MigrateSchema;
import com.goodworkalan.notice.viewer.controller.VerifyLogin;
import com.goodworkalan.paste.connector.Connector;
import com.goodworkalan.paste.connector.Router;
import com.goodworkalan.paste.controller.Redirection;
import com.goodworkalan.paste.controller.Startup;
import com.goodworkalan.paste.forward.Forward;
import com.goodworkalan.paste.paths.ControllerClassName;
import com.goodworkalan.paste.redirect.Redirect;
import com.goodworkalan.paste.stream.Stream;

/**
 * Routes URL paths to controllers and specifies renderers for controllers.
 * 
 * @author Alan Gutierrez
 */
public class NoticeViewerRouter implements Router {
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
                .path("/columns/delete").to(ColumnDelete.class).end()
                .path("/filters/add").to(FilterAdd.class).end()
                .path("/filters/save").to(FilterSave.class).end()
                .path("/filters/delete").to(FilterDelete.class).end()
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
                .controller(ColumnDelete.class)
                .controller(FilterAdd.class)
                .controller(FilterSave.class)
                .controller(FilterDelete.class)
                    .with(Stream.class)
                    .contentType("application/json")
                .end();
    }
}
