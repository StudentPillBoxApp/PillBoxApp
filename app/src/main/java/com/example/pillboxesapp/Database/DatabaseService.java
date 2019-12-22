package com.example.pillboxesapp.Database;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatabaseService implements IDatabaseService {
    private FirebaseFirestore database;

    public DatabaseService() {
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public void addUser(UserEntity userEntity, final ICallbackResult callback) {
        Map<String, Object> userData = createUserDataSet(userEntity);
        database.collection(COLLECTION_USER).document(userEntity.getUserID()).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(true);
                } else {
                    callback.onCallback(false);
                }
            }
        });
    }

    private Map<String, Object> createUserDataSet(UserEntity userEntity) {
        Map<String, Object> userData = new HashMap<>();
        String userType = VALUE_PATIENT;
        if (userEntity.isCarer()) {
            userType = VALUE_CARER;
        }
        userData.put(FIELD_EMAIL, userEntity.getEmail());
        userData.put(FIELD_USER_TYPE, userType);
        return userData;
    }

    @Override
    public void addPillBox(PillBoxEntity pillBoxEntity, final ICallbackResult callback) {
        Map<String, Object> pillData = createPillBoxDataSet(pillBoxEntity);
        database.collection(COLLECTION_PILL_BOXES).document(pillBoxEntity.getID()).set(pillData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(true);
                } else {
                    callback.onCallback(false);
                }
            }
        });
    }

    private Map<String, Object> createPillBoxDataSet(PillBoxEntity pillBoxEntity) {
        Map<String, Object> pillData = new HashMap<>();
        pillData.put(FIELD_USER_ID, pillBoxEntity.getUserID());
        pillData.put(FIELD_BOX_NUM, pillBoxEntity.getBoxNum());
        pillData.put(FIELD_PILL_NAME, pillBoxEntity.getPillName());
        pillData.put(FIELD_STOCK, pillBoxEntity.getStock());
        return pillData;
    }

    @Override
    public void addAlarm(AlarmEntity alarmEntity, final ICallbackResult callback) {
        Map<String, Object> alarmData = createAlarmDataSet(alarmEntity);
        database.collection(COLLECTION_ALARMS).document().set(alarmData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(true);
                } else {
                    callback.onCallback(false);
                }
            }
        });
    }

    private Map<String, Object> createAlarmDataSet(AlarmEntity alarmEntity) {
        Map<String, Object> alarmData = new HashMap<>();
        alarmData.put(FIELD_PILL_BOX_ID, alarmEntity.getPillBoxID());
        alarmData.put(FIELD_DATE, alarmEntity.getStartDate());
        alarmData.put(FIELD_TIME, alarmEntity.getStartTime());
        alarmData.put(FIELD_REPEAT, alarmEntity.getRepeat());
        alarmData.put(FIELD_QUANTITY, alarmEntity.getQuantity());
        return alarmData;
    }

    @Override
    public void deleteUser(String userID, final ICallbackResult callback) {
        DocumentReference userReference = database.collection(COLLECTION_USER).document(userID);
        userReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(true);
                } else {
                    callback.onCallback(false);
                }
            }
        });
    }

    @Override
    public void deletePillbox(String pillBoxID, final ICallbackResult callback) {
        DocumentReference pillBoxReference = database.collection(COLLECTION_PILL_BOXES).document(pillBoxID);
        pillBoxReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(true);
                } else {
                    callback.onCallback(false);
                }
            }
        });
    }

    @Override
    public void deleteAlarm(String alarmID, final ICallbackResult callback) {
        DocumentReference alarmReference = database.collection(COLLECTION_ALARMS).document(alarmID);
        alarmReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(true);
                } else {
                    callback.onCallback(false);
                }
            }
        });
    }

    @Override
    public void findCarerByEmail(String email, final ICallbackResult callback) {
        CollectionReference userCollection = database.collection(COLLECTION_USER);
        Query query = userCollection.whereEqualTo(FIELD_EMAIL, email).whereEqualTo(FIELD_USER_TYPE, VALUE_CARER);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(getUserIdAndName(task));
                }
            }
        });
    }

    @Override
    public void findPatientByEmail(String email, final ICallbackResult callback) {
        CollectionReference userCollection = database.collection(COLLECTION_USER);
        Query query = userCollection.whereEqualTo(FIELD_EMAIL, email).whereEqualTo(FIELD_USER_TYPE, VALUE_PATIENT);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(getUserIdAndName(task));
                }
            }
        });
    }

    private String[] getUserIdAndName(@NonNull Task<QuerySnapshot> task) {
        String[] userDetails = {"", ""};
        for (QueryDocumentSnapshot document : task.getResult()) {
            if (document.exists()) {
                userDetails[0] = document.getId();
                Object name = document.get(FIELD_USER_NAME);
                if (name != null) {
                    userDetails[1] = name.toString();
                }
            }
        }
        return userDetails;
    }

    @Override
    public void isUserCarer(String userID, final ICallbackResult callback) {
        DocumentReference userReference = database.collection(COLLECTION_USER).document(userID);
        userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userType = getUserType(task);
                    if (userType.equals(VALUE_CARER)) {
                        callback.onCallback(true);
                    } else if (userType.equals(VALUE_PATIENT)) {
                        callback.onCallback(false);
                    }
                }
            }
        });
    }

    private String getUserType(@NonNull Task<DocumentSnapshot> task) {
        String userType = "";
        DocumentSnapshot userEntity = task.getResult();
        if (userEntity.exists()) {
            Object type = userEntity.get(FIELD_USER_TYPE);
            if (type != null) {
                userType = type.toString();
            }
        }
        return userType;
    }

    @Override
    public void loadCarersPatients(String carerID, final ICallbackResult callback) {
        CollectionReference userCollection = database.collection(COLLECTION_USER);
        Query query = userCollection.whereEqualTo(FIELD_CARER_ID, carerID).whereEqualTo(FIELD_USER_TYPE, VALUE_PATIENT);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(collectIds(task));
                }
            }
        });
    }

    private ArrayList<String> collectIds(@NonNull Task<QuerySnapshot> task) {
        ArrayList<String> collectionIDs = new ArrayList<>();
        for (QueryDocumentSnapshot document : task.getResult()) {
            if (document.exists()) {
                collectionIDs.add(document.getId());
            }
        }
        return collectionIDs;
    }

    @Override
    public void loadUsersPillBoxes(String userID, final ICallbackResult callback) {
        CollectionReference pillBoxCollection = database.collection(COLLECTION_PILL_BOXES);
        Query query = pillBoxCollection.whereEqualTo(FIELD_USER_ID, userID);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(collectPillBoxes(task));
                }
            }
        });
    }

    private ArrayList<PillBoxEntity> collectPillBoxes(@NonNull Task<QuerySnapshot> task) {
        ArrayList<PillBoxEntity> pillBoxEntities = new ArrayList<>();
        for (QueryDocumentSnapshot document : task.getResult()) {
            if (document.exists()) {
                String id = document.getId();
                String userID = (String) document.get(FIELD_USER_ID);
                int boxNum = (int) document.get(FIELD_BOX_NUM);
                String pillName = (String) document.get(FIELD_PILL_NAME);
                int stock = (int) document.get(FIELD_STOCK);
                PillBoxEntity pillBoxEntity = new PillBoxEntity(id, userID, pillName, boxNum, stock);
                pillBoxEntities.add(pillBoxEntity);
            }
        }
        return pillBoxEntities;
    }

    @Override
    public void loadPillBoxAlarms(String pillBoxUID, final ICallbackResult callback) {
        CollectionReference pillBoxCollection = database.collection(COLLECTION_ALARMS);
        Query query = pillBoxCollection.whereEqualTo(FIELD_PILL_BOX_ID, pillBoxUID);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(collectAlarms(task));
                }
            }
        });
    }

    private ArrayList<AlarmEntity> collectAlarms(@NonNull Task<QuerySnapshot> task) {
        ArrayList<AlarmEntity> alarmEntities = new ArrayList<>();
        for (QueryDocumentSnapshot document : task.getResult()) {
            if (document.exists()) {
                String id = document.getId();
                String startDate = (String) document.get(FIELD_DATE);
                String startTime = (String) document.get(FIELD_DATE);
                int repeat = (int) document.get(FIELD_REPEAT);
                int quantity = (int) document.get(FIELD_QUANTITY);
                AlarmEntity alarmEntity = new AlarmEntity(id, startDate, startTime, repeat, quantity);
                alarmEntities.add(alarmEntity);
            }
        }
        return alarmEntities;
    }

    @Override
    public void loadUsersAlarms(String userID, final ICallbackResult callback) {
        CollectionReference pillBoxCollection = database.collection(COLLECTION_PILL_BOXES);
        Query query = pillBoxCollection.whereEqualTo(FIELD_USER_ID, userID);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            String id = document.getId();
                            loadPillBoxAlarms(id, callback);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void removeRedundantAlarms(final ICallbackResult callback) {
        int nonRepeat = 0;
        CollectionReference alarmCollection = database.collection(COLLECTION_ALARMS);
        Query query = alarmCollection.whereEqualTo(FIELD_REPEAT, nonRepeat);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            if (isAlarmDateOld(document)) {
                                deleteAlarm(document.getId(), callback);
                            }
                        }
                    }
                }
            }
        });
    }

    private boolean isAlarmDateOld(QueryDocumentSnapshot document) {
        boolean redundant = false;
        String strDate = (String) document.get(FIELD_DATE);
        String strTime = (String) document.get(FIELD_TIME);
        String dateAndTime = strDate + " " + strTime;
        Date alarmDate = new Date();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            alarmDate = dateFormat.parse(dateAndTime);
        } catch (java.text.ParseException e) {
            return false;
        }
        Date today = Calendar.getInstance().getTime();
        if (alarmDate.compareTo(today) <= 0) {
            redundant = true; // Stored date is before today's date.
        }
        return redundant;
    }
}