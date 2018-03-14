package com.example.mp.clincdatabase;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {


    public ArrayList<Schedule> scheduleList;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;
    private Context context;
    private String user;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView schedDate;
        private TextView schedPrescription;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTexts(String date, ArrayList<String> prescription){

            final int pos = getAdapterPosition();
            final String posString = Integer.toString(pos);
            if(pos != RecyclerView.NO_POSITION){
                Schedule clickedDataItem = scheduleList.get(pos);

                schedDate = (TextView) mView.findViewById(R.id.dateSchedule);
                schedPrescription = (TextView) mView.findViewById(R.id.datePrescription);

                schedDate.setText(date);
                schedPrescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                        View dialogView = LayoutInflater.from(context).inflate(R.layout.edit_schedule, null);
                        Button editPrescriptions = (Button) dialogView.findViewById(R.id.prescriptions_edit);
                        Button editDate = (Button) dialogView.findViewById(R.id.date_edit);

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

                                                    miniLayout.addView(rowView);

                                                }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                miniBuilder.setView(miniView);
                                final AlertDialog dialogTrial = miniBuilder.create();
                                dialogTrial.show();
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

                                        scheduleList.get(pos).setPrescriptions(presTemp);
                                        userDataReference = databaseReference.child("Users").child(user).child("schedules");
                                        userDataReference.setValue(scheduleList);
                                        notifyDataSetChanged();
                                        dialogTrial.dismiss();

                                    }
                                });
                            }
                        });

                    }
                });

                schedPrescription.setOnLongClickListener(new View.OnLongClickListener() {
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
                                int pos = getAdapterPosition();
                                String posString = Integer.toString(pos);
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

            schedPrescription.setText("");

            for(int i = 0; i < prescription.size(); i++){
                schedPrescription.append((i+1) + ") "+prescription.get(i).toString() + "\n");
            }
        }
    }

    public ScheduleAdapter(ArrayList<Schedule> scheduleList, String user, Context context){
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
    public void onBindViewHolder(ScheduleAdapter.ViewHolder holder, int position) {
        holder.setTexts(scheduleList.get(position).getDate(), scheduleList.get(position).getPrescriptions());
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }
}
