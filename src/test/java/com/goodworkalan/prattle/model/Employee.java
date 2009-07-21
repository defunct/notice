package com.goodworkalan.prattle.model;

import java.io.Serializable;

public class Employee implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String firstName;
    
    private String lastName;
    
    private int employeeId;
    
    public Employee()
    {
    }
    
    public Employee(String firstName, String lastName, int employeeId)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;
    }
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    
    public String getLastName()
    {
        return lastName;
    }
    
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
    
    public int getEmployeeId()
    {
        return employeeId;
    }
    
    public void setEmployeeId(int hatSize)
    {
        this.employeeId = hatSize;
    }
    
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Employee)
        {
            Employee person = (Employee) object;
            return firstName.equals(person.firstName)
                && lastName.equals(person.lastName)
                && employeeId == person.employeeId;
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return 1;
    }
}
