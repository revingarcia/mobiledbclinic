package com.example.mp.clincdatabase;

import java.util.ArrayList;


public class Records extends Alarm{

    private String physician;
    private ArrayList<Prescriptions> prescriptions = new ArrayList<>();
    private ArrayList<String> allergies;
    private ArrayList<String> drug_plan;
    private String frequency;

    public Records(){

    }

    public Records(Alarm alarm){
        super(alarm.getAm_pm(),alarm.getHour(),alarm.getMinutes(),alarm.isOn(),alarm.getDays());
    }

    public Records(String physician, Alarm alarmData, String frequency){
        this.physician = physician;
        this.frequency = frequency;
    }

    public Records(ArrayList<Prescriptions> prescriptions, ArrayList<String> allergies, ArrayList<String> drug_plan, String physician, Alarm alarmData, String frequency) {
        this.prescriptions = prescriptions;
        this.allergies = allergies;
        this.drug_plan = drug_plan;
        this.physician = physician;
        this.frequency = frequency;
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


    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setAlarmData(Alarm alarmData) {
        super.setAm_pm(alarmData.getAm_pm());
        super.setDays(alarmData.getDays());
        super.setHour(alarmData.getHour());
        super.setMinutes(alarmData.getMinutes());
        super.setOn(alarmData.isOn());
    }

    public String getAm_pm() {
        return super.getAm_pm();
    }

    public void setAm_pm(String am_pm) {
        super.setAm_pm(am_pm);
    }

    public int getHour() {
        return super.getHour();
    }

    public void setHour(int hour) {
        super.setHour(hour);
    }

    public int getMinutes() {
        return super.getMinutes();
    }

    public void setMinutes(int minutes) {
        super.setMinutes(minutes);
    }

    public boolean isOn() {
        return super.isOn();
    }

    public void setOn(boolean on) {
        super.setOn(on);
    }

    public ArrayList<String> getDays() {
        return super.getDays();
    }

    public void setDays(ArrayList<String> days) {
        super.setDays(days);
    }

}
