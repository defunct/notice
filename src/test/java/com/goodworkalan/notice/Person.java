package com.goodworkalan.notice;

/**
 * An example person bean.
 *
 * @author Alan Gutierrez
 */
public class Person {
    /** The first name. */
    private String firstName;

    /** The last name. */
    private String lastName;

    /**
     * Create a person.
     * 
     * @param firstName
     *            The first name.
     * @param lastName
     *            The last name.
     */
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Get the first name.
     * 
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the last name.
     * 
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }
}
