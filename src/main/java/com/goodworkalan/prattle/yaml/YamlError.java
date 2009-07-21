package com.goodworkalan.prattle.yaml;

/**
 * An error message that is written to the YAML stream in lieu
 * of an object that failed to deserialize.
 * 
 * @author Alan Gutierrez
 */
public class YamlError
{
    /** The error message. */
    private final String message;
    
    /** The cause. */
    private final Throwable cause;

    /**
     * Create a YAML error.
     * 
     * @param message
     *            The error message.
     * @param cause
     *            The cause;
     */
    public YamlError(String message, Throwable cause)
    {
        this.message = message;
        this.cause = cause;
    }

    /**
     * Get the error message.
     * 
     * @return The error message.
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Get the cause.
     * 
     * @return The cause.
     */
    public Throwable getCause()
    {
        return cause;
    }
}
