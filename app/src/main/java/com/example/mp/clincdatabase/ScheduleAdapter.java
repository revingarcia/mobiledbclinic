package com.example.mp.clincdatabase;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {


    public ArrayList<Alarm> scheduleList;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;

    private static final int ALARM_REQUEST_CODE = 133;

    private PendingIntent pendingIntent;
    private Context context;
    private String user;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView alarmType;
        private TextView alarmTimeDate;
        private TextView alarmAMPM;
        private TextView alarmPhysician;
        private Switch isonCheck;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTexts(String type, String timedate, String ampm, String physician, Boolean isOn){
            alarmType = (TextView) mView.findViewById(R.id.tv_type_alarm);
            alarmTimeDate = (TextView) mView.findViewById(R.id.tv_alarmTime);
            alarmAMPM = (TextView) mView.findViewById(R.id.tv_ampm);
            alarmPhysician = (TextView) mView.findViewById(R.id.tv_physician);
            isonCheck = (Switch) mView.findViewById(R.id.imageArrow);
            isonCheck.setChecked(isOn);
            alarmType.setText(type);
            alarmAMPM.setText(ampm);
            alarmTimeDate.setText(timedate);
            alarmPhysician.setText(physician);

            isonCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Check", "PUMAPASOK" +  isonCheck.isChecked());
                    if(isonCheck.isChecked()) {
                        scheduleList.get(getAdapterPosition()).setOn(true);


                        int hourOfUser = scheduleList.get(getAdapterPosition()).getHour();
                        int minuteOfUser = scheduleList.get(getAdapterPosition()).getMinutes();
                        Calendar calendar = Calendar.getInstance();
                        int hourOfPhone = calendar.get(Calendar.HOUR_OF_DAY); //24hr format
                        int minOfPhone = calendar.get(Calendar.MINUTE);
                        int seconds = calendar.get(Calendar.SECOND);

                        Log.d("Current Time", String.valueOf(calendar.getTime()));
                        Log.d("Current Time", String.valueOf(hourOfPhone));
                        Log.d("CHECK HOUR USER: ", String.valueOf(hourOfUser));
                        Log.d("CHECK MINUTE U: ", String.valueOf(minuteOfUser));
                                     /* Retrieve a PendingIntent that will perform a broadcast */
                        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                        if(scheduleList.get(getAdapterPosition()) instanceof Records)
                            alarmIntent.putExtra("type", "Record");
                        else
                            alarmIntent.putExtra("type", "Appointment");
                        pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, alarmIntent, 0);
                        int finalhour;
                        int finalminute;
                        int mintosec; //seconds
                        int hourtosec; //seconds
                        //int mintomilli = mintosec * 1000;
                        //int hourtomilli = hourtosec * 1000;
                        int finaltime;

                        if (hourOfUser > hourOfPhone) {
                            if (minuteOfUser > minOfPhone) {// user 14:19 phone 8:15
                                finalhour = hourOfUser - hourOfPhone;
                                finalminute = Math.abs(minuteOfUser - minOfPhone);
                                mintosec = finalminute * 60; //seconds
                                hourtosec = finalhour * 60 * 60; //seconds
                                finaltime = mintosec + hourtosec - seconds;
                            } else if (minuteOfUser == minOfPhone) { //User 14:55 phone 11:55
                                finalhour = hourOfUser - hourOfPhone;
                                hourtosec = finalhour * 60 * 60; //seconds
                                finaltime = hourtosec - seconds;

                            } else { //TODAY //user 14:15 phone 8:19
                                finalhour = hourOfUser - hourOfPhone - 1;
                                finalminute = 60 - (minOfPhone - minuteOfUser);
                                mintosec = finalminute * 60; //seconds
                                hourtosec = finalhour * 60 * 60; //seconds
                                finaltime = mintosec + hourtosec - seconds;
                            }

                        } else if (hourOfPhone > hourOfUser) { //mas mababa si hour of user kesa current ng phone
                            if (minuteOfUser > minOfPhone) { //TOMORROW phone 22:15 user 18:19
                                finalhour = hourOfPhone - hourOfUser;
                                finalminute = Math.abs(minuteOfUser - minOfPhone);
                                mintosec = finalminute * 60; //seconds
                                hourtosec = (24 - finalhour) * 60 * 60; //seconds
                                finaltime = mintosec + hourtosec - seconds;
                            } else if (minuteOfUser == minOfPhone) { //phone 22:55 user 21:55
                                finalhour = hourOfPhone - hourOfUser;
                                hourtosec = (24 - finalhour) * 60 * 60; //seconds
                                finaltime = hourtosec - seconds;

                            } else { //min of phone > minuser
                                finalhour = hourOfPhone - hourOfUser; //phone 22:15 user 18:10
                                finalminute = 60 - (minOfPhone - minuteOfUser);
                                mintosec = finalminute * 60; //seconds
                                hourtosec = (24 - finalhour - 1) * 60 * 60; //seconds
                                finaltime = mintosec + hourtosec - seconds;
                            }
                        } else {
                            if (minOfPhone > minuteOfUser) { //phone 11:45 user 11:10
                                finalminute = 60 - (minOfPhone - minuteOfUser);
                                mintosec = finalminute * 60; //seconds
                                hourtosec = 23 * 60 * 60; //seconds
                                finaltime = mintosec + hourtosec - seconds;

                            } else if (minuteOfUser > minOfPhone) { //user 11:10 phone 11:08
                                finalminute = minuteOfUser - minOfPhone;
                                mintosec = finalminute * 60; //seconds
                                finaltime = mintosec - seconds;
                            } else {
                                finaltime = 0;
                            }

                        }
                        triggerAlarmManager(finaltime);
                    }
                    else
                        scheduleList.get(getAdapterPosition()).setOn(false);
                    ArrayList<Records> recordHolder = new ArrayList<>();
                    ArrayList<Appointment> appointmentHolder = new ArrayList<>();
                    for(int i = 0; i < scheduleList.size(); i++){
                        if(scheduleList.get(i) instanceof Records){
                            recordHolder.add((Records)scheduleList.get(i));
                        }
                        else if(scheduleList.get(i) instanceof Appointment){
                            appointmentHolder.add((Appointment)scheduleList.get(i));
                        }
                    }

                    userDataReference = databaseReference.child("Users").child(user);
                    userDataReference.child("appointments").setValue(appointmentHolder);
                    userDataReference.child("records").setValue(recordHolder);

                }
            });
        }
        public void checkToggle(){

        }
    }

    public ScheduleAdapter(ArrayList<Alarm> scheduleList, String user, Context context){
        this.scheduleList = scheduleList;
        this.user = user;
        this.context = context;
    }

    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_listrow, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleAdapter.ViewHolder holder, final int position) {
        if(scheduleList.get(position) instanceof Records) {
            //holder.setTexts(,scheduleList.get(position).getDate(), scheduleList.get(position).getPrescriptions());
            if(scheduleList.get(position).getAm_pm().equalsIgnoreCase("PM"))
                holder.setTexts("Records", (scheduleList.get(position).getHour()-12) + ":" + scheduleList.get(position).getMinutes(), scheduleList.get(position).getAm_pm(),((Records) scheduleList.get(position)).getPhysician(), scheduleList.get(position).isOn());
            else
                holder.setTexts("Records", (scheduleList.get(position).getHour()-12) + ":" + scheduleList.get(position).getMinutes(), scheduleList.get(position).getAm_pm(),((Records) scheduleList.get(position)).getPhysician(), scheduleList.get(position).isOn());
        }
        else if(scheduleList.get(position) instanceof Appointment){
            holder.setTexts("Appointment",((Appointment) scheduleList.get(position)).getDateMonth() + "/" + ((Appointment) scheduleList.get(position)).getDateDay() + "/" + ((Appointment) scheduleList.get(position)).getDateYear(), (scheduleList.get(position).getHour()-12) + ":" + scheduleList.get(position).getMinutes() + " " + scheduleList.get(position).getAm_pm(),"Appointment with" + ((Appointment) scheduleList.get(position)).getPhysician(), scheduleList.get(position).isOn());
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pos = position;
                final String posString = Integer.toString(pos);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.edit_schedule, null);
                Button editPrescriptions = (Button) dialogView.findViewById(R.id.prescriptions_edit);
                Button editDate = (Button) dialogView.findViewById(R.id.date_edit);
                Button editAlarm = (Button) dialogView.findViewById(R.id.alarm_edit);

                mBuilder.setView(dialogView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                editPrescriptions.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        AlertDialog.Builder miniBuilder = new AlertDialog.Builder(context);
                        final View miniView = LayoutInflater.from(context).inflate(R.layout.edit_schedule_prescription, null);
                        final LinearLayout miniLayout = miniView.findViewById(R.id.layout_prescriptions);
                        Button editPrescriptionSched = (Button) miniView.findViewById(R.id.editPrescriptionData);
                        userDataReference = databaseReference.child("Users").child(user).child("schedules").child(posString);
                        userDataReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Schedule scheduletemp = dataSnapshot.getValue(Schedule.class);
                                for(int i = 0; i < scheduletemp.getPrescriptions().size(); i++) {
                                    String tempSched = scheduletemp.getPrescriptions().get(i);
                                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View rowView = layoutInflater.inflate(R.layout.row_prescription, null);
                                    EditText textOut = (EditText) rowView.findViewById(R.id.editTextPrestcription);
                                    textOut.setText(tempSched);
                                    textOut.setFocusable(true);
                                    miniLayout.addView(rowView);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        miniBuilder.setView(miniView);
                        final AlertDialog dialogTrial = miniBuilder.create();
                       // dialogTrial.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                        dialogTrial.show();
                        dialogTrial.getWindow().clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                        dialogTrial.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                        editPrescriptionSched.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View view) {
                                ArrayList<String> presTemp = new ArrayList<>();
                                int childCount = miniLayout.getChildCount();
                                for(int i = 0; i<childCount; i++){

                                    View thisChild = miniLayout.getChildAt(i);
                                    EditText edittemp = (EditText) thisChild.findViewById(R.id.editTextPrestcription);
                                    presTemp.add(edittemp.getText().toString());

                                }

                               // scheduleList.get(pos).setPrescriptions(presTemp);
                                userDataReference = databaseReference.child("Users").child(user).child("schedules");
                                userDataReference.setValue(scheduleList);
                                notifyDataSetChanged();
                                dialogTrial.dismiss();

                            }
                        });
                    }
                });


                editAlarm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                        View dialogView = LayoutInflater.from(context).inflate(R.layout.edit_schedule_alarm, null);
                        LinearLayout prescription_container = (LinearLayout) dialogView.findViewById(R.id.container_prescription_alarm);
                        Button prescription_alarm = (Button) dialogView.findViewById(R.id.prescription_alarm);
                        /*
                        for(int i=0; i < scheduleList.get(position).getPrescriptions().size(); i++){
                            LayoutInflater layoutInflater =
                                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View AddView = layoutInflater.inflate(R.layout.edit_schedule_alarm_row, null);
                            TextView prescriptionAlarm = (TextView) AddView.findViewById(R.id.alarmPrescription);
                            prescriptionAlarm.setText(scheduleList.get(position).getPrescriptions().get(i).toString());
                            prescription_container.addView(AddView);
                        }
                        */
                        mBuilder.setView(dialogView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.show();

                        prescription_alarm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }
                });

            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.continue_back, null);
                Button continueButton = (Button) dialogView.findViewById(R.id.continue_button);
                Button backButton = (Button) dialogView.findViewById(R.id.no_button);

                mBuilder.setView(dialogView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                continueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int pos = position;
                        final String posString = Integer.toString(pos);
                        userDataReference = databaseReference.child("Users").child(user).child("schedules").child(posString);
                        //userDataReference.setValue(null);
                        userDataReference.setValue(null);
                        scheduleList.remove(pos);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public void triggerAlarmManager(int alarmTriggerTime) {
        // get a Calendar object with current time
        Calendar cal = Calendar.getInstance();
        // add alarmTriggerTime seconds to the calendar object
        cal.add(Calendar.SECOND, alarmTriggerTime);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);//set alarm manager with entered timer by converting into milliseconds
        Toast.makeText(context, "Alarm Set for " + alarmTriggerTime + " seconds.", Toast.LENGTH_SHORT).show();

    }

    public void stopAlarmManager() {

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);//cancel the alarm manager of the pending intent


        //Stop the Media Player Service to stop sound
        context.stopService(new Intent(context, AlarmSoundService.class));

        //remove the notification from notification tray
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(AlarmNotificationService.NOTIFICATION_ID);

    }
}
