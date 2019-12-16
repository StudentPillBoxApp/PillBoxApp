package com.example.pillboxesapp;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DatabaseService implements IDatabaseService {
    private final String userCollectionID = "users";
    private final String pillBoxCollectionPathName = "PillBoxes";
    private FirebaseFirestore database;

    public DatabaseService() {
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public void addUser(String userID, Map<String, Object> userData) {
        database.collection(userCollectionID).document(userID).set(userData);
    }

    @Override
    public Map<String, Object> createUserDataSet(String email, boolean isCarer, boolean needsCarer) {
        Map<String, Object> userData = new HashMap<>();
        String userType = "patient";
        if (isCarer) {
            userType = "carer";
        }
        userData.put("email", email);
        userData.put("user_type", userType);
        userData.put("needs_carer", needsCarer);
        return userData;
    }

    @Override
    public void addPillBox(String userID, String pillBoxUID, Map<String, Object> pillData) {
        DocumentReference userReference = database.collection(userCollectionID).document(userID);
        DocumentReference pillBoxReference = userReference.collection(pillBoxCollectionPathName).document(pillBoxUID);
        pillBoxReference.set(pillData);
    }

    @Override
    public Map<String, Object> createPillDataSet(int boxNum, String pillName) {
        int unknownQuantity = -1;
        return createPillDataSet(boxNum, pillName, unknownQuantity);
    }

    @Override
    public Map<String, Object> createPillDataSet(int boxNum, String pillName, int stockCount) {
        Map<String, Object> pillData = new HashMap<>();
        pillData.put("box_num", boxNum);
        pillData.put("pill_name", pillName);
        pillData.put("stock", stockCount);
        return pillData;
    }

    @Override
    public void addAlarm(String userID, String pillBoxUID, Map<String, Object> alarmData) {
        DocumentReference userReference = database.collection(userCollectionID).document(userID);
        DocumentReference pillBoxReference = userReference.collection(pillBoxCollectionPathName).document(pillBoxUID);
        DocumentReference alarmReference = pillBoxReference.collection("Alarms").document();
        alarmReference.set(alarmData);
    }

    @Override
    public Map<String, Object> createAlarmDataSet(String startDate, String startTime, int repeat, int quantity) {
        Map<String, Object> alarmData = new HashMap<>();
        alarmData.put("date", startDate);
        alarmData.put("time", startTime);
        alarmData.put("repeat", repeat);
        alarmData.put("quantity", quantity);
        return alarmData;
    }
}
