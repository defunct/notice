package com.goodworkalan.prattle;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * Singleton factory that consumes prattle messages.
 *  
 * @author Alan Gutierrez
 */
public final class Prattle {
    /** The single instance. */
    private final static Prattle INSTANCE = create();
    
    private final Consumer consumer;

    /**
     * Construct an instance of the Prattle logger that feeds messages to the
     * given consumer strategy. The consumer strategy is either the core
     * strategy or a null strategy if Prattle is disabled.
     * 
     * @param consumer
     *            The Prattle consumer.
     */
    public Prattle(Consumer consumer) {
        this.consumer = consumer;
    }

    /**
     * Shutdown the consumer thread, blocking until it finishes.
     */
    public void shutdown() {
        consumer.shutdown();
    }
    
    public static Prattle getInstance()
    {
        return INSTANCE;
    }

    private static void read(Properties properties, List<Recorder> recorders) throws Exception
    {
        for (String recorderName : properties.getProperty("prattle.recorders", "").split(",\\s*"))
        {
            String prefix = "prattle.recorder." + recorderName;
            String className = properties.getProperty(prefix);
            Class<?> recorderClass = Class.forName(className);
            Recorder recorder = (Recorder) recorderClass.newInstance();
            recorder.initialize(prefix + ".", new Configuration(properties));
            recorders.add(recorder);
        }
    }

    private static Prattle create()
    {
        List<Recorder> recorders = new ArrayList<Recorder>();
        try
        {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources("prattle.properties");
        
            while (resources.hasMoreElements())
            {
                URL url = resources.nextElement();
                Properties properties = new Properties();
                properties.load(url.openStream());
                read(properties, recorders);
            }
        }
        catch (Exception e)
        {
            System.err.println("Unable to configure Prattle extended logging.");
            e.printStackTrace();
            return new Prattle(new NullConsumer());
        }
    
        if (recorders.isEmpty())
        {
            return new Prattle(new NullConsumer());
        }

        return new Prattle(new CoreConsumer(recorders));
    }
    
    public void send(Message message)
    {
        consumer.consume(message);
    }
}
