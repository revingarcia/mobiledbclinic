package com.example.mp.clincdatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class Schedule {

    private String date;
    private ArrayList<String> prescriptions;


    public Schedule(){

    }

    public Schedule(String date, ArrayList<String> prescriptions) {
        this.date = date;
        this.prescriptions = prescriptions;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(ArrayList<String> prescriptions) {
        this.prescriptions = prescriptions;
    }
}
