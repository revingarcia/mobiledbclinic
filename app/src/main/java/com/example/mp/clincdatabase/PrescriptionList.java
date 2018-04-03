package com.example.mp.clincdatabase;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private Button backBtn;
    private Button createBtn;
    private ArrayList<Records> recordTempList;
    private Records recordTemp;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription_views);
        backBtn = (Button) findViewById(R.id.prescriptionLayout_back);
        createBtn = (Button) findViewById(R.id.prescriptionLayout_create);
        prescripList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recordTempList = new ArrayList<>();
        recordTemp = new Records();
        Intent intent1 = getIntent();
        final String user = intent1.getStringExtra("username");
        Log.i("MYACTIVITY", "Went to the Prescription List CLass");
        user1 = new Users();
        mAdapter = new PrescriptionAdapter(prescripList, user, this);
        userDataReference = databaseReference.child("Users").child(user).child("records"); //username ng user (the unique value)
        userDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    prescripList.clear();
                    for(DataSnapshot childRecords: dataSnapshot.getChildren()){
                        recorded = childRecords.getValue(Records.class); // per Record
                        Log.i("MYACTIVITY", recorded.getPhysician());
                        prescripList.add(recorded);

                    }

                    mAdapter.notifyDataSetChanged();

                }
                else{
                    Log.i("MYACTIVTY", "I DONT EXIST");
                    Toast.makeText(PrescriptionList.this, "No Record has been establish, please make one", Toast.LENGTH_LONG).show();
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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addRecordIntent = new Intent(PrescriptionList.this, PrescriptionAlarmAdd.class);
                addRecordIntent.putExtra("username", user);
                startActivity(addRecordIntent);


                /*
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PrescriptionList.this);
                View dialogView = getLayoutInflater().inflate(R.layout.add_record, null);
                final EditText addPhysician = (EditText) dialogView.findViewById(R.id.recordPhysician);
                final Button record = (Button) dialogView.findViewById(R.id.btnAddRecord);

                mBuilder.setView(dialogView);

                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(PrescriptionList.this, "Added a record", Toast.LENGTH_SHORT).show();
                        prescripList.clear();
                        if(!addPhysician.getText().toString().isEmpty()){ // if the text is not empty
                            recordTemp.setPhysician(addPhysician.getText().toString());
                            recordTempList.add(recordTemp);

                            userDataReference = databaseReference.child("Users").child(user).child("records");
                            userDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){ //if a child node/key "records" already exist
                                        Records recordTempo;
                                        for(DataSnapshot childRecord: dataSnapshot.getChildren()){ //add all the records from the firebase to the arraylist
                                            recordTempo = childRecord.getValue(Records.class);
                                            recordTempList.add(recordTempo);
                                        }
                                        ;
                                        userDataReference.setValue(recordTempList); // then update the firebase

                                    }
                                    else{
                                        userDataReference.setValue(recordTempList);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(PrescriptionList.this, "Please input a Physician", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                */
            }
        });

    }
}
