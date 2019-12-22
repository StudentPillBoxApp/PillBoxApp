package com.example.pillboxesapp;

public class AlarmEntity {
    private String alarmID;
    private String startDate;
    private String startTime;
    private String pillBoxID;
    private int repeat;
    private int quantity;

    public AlarmEntity(String startDate, String startTime, String pillBoxID, int quantity, int repeat) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.pillBoxID = pillBoxID;
        this.quantity = quantity;
        this.repeat = repeat;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getPillBoxID() {
        return pillBoxID;
    }

    public int getRepeat() {
        return repeat;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getAlarmID() {
        return alarmID;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAlarmID(String alarmID) {
        this.alarmID = alarmID;
    }
}