package com.example.mp.clincdatabase;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RecordView extends AppCompatActivity {

    private String user;
    private String position;
    private int pos;
    private EditText prescript_name;
    private EditText prescript_contact;
    private EditText prescript_physician;
    private TableLayout pres_tbl;

    private Records recordTemp;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_prescription);

        Intent intent1 = getIntent();
        user = intent1.getStringExtra("username");
        pos = intent1.getIntExtra("position", 0);
        position = Integer.toString(pos);

        prescript_name = (EditText) findViewById(R.id.prescript_name);
        prescript_contact = (EditText) findViewById(R.id.precrip_contact);
        prescript_physician = (EditText) findViewById(R.id.prescriped_physician);

        userDataReference = databaseReference.child("Users").child(user);
        userDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Users users1 = dataSnapshot.getValue(Users.class);
                    prescript_name.setText(users1.getFname());
                    prescript_contact.setText(users1.getContact());
                    prescript_physician.setText(users1.getRecords().get(pos).getPhysician());
                    startTable(users1.getRecords().get(pos));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    public void startTable(Records record){
        TableLayout stk = (TableLayout) findViewById(R.id.table_prescriptino);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(" Name of Medicine ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" Frequency ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" Condition ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText(" Notes ");
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.CENTER);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);
        for (int i = 0; i < record.getPrescriptions().size(); i++) {
            TableRow tbrow = new TableRow(this);
            tbrow.setWeightSum(4);
            TextView t1v = new TextView(this);
            t1v.setText(record.getPrescriptions().get(i).getName());
            t1v.setWidth(20);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
           // t1v.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText(record.getPrescriptions().get(i).getFrequency());
            t2v.setWidth(20);
            //t2v.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText(record.getPrescriptions().get(i).getConditions());
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            TextView t4v = new TextView(this);
            t4v.setText(record.getPrescriptions().get(i).getNotes());
            t4v.setTextColor(Color.WHITE);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);
            stk.addView(tbrow);
        }
    }

}
