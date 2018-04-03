package com.example.mp.clincdatabase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MedicineSchedule extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ScheduleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Alarm> schedList;
    private Button setSchedule;
    private Button backBtn;
    private CheckBox recordCheckbox;
    private CheckBox appointmentCheckbox;
    private LinearLayout container;

    private EditText textIn;
    private String user1;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;

    private Records schedulesTemp;
    private Appointment appointmentTemp;

    SwipeController swipeController = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_schedule);



        Intent intent1 = getIntent();
        user1 = intent1.getStringExtra("username");
        recordCheckbox = (CheckBox) findViewById(R.id.recordCheck);
        recordCheckbox.setChecked(true);
        appointmentCheckbox = (CheckBox) findViewById(R.id.appointmentCheck);
        appointmentCheckbox.setChecked(true);
        schedList = new ArrayList<>();
        mAdapter = new ScheduleAdapter(schedList, user1, this);

        loadDatabase();

        appointmentCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDatabase();
            }
        });

        recordCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDatabase();
            }
        });


        Log.i("MYACTIVITY", "Went here to the Medicine Schedule");
        mRecyclerView = (RecyclerView) findViewById(R.id.medsched_recycler_view);

        backBtn = (Button) findViewById(R.id.prescriptionLayout_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                mAdapter.scheduleList.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
                ArrayList<Records> recordHolder = new ArrayList<>();
                ArrayList<Appointment> appointmentHolder = new ArrayList<>();
                for(int i = 0; i < schedList.size(); i++){
                    if(schedList.get(i) instanceof Records){
                        recordHolder.add((Records)schedList.get(i));
                    }
                    else if(schedList.get(i) instanceof Appointment){
                        appointmentHolder.add((Appointment)schedList.get(i));
                    }
                }

                userDataReference = databaseReference.child("Users").child(user1);
                userDataReference.child("appointments").setValue(appointmentHolder);
                userDataReference.child("records").setValue(recordHolder);
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });


    }

    public void loadDatabase(){
        userDataReference = databaseReference.child("Users").child(user1);
        userDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    schedList.clear();
                    if(dataSnapshot.child("records").exists() && recordCheckbox.isChecked()) {
                        for (DataSnapshot childSchedule : dataSnapshot.child("records").getChildren()) {
                            schedulesTemp = childSchedule.getValue(Records.class);
                            schedList.add(schedulesTemp);
                        }
                    }
                    if(dataSnapshot.child("appointments").exists() && appointmentCheckbox.isChecked()){
                        for(DataSnapshot childAppointment: dataSnapshot.child("appointments").getChildren()){
                            appointmentTemp = childAppointment.getValue(Appointment.class);
                            schedList.add(appointmentTemp);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
