package com.example.mp.clincdatabase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
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
    private ArrayList<Schedule> schedList;
    private Button setSchedule;
    private LinearLayout container;

    private EditText textIn;
    private String user1;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;

    private Schedule schedulesTemp;

    SwipeController swipeController = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_schedule);

        Intent intent1 = getIntent();
        user1 = intent1.getStringExtra("username");
        schedList = new ArrayList<>();
        mAdapter = new ScheduleAdapter(schedList, user1, this);

        userDataReference = databaseReference.child("Users").child(user1);
        userDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    schedList.clear();
                    for(DataSnapshot childSchedule: dataSnapshot.child("schedules").getChildren()){
                        schedulesTemp = childSchedule.getValue(Schedule.class);
                        schedList.add(schedulesTemp);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Log.i("MYACTIVITY", "Went here to the Medicine Schedule");
        mRecyclerView = (RecyclerView) findViewById(R.id.medsched_recycler_view);
        setSchedule = (Button) findViewById(R.id.add_schedule);

        setSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedicineSchedule.this, AddSchedule.class);
                intent.putExtra("username", user1);
                startActivity(intent);
            }

        });

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        /*
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                mAdapter.scheduleList.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
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
        */

    }
}
