package com.goodworkalan.prattle.yaml.viewer.paste;

import com.goodworkalan.paste.Connector;
import com.goodworkalan.paste.Router;
import com.goodworkalan.paste.forward.Forward;
import com.goodworkalan.paste.paths.FormatArguments;
import com.goodworkalan.prattle.yaml.viewer.controller.LandingView;

public class PrattleYamlViewRouter implements Router
{
    public void connect(Connector connector)
    {
        connector
            .connect()
                .path("").or().path("/").to(LandingView.class).end()
                .end();
        connector
            .view()
                .controller(LandingView.class)
                .with(Forward.class).format("/freemarker.directory/controllers/%s.ftl", FormatArguments.CONTROLLER_CLASS_NAME)
                .end();
    }
}
