package com.example.mp.clincdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by waboy on 4/5/2018.
 */

public class ProfileEdit extends AppCompatActivity{

    private String user;
    private EditText profileName, placeHolderUserName, placeHolderBirthday, placeHolderContact;
    private LinearLayout allegriesLayout, pharmaceuticalLayout;
    private Button backBtn, editBtn;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;
    private Users userTemp;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        Intent intent = getIntent();
        user = intent.getStringExtra("username");

        profileName = (EditText) findViewById(R.id.profileName);
        placeHolderUserName = (EditText) findViewById(R.id.placeHolderName);
        placeHolderBirthday = (EditText) findViewById(R.id.editText2);
        placeHolderContact = (EditText) findViewById(R.id.editText);
        allegriesLayout = (LinearLayout) findViewById(R.id.allergiesLayout);
        pharmaceuticalLayout = (LinearLayout) findViewById(R.id.prescribedLayout);
        backBtn = (Button) findViewById(R.id.backBtn2);

        userDataReference = databaseReference.child("Users").child(user);
        userDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userTemp = dataSnapshot.getValue(Users.class);
                profileName.setText(userTemp.getFname());
                placeHolderBirthday.setText(userTemp.getBirthday());
                placeHolderContact.setText(userTemp.getContact());
                placeHolderUserName.setText(userTemp.getUsername());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editBtn = (Button) findViewById(R.id.btnEdit2);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder editBuilder = new AlertDialog.Builder(ProfileEdit.this);
                View editView = LayoutInflater.from(ProfileEdit.this).inflate(R.layout.password_validation, null);
                final Button btnNo = (Button) editView.findViewById(R.id.btnNo);
                final Button btnYes = (Button) editView.findViewById(R.id.btnYes);
                final EditText pass = (EditText) editView.findViewById(R.id.passwordPlace);

                editBuilder.setView(editView);

                final AlertDialog dialog = editBuilder.create();
                dialog.show();
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!pass.getText().toString().isEmpty()){
                                    if(userTemp.getPassword().equalsIgnoreCase(pass.getText().toString())){
                                        userTemp.setContact(placeHolderContact.getText().toString());
                                        userTemp.setBirthday(placeHolderBirthday.getText().toString());
                                        userTemp.setFname(profileName.getText().toString());
                                        userTemp.setUsername(placeHolderUserName.getText().toString());
                                        userDataReference.setValue(userTemp);
                                        dialog.dismiss();

                                    }
                                    else
                                        Toast.makeText(ProfileEdit.this, "The password is Incorrect", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(ProfileEdit.this, "The password field is empty", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}
