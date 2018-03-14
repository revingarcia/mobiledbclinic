package com.example.mp.clincdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PrescriptionList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PrescriptionAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Records> prescripList;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;
    private Users user1;
    private Records recorded;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription_views);
        prescripList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);



        Intent intent1 = getIntent();
        final String user = intent1.getStringExtra("username");
        Log.i("MYACTIVITY", "Went to the Prescription List CLass");
        user1 = new Users();
        mAdapter = new PrescriptionAdapter(prescripList, user, this);
        userDataReference = databaseReference.child("Users").child(user);
        userDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    user1 = dataSnapshot.getValue(Users.class);
                    for(DataSnapshot childRecords: dataSnapshot.child("records").getChildren()){
                        recorded = childRecords.getValue(Records.class);
                        Log.i("MYACTIVITY", recorded.getPhysician());
                        prescripList.add(recorded);

                    }

                    mAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        Log.i("MYACTIVTY", prescripList.size() + "WAHH");

    }
}
