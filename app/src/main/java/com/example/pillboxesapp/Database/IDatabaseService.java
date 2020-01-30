package com.example.pillboxesapp.Database;

public interface IDatabaseService {
    String FIELD_USER_NAME = "name";
    String FIELD_EMAIL = "email";
    String FIELD_USER_TYPE = "user_type";
    String FIELD_USER_ID = "user_id";
    String FIELD_CARER_ID = "carer_id";
    String FIELD_BOX_NUM = "box_num";
    String FIELD_PILL_NAME = "pill_name";
    String FIELD_PILL_BOX_ID = "pill_box_id";
    String FIELD_STOCK = "stock";
    String FIELD_DATE = "date";
    String FIELD_TIME = "time";
    String FIELD_REPEAT = "repeat";
    String FIELD_QUANTITY = "quantity";

    String VALUE_CARER = "carer";
    String VALUE_PATIENT = "patient";

    String COLLECTION_USER = "Users";
    String COLLECTION_PILL_BOXES = "PillBoxes";
    String COLLECTION_ALARMS = "Alarms";


    void addUser(UserEntity userEntity, final ICallbackResult callback);

    void addPillBox(PillBoxEntity pillBoxEntity, final ICallbackResult callback);

    void addAlarm(AlarmEntity alarmEntity, final ICallbackResult callback);

    void deleteUser(String userID, final ICallbackResult callback);

    void deletePillbox(String pillBoxID, final ICallbackResult callback);

    void deleteAlarm(String alarmID, final ICallbackResult callback);

    void findCarerByEmail(String email, final ICallbackResult callback);

    void findPatientByEmail(String email, final ICallbackResult callback);

    void isUserCarer(String userID, final ICallbackResult callback);

    void linkPatientAndCarer(String patientID, String CarerID, final ICallbackResult callback);

    void loadCarersPatients(String carerID, final ICallbackResult callback);

    void loadUsersPillBoxes(String userID, final ICallbackResult callback);

    void loadPillBoxAlarms(String pillBoxUID, final ICallbackResult callback);

    void loadUsersAlarms(String userID, final ICallbackResult callback);

    void removeRedundantAlarms(final ICallbackResult callback);

}
