package com.example.mp.clincdatabase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private ArrayList<Appointment> scheduleList;
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

        public void setTexts(Appointment appointment){
            schedDate = (TextView) mView.findViewById(R.id.dateSchedule);
            schedPrescription = (TextView) mView.findViewById(R.id.datePrescription);

            schedDate.setText(appointment.getDate());
            schedPrescription.setText("Appointment with" + appointment.getPhysician() + "(" + appointment.getTime() +")");


            schedPrescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Edit Function WIll be implemented in the Final Version", Toast.LENGTH_SHORT).show();
                }
            });


            schedPrescription.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(context, "Delete Function WIll be implemented in the Final Version", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION){
                Appointment clickedDataItem = scheduleList.get(pos);

            }
        }
    }

    public AppointmentAdapter(ArrayList<Appointment> scheduleList, String user, Context context){
        this.scheduleList = scheduleList;
        this.user = user;
        this.context = context;
    }

    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_listrow, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppointmentAdapter.ViewHolder holder, int position) {
        holder.setTexts(scheduleList.get(position));
    }


    @Override
    public int getItemCount() {
        return scheduleList.size();
    }
}
