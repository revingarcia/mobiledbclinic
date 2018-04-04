package com.example.mp.clincdatabase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.AlphabeticIndex;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder> {
    @NonNull

    private ArrayList<Records> prescriptionList;
    private String user;
    private Context context;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
            private View mView;
            private TextView mTextView;
            private TextView description;
            private ImageView image;
        public ViewHolder(View v) {
            super(v);
            mView = v;

            userDataReference = databaseReference.child("Users").child(user).child("records");
            userDataReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    prescriptionList.clear();
                    if(dataSnapshot.exists()){
                        for(DataSnapshot childRecord: dataSnapshot.getChildren()){
                            Records recordtemp = childRecord.getValue(Records.class);
                            prescriptionList.add(recordtemp);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public void setName(String prescName, String num){
            mTextView = mView.findViewById(R.id.tv_physician);
            description = mView.findViewById(R.id.tv_description);
            image = mView.findViewById(R.id.imageArrow);
            mTextView.setText(prescName);
            description.setText(num);

        }

        public TextView getmTextView (){
            return mTextView;
        }
    }

    public PrescriptionAdapter(ArrayList<Records> prescriptionList, String user, Context context){
        this.prescriptionList = prescriptionList;
        this.user = user;
        this.context = context;
    }


    @Override
    public PrescriptionAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_list,parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final PrescriptionAdapter.ViewHolder holder, final int position) {
        holder.setName(prescriptionList.get(position).getPhysician(), "Number of Prescriptions"+prescriptionList.get(position).getPrescriptions().size());
        final int pos = position;
        final String posString = Integer.toString(pos);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if item still exists
                if(pos != RecyclerView.NO_POSITION){


                    userDataReference = databaseReference.child("Users").child(user);
                    userDataReference.child("records").addListenerForSingleValueEvent(new ValueEventListener(){

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(posString).child("prescriptions").exists()){
                                Records clickedDataItem = prescriptionList.get(pos);
                                Intent intent = new Intent(holder.mView.getContext(), RecordView.class);
                                intent.putExtra("username", user);
                                intent.putExtra("position", pos);
                                context.startActivity(intent);
                            }
                            else{
                                Toast.makeText(holder.mView.getContext(), "This Record does not have any prescription", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });


                }
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // check if item still exists
                if(pos != RecyclerView.NO_POSITION){
                    Records clickedDataItem = prescriptionList.get(pos);
                    AlertDialog.Builder menuBuilder = new AlertDialog.Builder(context);
                    View menuDialogView = LayoutInflater.from(context).inflate(R.layout.prescriptions_edit,null);
                    final Button addPrescription = (Button) menuDialogView.findViewById(R.id.add_new_prescription);
                    final Button alarmEdit = (Button) menuDialogView.findViewById(R.id.alarm_edit);
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialogadd_prescription, null);
                            final EditText addMed = (EditText) dialogView.findViewById(R.id.addMed);
                            final EditText addCondition = (EditText) dialogView.findViewById(R.id.addCondition);
                            final EditText addNotes = (EditText) dialogView.findViewById(R.id.addNotes);

                            Button addPresLogin = (Button) dialogView.findViewById(R.id.addPresLogin);

                            mBuilder.setView(dialogView);

                            final AlertDialog dialog = mBuilder.create();
                            dialog.show();
                            addPresLogin.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    /*
                                    prescriptionList.get(pos).getPrescriptions().add(new Prescriptions(addMed.getText().toString(), adFrequency.getText().toString(),addCondition.getText().toString(), addNotes.getText().toString()));
                                    userDataReference = databaseReference.child("Users").child(user).child("records");
                                    userDataReference.setValue(prescriptionList);
                                    */
                                    dialog.dismiss();
                                    Intent intent = new Intent(context, PrescriptionAlarmAdd.class);
                                    intent.putExtra("medicine", addMed.getText().toString());
                                    intent.putExtra("condition", addCondition.getText().toString());
                                    intent.putExtra("notes", addNotes.getText().toString());
                                    intent.putExtra("username", user);
                                    intent.putExtra("position", position);
                                    context.startActivity(intent);
                                    notifyDataSetChanged();
                                }
                            });


                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }
}
