package com.example.mp.clincdatabase;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import net.frakbot.glowpadbackport.GlowPadView;

import java.util.ArrayList;
import java.util.Calendar;


public class AlarmNotif extends AppCompatActivity {

    public static final int TARGET_SNOOZE = 0;
    public static final int TARGET_DRINK = 2;
    public static final int PENDING_NEXT_ACTIVITY=55;
    public static final int NOTIFICATION_ANNOUNCEMENT=55;
    public static final int PENDING_ALARM_RECEIVER=56;


    private PowerManager.WakeLock wl;
    //private Schedule schedule;

    private Ringtone r;
    private Vibrator v;
    private GlowPadView glowPad;
    private TextView textGlo;
    Handler handler;
    Runnable autoCloser;

    boolean isClicked;
    boolean isMilitary;

    int userRingerMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmnotif);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String physician = intent.getStringExtra("physician");
        Log.d("ALARM NOTIF", "AFTER Alarm NOtIF");
        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplicationContext(), alarmTone);

        Log.d("ALARMNOTIF:", "Pumasok sa Alarm Notif");
        AudioManager audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        glowPad = (GlowPadView) findViewById(R.id.gpv_alarm_medicine);
        textGlo = (TextView) findViewById(R.id.textGlo);
        textGlo.setText("Your" + type  + "with" + physician);
        userRingerMode = audioManager.getRingerMode();

        int volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);

        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);


        //ThemeUtil.onActivityCreateSetTheme(this);


        //ButterKnife.bind(this);

        //medicineUtil = new MedicineUtil(getBaseContext());
        //scheduleUtil = new ScheduleUtil(getBaseContext());
        //medicinePlanUtil = new MedicinePlanUtil(getBaseContext());*/

        //isMilitary = false;

        //checkIntent();
        //initializeAdapter();
        initializeContent();
        initializeContinuousPing();
        initializeTimedPresence();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

    }

    /*public void checkIntent(){
        Bundle data = getIntent().getExtras();
        schedule = data.getParcelable(Schedule.TABLE);
    }*/

    /*public void initializeAdapter(){

        ArrayList<MedicinePlan> medicinePlanList = medicinePlanUtil.getMedicinePlanListWithScheduleId(schedule.getSqlId());
        if(medicinePlanList != null) {
            int[] medicineId = new int[medicinePlanList.size()];
            for (int i = 0; i < medicinePlanList.size(); i++) {
                medicineId[i] = medicinePlanList.get(i).getMedicineId();
                Log.wtf("DRINK MEDICINE", "MEDICINE WITH ID OF " + medicineId[i]);
            }
            alarmMedicineAdapter = new AlarmMedicineAdapter(getBaseContext(), medicineUtil.getMedicine(medicineId), Medicine.COLUMN_ID);
        }else {
            alarmMedicineAdapter = new AlarmMedicineAdapter(getBaseContext(), null, Medicine.COLUMN_ID);
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        rvMedicineAlert.setAdapter(alarmMedicineAdapter);
        rvMedicineAlert.setLayoutManager(mLayoutManager);
    }*/

    public void initializeContent(){
        Log.d("Initialize Content", "PUmasok");
        playRingtone();
        playVibration();

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

//        linTextHint.setVisibility(View.GONE);

        //String displayTime = Date.format(hour, minute, isMilitary);
        //this.tvAlarmTime.setText(displayTime.split("\\s+")[0]);
        //this.tvAlarmTimePeriod.setText(displayTime.split("\\s+")[1]);

        /*if(alarmMedicineAdapter.getItemCount() == 0){
            linEmptyMedicineList.setVisibility(View.VISIBLE);
            svMedicineContainer.setVisibility(View.GONE);
        }else{
            linEmptyMedicineList.setVisibility(View.GONE);
            svMedicineContainer.setVisibility(View.VISIBLE);
        }*/

        //final int[] mults = {1, 2, 3, 4, 6, 8, 10, 12, 16, 20, 32};
        glowPad.setPointsMultiplier(12);
        glowPad.setOnTriggerListener(new GlowPadView.OnTriggerListener() {
            @Override
            public void onGrabbed(View v, int handle) {
                setClicked(true);
//                linTextHint.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReleased(View v, int handle) {
                setClicked(false);
//                linTextHint.setVisibility(View.GONE);
            }

            @Override
            public void onTrigger(View v, int target) {
                drink();
                glowPad.reset(true);
            }

            @Override
            public void onGrabbedStateChange(View v, int handle) {
                // Do nothing
            }

            @Override
            public void onFinishFinalAnimation() {
                // Do nothing
            }
        });
    }

    public void initializeContinuousPing(){
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while(true){
                        if(!isInterrupted() && !isClicked) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    glowPad.ping();
                                }
                            });
                            Thread.sleep(1500);
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    public void initializeTimedPresence(){
        handler = new Handler();
        autoCloser = new Runnable(){
            @Override
            public void run() {
                //snooze();
            }
        };
        handler.postDelayed(autoCloser, 45000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.wtf("STOP", "STOPPING DRINK MED ACT");

    }

    public void resetRingerMode(){
        AudioManager audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(userRingerMode);
    }

    public void playRingtone(){
        Log.wtf("PLAYING", "RINGTONE IS STARTING");
        /*Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtoneAlarm = RingtoneManager.getRingtone(getApplicationContext(), alarmTone);*/
        r.play();
    }

    public void playVibration(){
        v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    public boolean isClicked(){
        return isClicked;
    }
    public void setClicked(boolean bool){
        isClicked = bool;
    }

    public void drink(){
        resetRingerMode();

        //ArrayList<MedicinePlan> medicinePlanList = medicinePlanUtil.getMedicinePlanListWithScheduleId(schedule.getSqlId());
        /*if(medicinePlanList != null) {
            int[] medicineId = new int[medicinePlanList.size()];
            for (int i = 0; i < medicinePlanList.size(); i++) {
                Medicine med = medicineUtil.getMedicine(medicinePlanList.get(i).getMedicineId());
                Log.wtf("DRINK MEDICINE", "MEDICINE WITH ID OF " + medicineId[i]);
                med.drink(med.getDosage());
                medicineUtil.updateMedicine(med);
            }
        }else {
        }*/
        if(r.isPlaying()){
            r.stop();
        }
        if(v.hasVibrator()){
            v.cancel();
        }

        /*if (schedule.getDrinkingInterval() == 0) {
            schedule.setActivated(false);
            AlarmUtil.stopAssociatedAlarmsWithSchedule(getBaseContext(), schedule);
        } else {
            schedule.setNextDrinkingTime(schedule.getNextDrinkingTime() + schedule.getDrinkingInterval() * DateUtil.MILLIS_TO_MINUTES);
            AlarmUtil.setAlarmForSchedule(getBaseContext(), schedule);
        }
        scheduleUtil.updateSchedule(schedule);*/

        finish();
    }

  /*  @OnClick (R.id.snooze)
    public void snoozeClicked(){

//        Log.d("HELLO","HELLO HELLO HELLO");
//        AlarmManager alarmManager=(AlarmManager)getSystemService(Service.ALARM_SERVICE);
//
//        Intent intentAlarm= new Intent(getBaseContext(),AlarmReceiver.class);
//
//        PendingIntent pendingAlarm = PendingIntent.getBroadcast(getBaseContext()
//                ,PENDING_ALARM_RECEIVER
//                ,intentAlarm
//                ,PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME
//                , SystemClock.elapsedRealtime()+(2000)
//                , pendingAlarm
//        );
//        Log.d("HELLO","HELLO HELLO HELLO HELLO");
        snooze();
    }*/

    /*public void snooze(){
        resetRingerMode();
        if(r.isPlaying()){
            r.stop();
        }
        if(v.hasVibrator()){
            v.cancel();
        }

        Intent i = new Intent();
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.setClass(getBaseContext(), ScheduleListActivity.class);
        i.putExtra(Schedule.TABLE, schedule);
        i.putExtra(getString(R.string.schedule_snooze), 5);
        startActivity(i);
        finish();
    }*/
    @Override
    public void onBackPressed() {

    }

}

