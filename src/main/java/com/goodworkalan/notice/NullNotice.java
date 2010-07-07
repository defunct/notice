package com.goodworkalan.notice;

/**
 * A do nothing implementation of <code>Notice</code> returned by
 * <code>NoticeFactory</code> when a level is disabled.
 * 
 * @author Alan Gutierrez
 */
class NullNotice extends Notice {
    /** The singleton instance. */
    public final static Notice INSTANCE = new NullNotice();

    /**
     * Does nothing.
     * 
     * @param name
     *            The stop watch name.
     * @return This notice in order to chain method calls.
     */
    public Notice start(String name) {
        return this;
    }

    /**
     * Does nothing.
     * 
     * @param name
     *            The stop watch name.
     * @return This notice in order to chain method calls.
     */
    public Notice stop(String name) {
        return this;
    }

    /**
     * Does nothing.
     * 
     * @param name
     *            The map key.
     * @param object
     *            The object to diffuse and add to map.
     * @param includes
     *            The paths to include in the recursive diffusion.
     * @return This notice to continue to build the notice.
     */
    public Notice put(String name, Object object, String... paths) {
        return this;
    }

    /**
     * Return a do nothing implementation of <code>Mapper</code>.
     * 
     * @param key
     *            The notice entry key.
     * @return A do nothing list builder to build the child list.
     */
    public Lister<Notice> list(String key) {
        return new NullLister<Notice>(this);
    }

    /**
     * Return a do nothing implementation of <code>Mapper</code>.
     * 
     * @param key
     *            The notice entry key.
     * @return A do nothing map builder to build the child map.
     */
    public Mapper<Notice> map(String key) {
        return new NullMapper<Notice>(this);
    }

    /**
     * Does nothing.
     * 
     * @param sink
     *            The sink to write to.
     */
    public void send(Sink sink) {
    }

    /** Does nothing. */
    public void send() {
    }
}
