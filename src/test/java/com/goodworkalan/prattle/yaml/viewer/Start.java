package com.goodworkalan.prattle.yaml.viewer;

import java.io.IOException;

import javax.naming.NamingException;

import org.eclipse.jetty.plus.jndi.EnvEntry;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Start
{
    @Option(name = "-P", usage = "Port.")
    private int port = 8080;

    public static void main(String[] args) throws NamingException, IOException
    {
        Start start = new Start();
        CmdLineParser parser = new CmdLineParser(start);
        try
        {
            parser.parseArgument(args);
        }
        catch (CmdLineException e)
        {
            System.err.println(e.getMessage());
            System.err.println("java -jar myprogram.jar [options...] arguments...");
            parser.printUsage(System.err);
            return;
        }
        start.run();
    }
    
    private void run() throws NamingException, IOException
    {
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
                "org.eclipse.jetty.plus.webapp.EnvConfiguration",
                "org.eclipse.jetty.plus.webapp.Configuration",
                "org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
                "org.eclipse.jetty.webapp.TagLibConfiguration"
        });
        
        server.setHandler(webAppContext);

        webAppContext.getWebInf();
        
        new EnvEntry("21peeps/analyzing", Boolean.FALSE, true);
        new EnvEntry("21peeps/application/name", "Local Host", true);

        try
        {
            System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
            server.start();
            while (System.in.available() == 0)
            {
                Thread.sleep(5000);
            }
            server.stop();
            server.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(100);
        }
    }
}
