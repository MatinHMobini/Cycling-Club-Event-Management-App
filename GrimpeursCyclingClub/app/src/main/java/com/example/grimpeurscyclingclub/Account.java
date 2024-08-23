package com.example.grimpeurscyclingclub;

import java.io.Serializable;

public class Account implements Serializable {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Account Attributes
    private String username;
    private String password;
    private String email;

    //------------------------
    // CONSTRUCTORS
    //------------------------

    //Zero-arity constructor needed for methods in UserCatalogue
    public Account() {

    }


    public Account(String aUsername, String aPassword, String aEmail)
    {
        //TODO: Pass a DatabaseReference to a created account so they can access database?
        username = aUsername;
        password = aPassword;
        email = aEmail;
    }

    //------------------------
    // INTERFACE
    //------------------------

    public boolean setUsername(String aUsername)
    {
        boolean wasSet = false;
        username = aUsername;
        wasSet = true;
        return wasSet;
    }

    public boolean setPassword(String aPassword)
    {
        boolean wasSet = false;
        password = aPassword;
        wasSet = true;
        return wasSet;
    }

    public boolean setEmail(String aEmail)
    {
        boolean wasSet = false;
        email = aEmail;
        wasSet = true;
        return wasSet;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getEmail()
    {
        return email;
    }

    public void delete()
    {}


    public String toString()
    {
        return super.toString() + "["+
                "username" + ":" + getUsername()+ "," +
                "password" + ":" + getPassword()+ "," +
                "email" + ":" + getEmail()+ "]";
    }
}