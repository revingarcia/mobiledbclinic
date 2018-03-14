package com.example.mp.clincdatabase;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private ArrayList<Users> userArray;
    private String TAG = "MYACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userArray = new ArrayList<>();
        final TextView fffname1 = (TextView) findViewById(R.id.textView);

        Log.i("MYACTIVITY", "Went to the Main CLass");
        Button mShowRegDialog = (Button) findViewById(R.id.register);
        mShowRegDialog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });
        Button mShowLoginDialog = (Button) findViewById(R.id.login);
        mShowLoginDialog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);

            }
        });

        initValues();
    }



    public void initValues (){
        for(int k = 0; k < userArray.size(); k++) {
            Log.i(TAG, userArray.get(k).getUserID());
            Log.i(TAG, userArray.get(k).getFname());
        }
    }

}
