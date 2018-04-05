package com.example.mp.clincdatabase;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;


public class AddAppointment extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;
    private EditText appPhysician;
    private Button btnAddSched;
    private EditText inputDate;
    private EditText inputTime;
    private Button btnBack;
    private String user1;
    private Appointment schedules;
    private ArrayList<Appointment> scheduleList;
    private Alarm alarmTemp;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointmentsched_list);
        getSupportActionBar().setTitle("Make Appointment");

        Intent intent1 = getIntent();
        user1 = intent1.getStringExtra("username");
        scheduleList = new ArrayList<>();
        userDataReference = databaseReference.child("Users").child(user1);
        userDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Appointment> appointmentTemp = new ArrayList<>();
                scheduleList.clear();
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("appointments").exists()) {
                        for (DataSnapshot childSchedule : dataSnapshot.child("appointments").getChildren()) {
                            schedules = childSchedule.getValue(Appointment.class);
                            appointmentTemp.add(schedules);
                            Log.i("MYACTIVITY", schedules.getPhysician());
                        }
                        scheduleList = appointmentTemp;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        appPhysician = (EditText) findViewById(R.id.apptPhysician);
        btnAddSched = (Button) findViewById(R.id.btnAddAppt);
        btnBack = (Button) findViewById(R.id.backBtn);
        inputDate = (EditText) findViewById(R.id.dateInputAppointment);
        inputTime = (EditText) findViewById(R.id.timeInputAppointment);

        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(AddAppointment.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        inputDate.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        inputTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddAppointment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        inputTime.setText( ""+selectedHour + ":" + selectedMinute);
                        alarmTemp = new Alarm();
                        alarmTemp.setOn(true);
                        alarmTemp.setMinutes(selectedMinute);
                        alarmTemp.setHour(selectedHour);
                        if(selectedHour < 12)
                            alarmTemp.setAm_pm("AM");
                        else
                            alarmTemp.setAm_pm("PM");
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddSched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inputDate.getText().toString().isEmpty() && !inputTime.getText().toString().isEmpty() && !appPhysician.getText().toString().isEmpty()) {
                    String[] date = inputDate.getText().toString().split("/");
                    Appointment appointmentTemp = new Appointment(appPhysician.getText().toString(), Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
                    appointmentTemp.setAlarmData(alarmTemp);
                    scheduleList.add(appointmentTemp);
                    userDataReference.child("appointments").setValue(scheduleList);
                    finish();
                }
                else
                    Toast.makeText(AddAppointment.this, "Please fill in the blank, do not leave anything blank", Toast.LENGTH_SHORT).show();
            }
        });
        /*
        btnAddSched.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Appointment schedTemp = new Appointment(appPhysician.getText().toString(), apptDate.getText().toString(), appTime.getText().toString());
                scheduleList.add(schedTemp);
                userDataReference.child("appointments").setValue(scheduleList);
                finish();
            }
        });
        */
    }
}
