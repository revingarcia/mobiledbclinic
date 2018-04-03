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
        private TextView alarmType;
        private TextView alarmTimeDate;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTexts(String date, String description){

            alarmTimeDate = (TextView) mView.findViewById(R.id.tv_physician);
            alarmType = (TextView) mView.findViewById(R.id.tv_description);

            alarmTimeDate.setText(date);
            alarmType.setText(description);
        }
    }

    public AppointmentAdapter(ArrayList<Appointment> scheduleList, String user, Context context){
        this.scheduleList = scheduleList;
        this.user = user;
        this.context = context;
    }

    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_list, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppointmentAdapter.ViewHolder holder, int position) {
        holder.setTexts("11/12/2039", "Appointment with" + scheduleList.get(position).getPhysician());

    }


    @Override
    public int getItemCount() {
        return scheduleList.size();
    }
}
