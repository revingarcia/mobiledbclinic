package com.example.mp.clincdatabase;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class Menu extends AppCompatActivity {


    private Button btnPrescriptions;
    private Button btnIntakeSched;
    private Button btnDoctors;
    private Button btnNotif;
    private ImageView imageSchedule;
    private ImageView imageAppointment;
    private ImageView imagePresciption;
    private ConstraintLayout layoutSchedule;
    private ConstraintLayout layoutPrescription;
    private ConstraintLayout layoutAppiontment;
    private ConstraintLayout layoutProfile;
    private TextView menuTitle;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        getSupportActionBar().setTitle("Main Menu");

        Intent intent1 = getIntent();
        final String user = intent1.getStringExtra("username");
        Log.i("MYACTIVITY", "Went to the Menu CLass");
        menuTitle = (TextView) findViewById(R.id.menuTitle);
        Typeface tf = Typeface.createFromAsset(getAssets(), "font1.ttf");
        menuTitle.setTypeface(tf);
        btnPrescriptions = (Button) findViewById(R.id.btnPrescription);
        imagePresciption = (ImageView) findViewById(R.id.imagePrescription);
        layoutPrescription = (ConstraintLayout) findViewById(R.id.layoutPrescriptions);
        layoutPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, PrescriptionList.class);
                intent.putExtra("username", user);
                startActivity(intent);

            }
        });
        btnDoctors = (Button) findViewById(R.id.btnIntakeSched);
        imageAppointment = (ImageView) findViewById(R.id.imageViewSched);
        layoutSchedule = (ConstraintLayout) findViewById(R.id.layoutSchedule);
        layoutSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, MedicineSchedule.class);
                intent.putExtra("username", user);
                startActivity(intent);
            }
        });
        btnIntakeSched = (Button) findViewById(R.id.btnDoctors);
        imageAppointment = (ImageView) findViewById( R.id.imgappointment);
        layoutAppiontment = (ConstraintLayout) findViewById(R.id.layoutAppintment);
        layoutAppiontment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, AppointmentList.class);
                intent.putExtra("username", user);
                startActivity(intent);
            }
        });

        layoutProfile = (ConstraintLayout) findViewById(R.id.layoutProfile);
        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, ProfileEdit.class);
                intent.putExtra("username", user);
                startActivity(intent);
            }
        });
        /*
        btnNotif = (Button) findViewById(R.id.btnNotif);
        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "Functino is still not available", Toast.LENGTH_SHORT).show();
            }
        });
        */

    }
}
