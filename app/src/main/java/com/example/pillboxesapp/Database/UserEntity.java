package com.example.pillboxesapp.Database;

public class UserEntity {
    private String userID;
    private String name;
    private String email;
    private boolean isCarer;
    private boolean needsCarer;


    public UserEntity(String userID, String name, String email, boolean isCarer, boolean needsCarer) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.isCarer = isCarer;
        this.needsCarer = needsCarer;
    }

    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public boolean isCarer() {
        return isCarer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCarer(boolean carer) {
        isCarer = carer;
    }
}