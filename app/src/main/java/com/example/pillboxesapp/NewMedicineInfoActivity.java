package com.example.pillboxesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewMedicineInfoActivity extends AppCompatActivity {
    private Spinner spinner;
    private TextView txtViewTimeOne,txtViewTimeTwo,txtViewTimeThree,txtViewTimeFour, txtDate;
    private Button btnTimeOne,btnTimeTwo,btnTimeThree,btnTimeFour, btnSave, btnDate;
    private EditText txtMedicineName, txtNoPills, txtNumberOfDays;
    private Integer[] numbers = new Integer[]{1,2,3,4};
    private TimePickerDialog timePicker;
    private int hour = 0;
    private int minute = 0;
    private CheckBox chkDaily, chkXdays, chkNever;
    private DatePickerDialog datePickerDialog;
    private int startYear, starthMonth, startDay;
    private Date datePicked;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine_info);
        spinner = findViewById(R.id.spinner2);
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
        ButtonSetListeners();

    }

    private void ButtonSetListeners() {

        btnTimeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(NewMedicineInfoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txtViewTimeOne.setText( selectedHour + ":" + selectedMinute);
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
                        txtViewTimeTwo.setText( selectedHour + ":" + selectedMinute);
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
                        txtViewTimeThree.setText( selectedHour + ":" + selectedMinute);
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
                        txtViewTimeFour.setText( selectedHour + ":" + selectedMinute);
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
        /*
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            @Override
            public void onClick(View v) {
                //do validtion
                String medicineName = txtMedicineName.getText().toString();
                int numberOfPills = Integer.valueOf(txtNoPills.getText().toString());
                String time1 = txtViewTimeOne.getText().toString();
                String time2 = txtViewTimeTwo.getText().toString();
                String time3 = txtViewTimeThree.getText().toString();
                String time4 = txtViewTimeFour.getText().toString();
                int numberOfPillsPerDay = 0;
                if(!time1.equals("")){
                    numberOfPillsPerDay++;
                }
                if(!time2.equals("")){
                    numberOfPillsPerDay++;
                } if(!time3.equals("")){
                    numberOfPillsPerDay++;
                } if(!time4.equals("")){
                    numberOfPillsPerDay++;
                }
                int totalNumberOfDays = numberOfPills / numberOfPillsPerDay;


                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                //SVISTO
                Calendar cal2 = Calendar.getInstance();
                Date date = new Date();
                try {
                    date = dateFormat.parse(txtDate.getText().toString());
                } catch (ParseException e) {

                }
                //SVISTO



                date = cal2.getTime();
                long time = date.getTime();
                Timestamp timestamp = new Timestamp(time);
                int frequency = 1;
                if(chkDaily.isEnabled()){
                    frequency = numberOfPills;
                }else if(chkNever.isEnabled()){
                    frequency = 0;
                }else if(chkXdays.isEnabled()){
               if(txtNumberOfDays.getText().toString().equals("")){
                   ///Write Toast message san to lathos password
                   return;
               } else{
                   frequency = Integer.valueOf(txtNumberOfDays.getText().toString());
               }
                }
                Timestamp[] dateArrays = new Timestamp[totalNumberOfDays];
                Date date1 = new Date (timestamp.getTime());
                for (int i=0; i<totalNumberOfDays; i++){
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date1);
                    cal.add(Calendar.DATE, frequency);
                    date1 = cal.getTime();
                    dateArrays[i] = new Timestamp(date1.getTime());
                }

                mDatabase.child("medicines").setValue("a");
           //     mDatabase.




                }
        }); */

    }
        private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                startDay = selectedDay;
                starthMonth = selectedMonth;
                startYear = selectedYear;
                txtDate.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                        + selectedYear);
            }
        };
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
                if(position == 0){
                    txtViewTimeOne.setVisibility(View.VISIBLE);
                    btnTimeOne.setVisibility(View.VISIBLE);
                    txtViewTimeTwo.setText("");
                    txtViewTimeThree.setText("");
                    txtViewTimeFour.setText("");

                }
                else if(position ==1){
                    txtViewTimeOne.setVisibility(View.VISIBLE);
                    btnTimeOne.setVisibility(View.VISIBLE);
                    txtViewTimeTwo.setVisibility(View.VISIBLE);
                    btnTimeTwo.setVisibility(View.VISIBLE);
                    txtViewTimeThree.setText("");
                    txtViewTimeFour.setText("");

                }else if(position ==2){
                    txtViewTimeOne.setVisibility(View.VISIBLE);
                    btnTimeOne.setVisibility(View.VISIBLE);
                    txtViewTimeTwo.setVisibility(View.VISIBLE);
                    btnTimeTwo.setVisibility(View.VISIBLE);
                    txtViewTimeThree.setVisibility(View.VISIBLE);
                    btnTimeThree.setVisibility(View.VISIBLE);
                    txtViewTimeFour.setText("");
                }else if(position ==3){
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
