package com.example.pillboxesapp;

import android.app.Application;

public class MyApplication extends Application {
    private String currentBoxID;

    public String getCurrentBoxID() {
        return currentBoxID;
    }

    public void setCurrentBoxID(String boxID) {
        currentBoxID = boxID;
    }
}
