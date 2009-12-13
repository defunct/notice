package com.goodworkalan.prattle;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.goodworkalan.reflective.ReflectiveException;
import com.goodworkalan.reflective.ReflectiveFactory;

/**
 * Singleton instance of a sink that consumes Prattle messages.
 * <p>
 * Prattle messages are consumed by the sink by passign them to method
 * {@link #send(Message) send}.
 * 
 * @author Alan Gutierrez
 */
public final class Sink {
    /** The reflective factory used to build recorders. */
    private final static ReflectiveFactory reflectiveFactory = new ReflectiveFactory();
    
    /** The single instance. */
    private final static Sink INSTANCE = create();
    
    /** The consume strategy. */
    private final Consumer consumer;

    /**
     * Construct an instance of the Prattle logger that feeds messages to the
     * given consumer strategy. The consumer strategy is either the core
     * strategy or a null strategy if Prattle is disabled.
     * 
     * @param consumer
     *            The Prattle consumer.
     */
    Sink(Consumer consumer) {
        this.consumer = consumer;
    }

    /**
     * Shutdown the consumer thread, blocking until it finishes.
     */
    public void shutdown() {
        consumer.shutdown();
    }

    /**
     * Get the Prattle logger Singleton.
     * 
     * @return The Singleton longer instance.
     */
    public static Sink getInstance() {
        return INSTANCE;
    }

    /**
     * Read a <code>prattle.properties</code> configuraiton file, constructing
     * the specified recorders and appending them to the given list.
     * 
     * @param properties
     *            The Prattle properties file to load.
     * @param recorders
     *            The list of recorders.
     */
    private static void read(Properties properties, List<Recorder> recorders) {
        for (String recorderName : properties.getProperty("prattle.recorders", "").split(",\\s*")) {
            String prefix = "prattle.recorder." + recorderName;
            String className = properties.getProperty(prefix);
            Class<?> recorderClass;
            try {
                recorderClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new PrattleException(0, e);
            }
            Recorder recorder;
            try {
                recorder = (Recorder) reflectiveFactory.getConstructor(recorderClass).newInstance();
            } catch (ReflectiveException e) {
                throw new PrattleException(0, e);
            }
            recorder.initialize(prefix + ".", new Configuration(properties));
            recorders.add(recorder);
        }
    }

    /**
     * Construct the singleton sink instance.
     * <p>
     * This method will search for files named <code>prattle.properties</code>
     * in the class path. It will load these properties files, instantiate the
     * {@link Recorder} classes specified in the <code>prattle.recorders</code>
     * property, and pass the properties file to the new recorder for further
     * configuration.
     * 
     * @return The singleton sink instance.
     */
    private static Sink create()
    {
        List<Recorder> recorders = new ArrayList<Recorder>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources("prattle.properties");
        
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                Properties properties = new Properties();
                properties.load(url.openStream());
                read(properties, recorders);
            }
        } catch (Throwable e) {
            System.err.println("Unable to configure Prattle extended logging.");
            e.printStackTrace();
            return new Sink(new NullConsumer());
        }
    
        if (recorders.isEmpty()) {
            return new Sink(new NullConsumer());
        }

        return new Sink(new CoreConsumer(recorders));
    }
    
    /**
     * Feed a message to the consumers thread. This method will insert
     * the message into an non-blocking queue and return immediately.
     * 
     * @param message
     */
    public void send(Message message) {
        consumer.consume(message);
    }
}
