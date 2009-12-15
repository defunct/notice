package com.goodworkalan.prattle.viewer;

import java.io.IOException;

import javax.naming.NamingException;

import org.eclipse.jetty.plus.jndi.EnvEntry;
import org.eclipse.jetty.plus.jndi.Resource;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class Start {
    @Option(name = "-c", usage = "MySQL connection url.")
    private String url;

    @Option(name = "-u", usage = "User name.")
    private String user;

    @Option(name = "-p", usage = "Password.")
    private String password;

    @Option(name = "-P", usage = "Port.")
    private int port = 8080;

    public static void main(String[] args) throws NamingException, IOException {
        Start start = new Start();
        CmdLineParser parser = new CmdLineParser(start);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar myprogram.jar [options...] arguments...");
            parser.printUsage(System.err);
            return;
        }
        start.run();
    }

    private void run() throws NamingException, IOException {
        Server server = new Server();
        SocketConnector connector = new SocketConnector();
        // Set some timeout options to make debugging easier.
        connector.setMaxIdleTime(1000 * 60 * 60);
        connector.setSoLingerTime(-1);
        connector.setPort(port);
        server.setConnectors(new Connector[] { connector });

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setServer(server);
        webAppContext.setContextPath("/");
        webAppContext.setWar("src/main/webapp");
        webAppContext.setConfigurationClasses(new String[] {
            "org.eclipse.jetty.webapp.WebInfConfiguration",
            "org.eclipse.jetty.webapp.WebXmlConfiguration",
            "org.eclipse.jetty.plus.webapp.EnvConfiguration",
            "org.eclipse.jetty.plus.webapp.Configuration"
        });
        
        server.setHandler(webAppContext);

        webAppContext.getWebInf();

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(url);
        if (user != null) {
            dataSource.setUser(user);
        }
        if (password != null) {
            dataSource.setPassword(password);
        }
        new Resource("prattle/viewer/datasource", dataSource);

        new EnvEntry("prattle/viewer/analyzing", Boolean.FALSE, true);

        try {
            System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
            server.start();
            while (System.in.available() == 0) {
                Thread.sleep(5000);
            }
            server.stop();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */
