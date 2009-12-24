package com.goodworkalan.notice;

public interface Noticeable<Self> {

    /**
     * Flatten the given object, referencing the Entry configuration to
     * determine if the object should be converted to a <code>Map</code>
     * or converted to a <code>String</code> using <code>toString</code>.
     * <p>
     * The given object graph must be a tree. Logging cyclical graphs will
     * result in endless recursion.
     * <p>
     * Object in the tree are turned into maps of their properites, unless
     * the object has been marked as one that will be converted using either
     * the toString method or a string converter.  
     * 
     * @param name
     *            The property name.
     * @param object
     *            The object property.
     * @return This log language element to continue the logging statement.
     */
    public Self put(String name, Object object);

    public Self put(String name, Object object, String... paths);

    public Self put(String name, Object object, boolean recurse);

    /**
     * Create a map building language element to build a map that will be
     * written to the log flagged with the given property name.
     * 
     * @param name
     *            The property name.
     * @return A map building language element to build a map to log.
     */
    public Mapper<Self> map(String id);

    /**
     * Create a map building language element to build a map that will be
     * written to the log flagged with the given property name.
     * 
     * @param name
     *            The property name.
     * @return A map building language element to build a map to log.
     */
    public Lister<Self> list(String id);

    /**
     * Write the log message to the default message log and send the output
     * properties to the consumer tread.
     */
    public void send(Sink sink);
}