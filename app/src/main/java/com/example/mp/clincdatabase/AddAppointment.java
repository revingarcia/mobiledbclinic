package com.example.mp.clincdatabase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;


public class AddAppointment extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;
    private EditText apptDate;
    private EditText appTime;
    private EditText appPhysician;
    private Button btnAddSched;
    private String user1;
    private Appointment schedules;
    private ArrayList<Appointment> scheduleList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointmentsched_list);

        Intent intent1 = getIntent();
        user1 = intent1.getStringExtra("username");
        scheduleList = new ArrayList<>();
        userDataReference = databaseReference.child("Users").child(user1);
        userDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Appointment> appointmentTemp = new ArrayList<>();
                if(dataSnapshot.exists()){
                    for(DataSnapshot childSchedule: dataSnapshot.child("appointments").getChildren()){
                        schedules = childSchedule.getValue(Appointment.class);
                        appointmentTemp.add(schedules);
                        Log.i("MYACTIVITY",schedules.getPhysician());
                    }
                    scheduleList = appointmentTemp;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        apptDate = (EditText) findViewById(R.id.apptDate);
        appTime = (EditText) findViewById(R.id.apptTime);
        appPhysician = (EditText) findViewById(R.id.apptPhysciain);
        btnAddSched = (Button) findViewById(R.id.btnAddAppt);
        btnAddSched.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Appointment schedTemp = new Appointment(appPhysician.getText().toString(), apptDate.getText().toString(), appTime.getText().toString());
                scheduleList.add(schedTemp);
                userDataReference.child("appointments").setValue(scheduleList);
                finish();
            }
        });
    }
}
