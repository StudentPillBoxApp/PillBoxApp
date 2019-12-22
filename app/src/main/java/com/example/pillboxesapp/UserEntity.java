package com.example.pillboxesapp;

public class UserEntity {
    private String userID;
    private String email;
    private boolean isCarer;
    private boolean needsCarer;

    public UserEntity(String userID, String email, boolean isCarer, boolean needsCarer) {
        this.userID = userID;
        this.email = email;
        this.isCarer = isCarer;
        this.needsCarer = needsCarer;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCarer(boolean carer) {
        isCarer = carer;
    }
}