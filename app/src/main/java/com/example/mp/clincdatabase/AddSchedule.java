package com.example.mp.clincdatabase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;


public class AddSchedule extends AppCompatActivity{

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;
    private EditText schedDate;
    private LinearLayout container;
    private EditText textin;
    private Button buttonAdd;
    private Button buttonremove;
    private Button btnAddSched;
    private String user1;
    private Schedule schedules;
    private ArrayList<Schedule> scheduleList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medsched_list);

        Intent intent1 = getIntent();
        user1 = intent1.getStringExtra("username");
        scheduleList = new ArrayList<>();
        userDataReference = databaseReference.child("Users").child(user1);
        userDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot childSchedule: dataSnapshot.child("schedules").getChildren()){
                        schedules = childSchedule.getValue(Schedule.class);
                        scheduleList.add(schedules);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        schedDate = (EditText) findViewById(R.id.schedDate);
        textin = (EditText) findViewById(R.id.textin);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        container = (LinearLayout) findViewById(R.id.container);
        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.row, null);
                EditText textOut = (EditText) addView.findViewById(R.id.textout);
                // textOut.setAdapter(adapter);
                textOut.setText(textin.getText().toString());
                Button buttonRemove = (Button)addView.findViewById(R.id.remove);

                final View.OnClickListener thisListener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout)addView.getParent()).removeView(addView);
                    }
                };

                buttonRemove.setOnClickListener(thisListener);
                container.addView(addView);
            }
        });
        btnAddSched = (Button) findViewById(R.id.btnAddSched);
        btnAddSched.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                ArrayList<String> prescriptionTemp = new ArrayList<>();

                int childCount = container.getChildCount();
                for(int i = 0; i < childCount; i++){
                    View thisChild = container.getChildAt(i);
                    EditText childtextView = (EditText)  thisChild.findViewById(R.id.textout);
                    String prescriptionTexts = childtextView.getText().toString();
                    prescriptionTemp.add(prescriptionTexts);
                }

                Schedule schedTemp = new Schedule(schedDate.getText().toString(), prescriptionTemp);
                scheduleList.add(schedTemp);
                userDataReference.child("schedules").setValue(scheduleList);
                finish();
            }
        });
    }
}
