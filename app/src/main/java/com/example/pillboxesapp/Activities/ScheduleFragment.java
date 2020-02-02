package com.example.pillboxesapp.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pillboxesapp.Database.AlarmEntity;
import com.example.pillboxesapp.Database.CallbackResult;
import com.example.pillboxesapp.Database.DatabaseService;
import com.example.pillboxesapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ScheduleFragment extends Fragment {
    private String pillName;
    private ArrayList<AlarmEntity> allAlarms;
    private View view;
    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule, container, false);
        if (container != null && container.getContext() instanceof HomeActivity) {
            homeActivity = (HomeActivity) container.getContext();
        }
        loadAllAlarms();
        return view;
    }

    private void loadAllAlarms() {
        allAlarms = new ArrayList<>();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseService dbService = new DatabaseService();
        dbService.loadUsersAlarms(userID, new CallbackResult() {
            @Override
            public void onCallback(ArrayList results) {
                for (Object result : results) {
                    if (result instanceof AlarmEntity) {
                        AlarmEntity alarm = (AlarmEntity) result;
                        generateAlarmDataset(alarm);
                    }
                }
                organiseAllAlarms();
                displayAllAlarms1();
            }
        });
        if (allAlarms.isEmpty()) {
            ArrayList<String> alarmStrings = new ArrayList<>();
            alarmStrings.add("No Alarms Set");
            ListView listView = (ListView) view.findViewById(R.id.ListView1);
            ArrayAdapter arrayAdapter = new ArrayAdapter(homeActivity, R.layout.row, alarmStrings);
            listView.setAdapter(arrayAdapter);
        }
    }

    private boolean generateAlarmDataset(AlarmEntity alarm) {
        String strDate = alarm.getStartDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(strDate));
        } catch (ParseException e) {
            return false;
        }
        c.add(Calendar.DATE, 10);
        String endDate = sdf.format(c.getTime());
        try {
            c.setTime(sdf.parse(strDate));
        } catch (ParseException e) {
            return false;
        }
        String firstDate = sdf.format(c.getTime());
        alarm.setStartDate(firstDate);
        allAlarms.add(alarm);
        String tempDate;
        boolean correct = true;
        while (correct) {
            AlarmEntity tempAlarm = new AlarmEntity(alarm.getStartDate(), alarm.getStartTime(), alarm.getPillBoxID(), alarm.getQuantity(), alarm.getRepeat());
            c.add(Calendar.DATE, alarm.getRepeat());
            tempDate = sdf.format(c.getTime());
            try {
                correct = sdf.parse(tempDate).before(sdf.parse(endDate));
            } catch (ParseException e) {
                return false;
            }
            if (correct) {
                tempAlarm.setStartDate(tempDate);
                allAlarms.add(tempAlarm);
            }
        }
        return true;
    }

    private void organiseAllAlarms() {
        Collections.sort(allAlarms, new Comparator<AlarmEntity>() {
            @Override
            public int compare(AlarmEntity lhs, AlarmEntity rhs) {
                String strDate = lhs.getStartDate();
                String strTime = lhs.getStartTime();
                String dateAndTime = strDate + " " + strTime;
                Date lhsAlarmDate = new Date();
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                    lhsAlarmDate = dateFormat.parse(dateAndTime);
                } catch (java.text.ParseException e) {

                }
                strDate = rhs.getStartDate();
                strTime = rhs.getStartTime();
                dateAndTime = strDate + " " + strTime;
                Date rhsAlarmDate = new Date();
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                    rhsAlarmDate = dateFormat.parse(dateAndTime);
                } catch (java.text.ParseException e) {

                }
                boolean correct = lhsAlarmDate.before(rhsAlarmDate);
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return correct ? -1 : 1;
            }
        });
    }

    private void displayAllAlarms1() {
        AlarmEntity alarm = allAlarms.get(0);
        DatabaseService dbService = new DatabaseService();
        dbService.getPillBoxNameFromID(alarm.getPillBoxID(), new CallbackResult() {
            @Override
            public void onCallback(String[] result) {
                pillName = result[0];
                displayAllAlarms2();
            }
        });
    }

    private void displayAllAlarms2() {
        ArrayList<String> alarmStrings = new ArrayList<>();
        for (AlarmEntity alarm : allAlarms) {
            String alarmInfo = alarm.getStartDate() + " - " + alarm.getStartTime() + " - " + pillName;
            alarmStrings.add(alarmInfo);
        }
        ListView listView = (ListView) view.findViewById(R.id.ListView1);
        ArrayAdapter arrayAdapter = new ArrayAdapter(homeActivity, R.layout.row, alarmStrings);
        listView.setAdapter(arrayAdapter);
    }
}