package com.example.mp.clincdatabase;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


public class MenuPrescriptions extends AppCompatActivity{

    private Button btnRecordPres;
    private Button btnViewPres;

    private ArrayList<Records> recordTempList;
    private Records recordTemp;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_prescriptions);

        Intent intent1 = getIntent();
        final String user = intent1.getStringExtra("username");
        Log.i("MYACTIVITY", "Went to the Prescription menu CLass");
        recordTempList = new ArrayList<>();
        recordTemp = new Records();
        btnRecordPres = (Button) findViewById(R.id.record_prescription);
        btnRecordPres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MenuPrescriptions.this);
                View dialogView = getLayoutInflater().inflate(R.layout.add_record, null);
                final EditText addPhysician = (EditText) dialogView.findViewById(R.id.recordPhysician);
                final Button record = (Button) dialogView.findViewById(R.id.btnAddRecord);

                mBuilder.setView(dialogView);

                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MenuPrescriptions.this, "Added a record", Toast.LENGTH_SHORT).show();
                        recordTempList.clear();
                        if(!addPhysician.getText().toString().isEmpty()){
                            recordTemp.setPhysician(addPhysician.getText().toString());
                            recordTempList.add(recordTemp);

                            userDataReference = databaseReference.child("Users").child(user).child("records");
                            userDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        Records recordTempo;
                                        for(DataSnapshot childRecord: dataSnapshot.getChildren()){
                                            recordTempo = childRecord.getValue(Records.class);
                                            recordTempList.add(recordTempo);
                                        }
;
                                        userDataReference.setValue(recordTempList);

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
                            Toast.makeText(MenuPrescriptions.this, "Please input a Physician", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }
        });
        btnViewPres = (Button) findViewById(R.id.view_prescription);
        btnViewPres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userDataReference = databaseReference.child("Users").child(user);
                userDataReference.child("records").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Log.i("MYACTIVITY", "I EXIST");

                            Intent intent = new Intent(MenuPrescriptions.this, PrescriptionList.class);
                            intent.putExtra("username", user);
                            startActivity(intent);

                        }
                        else{
                            Log.i("MYACTIVTY", "I DONT EXIST");
                            Toast.makeText(MenuPrescriptions.this, "No Record has been establish, please make one", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });
    }
}
