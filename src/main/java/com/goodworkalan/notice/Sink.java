package com.goodworkalan.notice;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
    private final static ReflectiveFactory reflective = new ReflectiveFactory();

    /** The map of sink names to sinks. */
    private final static ConcurrentMap<String, Sink> sinks = new ConcurrentHashMap<String, Sink>();

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
     * Get the default notice sink.
     * 
     * @return The default notice sink.
     */
    public static Sink getInstance() {
        return getInstance("default");
    }

    /**
     * Get the notice sink with the given name. The notice sink will be
     * configured using the properties file found in the
     * <code>com.goodworkalan.notice</code> package that contains the given
     * name, in the format <code>sink.name.properties</code>, where name is the
     * given name.
     * <p>
     * FIXME Make it so. (Name is different.)
     * <p>
     * FIXME Strange to add to someone else's package. Maybe
     * <code>META-INF/com.goodworkalan.notice/sink.properties</code>.
     * 
     * @param name
     *            The sink name.
     * @return
     */
    public static Sink getInstance(String name) {
        Sink sink = sinks.get(name);
        if (sink == null) {
            Sink newSink = create(name);
            sinks.putIfAbsent(name, newSink);
            sink = sinks.get(name);
            if (sink != newSink) {
                newSink.shutdown();
            }
        }
        return sink;
    }

    /**
     * Read a <code>notice.properties</code> configuration file, constructing
     * the specified recorders and appending them to the given list.
     * 
     * @param properties
     *            The properties file to load.
     * @param recorders
     *            The list of recorders.
     */
    private static void read(Properties properties, List<Configuration> configurations) {
        for (String recorderName : properties.getProperty("recorders", "").split(",\\s*")) {
            String prefix = "recorder." + recorderName;
            String className = properties.getProperty(prefix);
            Class<?> recorderClass;
            try {
                recorderClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new NoticeException(0, e);
            }
            Recorder recorder;
            try {
                recorder = (Recorder) reflective.getConstructor(recorderClass).newInstance();
            } catch (ReflectiveException e) {
                throw new NoticeException(0, e);
            }
            configurations.add(new Configuration(recorder, prefix + ".", properties));
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
    private static Sink create(String name) {
        List<Configuration> configurations = new ArrayList<Configuration>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String fileName = name.equals("default") ? "notice.properties" : "notice." + name + ".properties";
            Enumeration<URL> resources = classLoader.getResources(fileName);

            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                Properties properties = new Properties();
                properties.load(url.openStream());
                read(properties, configurations);
            }
        } catch (Exception e) {
            System.err.println("Unable to configure Prattle extended logging.");
            e.printStackTrace();
            return new Sink(new NullConsumer());
        }

        if (configurations.isEmpty()) {
            return new Sink(new NullConsumer());
        }

        return new Sink(new CoreConsumer(configurations));
    }

    /**
     * Feed a message to the consumers thread. This method will insert the
     * message into an non-blocking queue and return immediately.
     * 
     * @param message
     *            The message to record.
     */
    void send(Map<String, Object> entry) {
        consumer.consume(entry);
    }
}
