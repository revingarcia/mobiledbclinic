package com.example.mp.clincdatabase;

import android.app.AlertDialog;
import android.content.Context;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {


    public ArrayList<Alarm> scheduleList;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;
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
                        if(isonCheck.isChecked())
                            scheduleList.get(getAdapterPosition()).setOn(true);
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
}
