package com.example.mp.clincdatabase;

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


public class Login extends AppCompatActivity{

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;
    private EditText loginUser;
    private EditText loginPass;

    private Button btnLogin;
    private String TAG = "MYACTIVITY";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);

        loginUser = (EditText) findViewById(R.id.loginUser);
        loginPass = (EditText) findViewById(R.id.loginPass);
        Log.i("MYACTIVITY", "Went to the Login CLass");
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!loginUser.getText().toString().isEmpty() && !loginPass.getText().toString().isEmpty() ){
                    userDataReference = databaseReference.child("Users");
                    userDataReference.orderByChild("username").equalTo(loginUser.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                Log.i("DATAA WAS PRESENT", "went in here" + loginUser.getText().toString());
                                Toast.makeText(Login.this, "The Login and Password does not match", Toast.LENGTH_SHORT);
                                userDataReference = databaseReference.child("Users");
                                userDataReference.orderByChild("password").equalTo(loginPass.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()) {
                                           // Log.i("DATAA WAS PASSWARD", "went in here" + loginPass.getText().toString(), Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(Login.this, Menu.class);
                                            String user = loginUser.getText().toString();
                                            intent.putExtra("username", user);
                                            startActivity(intent);

                                        }
                                        else{
                                            Log.i("DATAA WAS PASSWARD", "MALI WALANG MATCH");
                                            Toast.makeText(Login.this, "The Login and Password does not match", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.i("ERRORRR NO PASSWARD", "MALI MALI MALI" + loginPass.getText().toString());
                                    }
                                });

                            }
                            else
                                Log.i("DATAA WAS PRESENT", "MALI WALANG MATCH");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.i("ERRORRR NO DATA PRESENT", "MALI MALI MALI" + loginUser.getText().toString());
                        }
                    });
                }
                else{
                    Toast.makeText(Login.this,
                            "Please fill in the blanks", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
