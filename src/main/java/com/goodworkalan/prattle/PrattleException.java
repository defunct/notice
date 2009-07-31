package com.goodworkalan.prattle;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class PrattleException extends RuntimeException
{
    /** The serial version id. */
    private static final long serialVersionUID = 20080620L;
    
    /** The error code. */
    private final int code;

    /** A list of arguments to the formatted error message. */
    private final List<Object> arguments = new ArrayList<Object>();
    
    /** A variable substitution in the property file creates an infinite loop. */
    public final static int PROPERTY_LOOP = 101;
    
    /** A property value ends with a backslash character, does not actually escape anything. */
    public final static int TERMINAL_BACKSLASH = 102;

    /**
     * Create a Sheaf exception with the given error code.
     * 
     * @param code
     *            The error code.
     */
    public PrattleException(int code)
    {
        super();
        this.code = code;
    }

    /**
     * Wrap the given cause exception in an addendum exception with the given
     * error code.
     * 
     * @param code
     *            The error code.
     * @param cause
     *            The cause exception.
     */
    public PrattleException(int code, Throwable cause)
    {
        super(cause);
        this.code = code;
    }

    /**
     * Get the error code.
     * 
     * @return The error code.
     */
    public int getCode()
    {
        return code;
    }

    /**
     * Add an argument to the list of arguments to provide the formatted error
     * message associated with the error code.
     * 
     * @param argument
     *            The format argument.
     * @return This sheaf exception for chained invocation of add.
     */
    public PrattleException add(Object argument)
    {
        arguments.add(argument);
        return this;
    }

    /**
     * Create an detail message from the error message format associated with
     * the error code and the format arguments.
     * 
     * @return The exception message.
     */
    @Override
    public String getMessage()
    {
        String key = Integer.toString(code);
        ResourceBundle exceptions = ResourceBundle.getBundle("com.goodworkalan.prattle.exceptions");
        String format;
        try
        {
            format = exceptions.getString(key);
        }
        catch (MissingResourceException e)
        {
            return key;
        }
        try
        {
            return String.format(format, arguments.toArray());
        }
        catch (Throwable e)
        {
            throw new Error(key, e);
        }
    }
}