package com.example.mp.clincdatabase;

import android.content.Intent;
import android.icu.text.AlphabeticIndex;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by waboy on 4/2/2018.
 */

public class PrescriptionAlarmAdd extends AppCompatActivity {

    private TimePicker timepicker;
    private Button setAlarm;
    private EditText physicianName;
    private RelativeLayout setRepeat;
    private TextView daysRepeat;
    private String username;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;
    private ArrayList<Records> recordListTem = new ArrayList<>();
    private String frequencyTemp;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription_alarm);
        Intent intent1 = getIntent();
        final Records prescriptionsTemp = new Records();
        frequencyTemp  ="Daily";
        prescriptionsTemp.setFrequency(frequencyTemp);
        username = intent1.getStringExtra("username");
        timepicker = (TimePicker) findViewById(R.id.timePicker);
        setRepeat = (RelativeLayout) findViewById(R.id.row_repeat);
        daysRepeat = (TextView) findViewById(R.id.alarm_repeat);
        daysRepeat.setText(frequencyTemp);
        physicianName = (EditText) findViewById(R.id.record_physician);
        setAlarm = (Button) findViewById(R.id.set_alarm_prescription);
        setRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PrescriptionAlarmAdd.this, "THE ONCLICK WORDS",Toast.LENGTH_LONG).show();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PrescriptionAlarmAdd.this);
                View dialogView = LayoutInflater.from(PrescriptionAlarmAdd.this).inflate(R.layout.prescription_alarm_days, null);
                final CheckBox sundayBox = (CheckBox) dialogView.findViewById(R.id.sundayBox);
                final CheckBox mondayBox = (CheckBox) dialogView.findViewById(R.id.mondayBox);
                final CheckBox tuesdayBox = (CheckBox) dialogView.findViewById(R.id.tuesdayBox);
                final CheckBox wednesdayBox = (CheckBox) dialogView.findViewById(R.id.wednesdayBox);
                final CheckBox thursdayBox = (CheckBox) dialogView.findViewById(R.id.thursdayBox);
                final CheckBox fridayBox = (CheckBox) dialogView.findViewById(R.id.fridayBox);
                final CheckBox saturdayBox = (CheckBox) dialogView.findViewById(R.id.saturdayBox);
                Button setRepeatBtn = (Button) dialogView.findViewById(R.id.alarm_set_repeat);
                mBuilder.setView(dialogView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                setRepeatBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Log.d("Check", String.valueOf(sundayBox.isChecked()));
                        Toast.makeText(PrescriptionAlarmAdd.this, "box SunStatus"+String.valueOf(sundayBox.isChecked()),Toast.LENGTH_LONG).show();
                        int i = 0;
                        Alarm alarmtemp = new Alarm();
                        if(sundayBox.isChecked()){
                            Log.i("Check", "Sunday is checked");
                            alarmtemp.getDays().add("Sunday");
                            i++;
                        }
                        if(mondayBox.isChecked()){
                            Log.i("Check", "Monday is checked");
                            alarmtemp.getDays().add("Monday");
                            i++;
                        }
                        if(tuesdayBox.isChecked()){
                            Log.i("Check", "Tuesday is checked");
                            alarmtemp.getDays().add("Tuesday");
                            i++;
                        }
                        if(wednesdayBox.isChecked()){
                            Log.i("Check", "Wednesday is checked");
                            alarmtemp.getDays().add("Wednesday");
                            i++;
                        }
                        if(thursdayBox.isChecked()){
                            Log.i("Check", "Thursday is checked");
                            alarmtemp.getDays().add("Thursday");
                            i++;
                        }
                        if(fridayBox.isChecked()){
                            Log.i("Check", "Friday is checked");
                            alarmtemp.getDays().add("Friday");
                            i++;
                        }
                        if(saturdayBox.isChecked()){
                            Log.i("Check", "Saturday is checked");
                            alarmtemp.getDays().add("Saturday");
                            i++;
                        }
                        int hour = timepicker.getCurrentHour();
                        int minute = timepicker.getCurrentMinute();
                        if(hour < 12)
                            alarmtemp.setAm_pm("AM");
                        else if(hour == 12)
                            alarmtemp.setAm_pm("NN");
                        else
                            alarmtemp.setAm_pm("PM");
                        alarmtemp.setHour(hour);
                        alarmtemp.setMinutes(minute);
                        alarmtemp.setOn(true);
                        prescriptionsTemp.setAlarmData(alarmtemp);
                        if(i == 0){
                            prescriptionsTemp.setFrequency("Once");
                        }
                        else {
                            frequencyTemp = "";
                            String repeatTemp = "";
                            for (int j = 0; j < prescriptionsTemp.getDays().size(); j++) {
                                if(prescriptionsTemp.getDays().get(j).toString().equalsIgnoreCase("Sunday")) {
                                    frequencyTemp = frequencyTemp + "Sun ";
                                    repeatTemp = repeatTemp + "Sun ";
                                }
                                else if(prescriptionsTemp.getDays().get(j).toString().equalsIgnoreCase("Monday")) {
                                    frequencyTemp = frequencyTemp + "Mon ";
                                    repeatTemp = repeatTemp + "M ";
                                }
                                else if(prescriptionsTemp.getDays().get(j).toString().equalsIgnoreCase("Tuesday")) {
                                    frequencyTemp = frequencyTemp + "Tue ";
                                    repeatTemp = repeatTemp + "T ";
                                }
                                else if(prescriptionsTemp.getDays().get(j).toString().equalsIgnoreCase("Wednesday")) {
                                    frequencyTemp = frequencyTemp + "Wed ";
                                    repeatTemp = repeatTemp + "T ";
                                }
                                else if(prescriptionsTemp.getDays().get(j).toString().equalsIgnoreCase("Thursday")) {
                                    frequencyTemp = frequencyTemp + "Thu ";
                                    repeatTemp = repeatTemp + "Th ";
                                }
                                else if(prescriptionsTemp.getDays().get(j).toString().equalsIgnoreCase("Friday")) {
                                    frequencyTemp = frequencyTemp + "Fri ";
                                    repeatTemp = repeatTemp + "F ";
                                }
                                else if(prescriptionsTemp.getDays().get(j).toString().equalsIgnoreCase("Saturday")) {
                                    frequencyTemp = frequencyTemp + "Sat ";
                                    repeatTemp = repeatTemp + "Sat ";
                                }
                            }
                            prescriptionsTemp.setFrequency(repeatTemp);
                            daysRepeat.setText(frequencyTemp);

                        }

                        userDataReference = databaseReference.child("Users").child(username).child("records");
                        userDataReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    Records recordtemp;
                                    recordListTem.clear();
                                    for(DataSnapshot childRecords: dataSnapshot.getChildren()){
                                        recordtemp = childRecords.getValue(Records.class);
                                        recordListTem.add(recordtemp);
                                    }

                                }
                                else{

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Log.i("time", "Currenet time:" + hour + "minute:" + minute);
                        dialog.dismiss();
                    }

                });

            }
        });
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!physicianName.getText().toString().isEmpty()) {
                    prescriptionsTemp.setPhysician(physicianName.getText().toString());
                    recordListTem.add(prescriptionsTemp);
                    userDataReference = databaseReference.child("Users").child(username).child("records");
                    userDataReference.setValue(recordListTem);
                    finish();
                }
                else
                    Toast.makeText(PrescriptionAlarmAdd.this, "Please do not leave Physician Blank", Toast.LENGTH_LONG).show();
            }
        });

    }
}
