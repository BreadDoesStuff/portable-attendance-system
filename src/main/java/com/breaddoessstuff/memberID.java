package com.breaddoessstuff;

public class memberID {

    private String secret;
    private String studentEmail;
    private boolean isPresent;
    
    // Set default memberID in case data isn't assigned
    public memberID() 
    {
        secret = "null";
        studentEmail = "null";
        isPresent = false;
    }

    public memberID(String secret, String studentEmail, boolean isPresent)
    {
        this.secret = secret;
        this.studentEmail = studentEmail;
        this.isPresent = isPresent;
    }

    // Mutators

    void setSecret(String secret)
    {
        this.secret = secret;
    }

    void setEmail(String studentEmail)
    {
        this.studentEmail = studentEmail;
    }

    void setPresence(boolean isPresent)
    {
        this.isPresent = isPresent;
    }

    // Accessors

    String getSecret()
    {
        return this.secret;
    }

    String getEmail()
    {
        return this.studentEmail;
    }

    boolean getPresence()
    {
        return this.isPresent;
    }

}
