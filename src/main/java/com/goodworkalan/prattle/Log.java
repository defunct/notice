package com.goodworkalan.prattle;

import java.io.Serializable;

public interface Log {
    /**
     * Add a message that will be written to the default application log as well
     * as sent to the message recivers.
     * 
     * @param format
     *            The message format in sprintf format.
     * @param args
     *            The arguments to pass to the format.
     * @return This log language element to continue the logging statement.
     */
    public Log message(String format, Object... args);

    /**
     * Write the given object property flagged with the given name. The name
     * must be a valid Java identifier.
     * <p>
     * The given object is passed to a logging thread. The given object must not
     * mutate in the application thread. If the application thread is going to
     * continue to mutate the object, the object should be frozen instead with a
     * call to {@link #freeze(String, Object, Class...) freeze}.
     * 
     * @param name
     *            The property name.
     * @param object
     *            The object property.
     * @return This log language element to continue the logging statement.
     */
    public Log object(String name, Object object);

    /**
     * Write an object with the given id, freezing the given object so that it
     * can mutate in the application thread while it is waiting to be written in
     * the consumer thread.
     * 
     * @param name
     *            The property name.
     * @param object
     *            The object property to freeze.
     * @param freeze
     *            The classes to freeze when encountered.
     * @return This log language element to continue the logging statement.
     */
    public Log freeze(String name, Object object, Class<?>... freeze);

    /**
     * Write an object property flagged with the given name, serializing the
     * object so that in the application thread while the serialized it is
     * waiting to be deserialized and written in the consumer thread.
     * 
     * @param name
     *            The property name.
     * @param serializable
     *            The object property to serialize.
     * @return This log language element to continue the logging statement.
     */
    public Log serialize(String name, Serializable serializable);

    /**
     * Write the string value of the object property flagged with the given
     * name. The object is converted to a string by calling
     * {@link Object#toString()} on the given object.
     * 
     * @param name
     *            The property name.
     * @param object
     *            The object to convert to a string.
     * @return This log language element to continue the logging statement.
     */
    public Log string(String name, Object object);

    /**
     * Create a map building language element to build a map that will be
     * written to the log flagged with the given property name.
     * 
     * @param name
     *            The property name.
     * @return A map building language element to build a map to log.
     */
    public Mapper<Log> map(String id);

    /**
     * Create a map building language element to build a map that will be
     * written to the log flagged with the given property name.
     * 
     * @param name
     *            The property name.
     * @return A map building language element to build a map to log.
     */
    public Lister<Log> list(String id);

    /**
     * Write the log message to the default message log and send the output
     * properties to the consumer tread.
     */
    public void send();
}
