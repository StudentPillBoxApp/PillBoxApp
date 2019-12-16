package com.example.pillboxesapp;

import java.util.Map;

public interface IDatabaseService {
    void addUser(String userID, Map<String, Object> userData);

    Map<String, Object> createUserDataSet(String email, boolean isCarer, boolean needsCarer);

    void addPillBox(String userID, String pillBoxUID, Map<String, Object> pillData);

    Map<String, Object> createPillDataSet(int boxNum, String pillName);

    Map<String, Object> createPillDataSet(int boxNum, String pillName, int stockCount);

    void addAlarm(String userID, String pillBoxUID, Map<String, Object> alarmData);

    Map<String, Object> createAlarmDataSet(String startDate, String startTime, int repeat, int quantity);
}
