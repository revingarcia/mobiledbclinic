package com.example.mp.clincdatabase;

import java.util.ArrayList;


public class Records {

    private String physician;
    private ArrayList<Prescriptions> prescriptions = new ArrayList<>();
    private ArrayList<String> allergies;
    private ArrayList<String> drug_plan;

    public Records(){

    }

    public Records(String physician){
        this.physician = physician;
    }

    public Records(ArrayList<Prescriptions> prescriptions, ArrayList<String> allergies, ArrayList<String> drug_plan, String physician) {
        this.prescriptions = prescriptions;
        this.allergies = allergies;
        this.drug_plan = drug_plan;
        this.physician = physician;
    }

    public ArrayList<Prescriptions> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(ArrayList<Prescriptions> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public ArrayList<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(ArrayList<String> allergies) {
        this.allergies = allergies;
    }

    public ArrayList<String> getDrug_plan() {
        return drug_plan;
    }

    public void setDrug_plan(ArrayList<String> drug_plan) {
        this.drug_plan = drug_plan;
    }

    public String getPhysician() {
        return physician;
    }

    public void setPhysician(String physician){
        this.physician = physician;
    }
}
