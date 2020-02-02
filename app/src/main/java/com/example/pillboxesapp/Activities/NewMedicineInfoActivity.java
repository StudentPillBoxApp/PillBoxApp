package com.example.pillboxesapp.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pillboxesapp.Database.AlarmEntity;
import com.example.pillboxesapp.Database.CallbackResult;
import com.example.pillboxesapp.Database.DatabaseService;
import com.example.pillboxesapp.Database.PillBoxEntity;
import com.example.pillboxesapp.MyApplication;
import com.example.pillboxesapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

public class NewMedicineInfoActivity extends AppCompatActivity {
    private String pillBoxID;
    private Spinner spinner;
    private TextView txtViewTimeOne, txtViewTimeTwo, txtViewTimeThree, txtViewTimeFour, txtDate;
    private Button btnTimeOne, btnTimeTwo, btnTimeThree, btnTimeFour, btnSave, btnDate;
    private EditText txtMedicineName, txtNoPills, txtNumberOfDays;
    private Integer[] numbers = new Integer[]{1, 2, 3, 4};
    private TimePickerDialog timePicker;
    private int hour = 0;
    private int minute = 0;
    private CheckBox chkDaily, chkXdays, chkNever;
    private DatePickerDialog datePickerDialog;
    private int startYear, starthMonth, startDay;
    private int timesPerDay;
    private int timesCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine_info);
        MyApplication appState = ((MyApplication) getApplicationContext());
        pillBoxID = appState.getCurrentBoxID();
        if (pillBoxID == null) {
            createNewPillbox();
        }
        spinner = findViewById(R.id.spinner2);
        btnTimeOne = findViewById(R.id.btnTimeOne);
        btnTimeTwo = findViewById(R.id.btnTimeTwo);
        btnTimeThree = findViewById(R.id.btnTimeThree);
        btnTimeFour = findViewById(R.id.btnTimeFour);
        chkDaily = findViewById(R.id.everydayrepeat);
        chkXdays = findViewById(R.id.eveyxdaysrepeat);
        chkNever = findViewById(R.id.neverrepeat);
        btnDate = findViewById(R.id.btnDate);
        txtNumberOfDays = findViewById(R.id.editText5);

        txtViewTimeOne = findViewById(R.id.txtViewTimeOne);
        txtViewTimeTwo = findViewById(R.id.txtViewTimeTwo);
        txtViewTimeThree = findViewById(R.id.txtViewTimeThree);
        txtViewTimeFour = findViewById(R.id.txtViewTimeFour);
        txtDate = findViewById(R.id.txtDate);
        txtMedicineName = findViewById(R.id.MedicineName);
        txtNoPills = findViewById(R.id.EnterPills);
        btnSave = findViewById(R.id.button_save);

        spinner.setAdapter(new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, numbers));
        SpinnerSetListener();
        checkBoxListeners();
        ButtonSetListeners();
    }

    private void createNewPillbox() {
        DatabaseService dbService = new DatabaseService();
        pillBoxID = "67:52:29:eb:aa:a1";
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String pillName = "";
        int boxnum = 1;
        PillBoxEntity pillBoxEntity = new PillBoxEntity(pillBoxID, userID, pillName, boxnum);
        dbService.addPillBox(pillBoxEntity, new CallbackResult() {
            @Override
            public void onCallback(Boolean result) {
                super.onCallback(result);
            }
        });
    }

    private void checkBoxListeners() {
        chkDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkDaily.isChecked()) {
                    chkNever.setChecked(false);
                    chkXdays.setChecked(false);
                }
            }
        });
        chkNever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkNever.isChecked()) {
                    chkDaily.setChecked(false);
                    chkXdays.setChecked(false);
                }
            }
        });
        chkXdays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkXdays.isChecked()) {
                    chkDaily.setChecked(false);
                    chkNever.setChecked(false);
                }
            }
        });
    }

    private void ButtonSetListeners() {
        btnTimeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(NewMedicineInfoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = Integer.toString(selectedHour);
                        if (hour.equals("0")) {
                            hour = "00";
                        }
                        if (hour.equals("5")) {
                            hour = "05";
                        }
                        String minute = Integer.toString(selectedMinute);
                        if (minute.equals("0")) {
                            minute = "00";
                        }
                        if (minute.equals("5")) {
                            minute = "05";
                        }
                        txtViewTimeOne.setText(hour + ":" + minute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                timePicker.setTitle("Select Time");
                timePicker.show();
            }
        });

        btnTimeTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(NewMedicineInfoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = Integer.toString(selectedHour);
                        if (hour.equals("0")) {
                            hour = "00";
                        }
                        if (hour.equals("5")) {
                            hour = "05";
                        }
                        String minute = Integer.toString(selectedMinute);
                        if (minute.equals("0")) {
                            minute = "00";
                        }
                        if (minute.equals("5")) {
                            minute = "05";
                        }
                        txtViewTimeTwo.setText(hour + ":" + minute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                timePicker.setTitle("Select Time");
                timePicker.show();
            }
        });

        btnTimeThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(NewMedicineInfoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = Integer.toString(selectedHour);
                        if (hour.equals("0")) {
                            hour = "00";
                        }
                        if (hour.equals("5")) {
                            hour = "05";
                        }
                        String minute = Integer.toString(selectedMinute);
                        if (minute.equals("0")) {
                            minute = "00";
                        }
                        if (minute.equals("5")) {
                            minute = "05";
                        }
                        txtViewTimeThree.setText(hour + ":" + minute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                timePicker.setTitle("Select Time");
                timePicker.show();
            }
        });

        btnTimeFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(NewMedicineInfoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = Integer.toString(selectedHour);
                        if (hour.equals("0")) {
                            hour = "00";
                        }
                        if (hour.equals("5")) {
                            hour = "05";
                        }
                        String minute = Integer.toString(selectedMinute);
                        if (minute.equals("0")) {
                            minute = "00";
                        }
                        if (minute.equals("5")) {
                            minute = "05";
                        }
                        txtViewTimeFour.setText(hour + ":" + minute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                timePicker.setTitle("Select Time");
                timePicker.show();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                startYear = c.get(Calendar.YEAR);
                starthMonth = c.get(Calendar.MONTH);
                startDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(
                        NewMedicineInfoActivity.this, datePickerListener, startYear, starthMonth, startDay);
                datePickerDialog.show();

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAlarmsToDatabase();
            }
        });
    }

    private boolean saveAlarmsToDatabase() {
        updatePillboxDetails();
        final int quantityPerTime = 1;
        ArrayList<String> times = new ArrayList<>();
        String time1 = txtViewTimeOne.getText().toString();
        if (!time1.equals("")) {
            times.add(time1);
        }
        String time2 = txtViewTimeTwo.getText().toString();
        if (!time2.equals("")) {
            times.add(time2);
        }
        String time3 = txtViewTimeThree.getText().toString();
        if (!time3.equals("")) {
            times.add(time3);
        }
        String time4 = txtViewTimeFour.getText().toString();
        if (!time4.equals("")) {
            times.add(time4);
        }
        timesPerDay = times.size();
        String startDate = txtDate.getText().toString();
        int repeat = 0;
        if (chkDaily.isChecked()) {
            repeat = 1;
        }
        if (chkXdays.isChecked()) {
            String xDays = txtNumberOfDays.getText().toString();
            repeat = Integer.parseInt(xDays);
        }
        timesCounter = 0;
        for (String time : times) {
            AlarmEntity alarmEntity = new AlarmEntity(startDate, time, pillBoxID, quantityPerTime, repeat);
            DatabaseService dbService = new DatabaseService();
            dbService.addAlarm(alarmEntity, new CallbackResult() {
                @Override
                public void onCallback(Boolean result) {
                    timesCounter++;
                    super.onCallback(result);
                    if (timesCounter == timesPerDay) {
                        Toast.makeText(NewMedicineInfoActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                        returnHome();
                    }
                }
            });
        }
        return true;
    }

    private void updatePillboxDetails() {
        String medicineName = txtMedicineName.getText().toString();
        String stockStr = txtNoPills.getText().toString();
        int stockCount = -1;
        if (!stockStr.equals("")) {
            stockCount = Integer.parseInt(stockStr);
        }
        DatabaseService dbService = new DatabaseService();
        dbService.updatePillBoxNameAndStock(pillBoxID, medicineName, stockCount, new CallbackResult() {
            @Override
            public void onCallback(Boolean result) {
                super.onCallback(result);
            }
        });
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            startDay = selectedDay;
            starthMonth = selectedMonth;
            startYear = selectedYear;
            txtDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/"
                    + selectedYear);
        }
    };

    private void returnHome() {
        Intent intentHome = new Intent(this, HomeActivity.class);
        startActivity(intentHome);
        finish();
    }

    private void SpinnerSetListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtViewTimeOne.setVisibility(View.INVISIBLE);
                btnTimeOne.setVisibility(View.INVISIBLE);
                txtViewTimeTwo.setVisibility(View.INVISIBLE);
                btnTimeTwo.setVisibility(View.INVISIBLE);
                txtViewTimeThree.setVisibility(View.INVISIBLE);
                btnTimeThree.setVisibility(View.INVISIBLE);
                txtViewTimeFour.setVisibility(View.INVISIBLE);
                btnTimeFour.setVisibility(View.INVISIBLE);
                if (position == 0) {
                    txtViewTimeOne.setVisibility(View.VISIBLE);
                    btnTimeOne.setVisibility(View.VISIBLE);
                    txtViewTimeTwo.setText("");
                    txtViewTimeThree.setText("");
                    txtViewTimeFour.setText("");

                } else if (position == 1) {
                    txtViewTimeOne.setVisibility(View.VISIBLE);
                    btnTimeOne.setVisibility(View.VISIBLE);
                    txtViewTimeTwo.setVisibility(View.VISIBLE);
                    btnTimeTwo.setVisibility(View.VISIBLE);
                    txtViewTimeThree.setText("");
                    txtViewTimeFour.setText("");

                } else if (position == 2) {
                    txtViewTimeOne.setVisibility(View.VISIBLE);
                    btnTimeOne.setVisibility(View.VISIBLE);
                    txtViewTimeTwo.setVisibility(View.VISIBLE);
                    btnTimeTwo.setVisibility(View.VISIBLE);
                    txtViewTimeThree.setVisibility(View.VISIBLE);
                    btnTimeThree.setVisibility(View.VISIBLE);
                    txtViewTimeFour.setText("");
                } else if (position == 3) {
                    txtViewTimeOne.setVisibility(View.VISIBLE);
                    btnTimeOne.setVisibility(View.VISIBLE);
                    txtViewTimeTwo.setVisibility(View.VISIBLE);
                    btnTimeTwo.setVisibility(View.VISIBLE);
                    txtViewTimeThree.setVisibility(View.VISIBLE);
                    btnTimeThree.setVisibility(View.VISIBLE);
                    txtViewTimeFour.setVisibility(View.VISIBLE);
                    btnTimeFour.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
}
