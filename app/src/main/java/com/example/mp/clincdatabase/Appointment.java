package com.example.mp.clincdatabase;


import java.util.ArrayList;

public class Appointment extends Alarm{

    private String physician;
    private int dateYear;
    private int dateMonth;
    private int dateDay;

    public Appointment(){

    }

    public Appointment(String am_pm, int hour, int minutes, boolean isOn, ArrayList<String> days, String physician, int dateYear, int dateMonth, int dateDay) {
        super(am_pm, hour, minutes, isOn, days);
        this.physician = physician;
        this.dateYear = dateYear;
        this.dateMonth = dateMonth;
        this.dateDay = dateDay;
    }

    public Appointment(String physician, int dateYear, int dateMonth, int dateDay) {
        this.physician = physician;
        this.dateYear = dateYear;
        this.dateMonth = dateMonth;
        this.dateDay = dateDay;
    }

    public Appointment(String physician, String date) {
        this.physician = physician;
    }

    public String getPhysician() {
        return physician;
    }

    public void setPhysician(String physician) {
        this.physician = physician;
    }

    public int getDateYear() {
        return dateYear;
    }

    public void setDateYear(int dateYear) {
        this.dateYear = dateYear;
    }

    public int getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(int dateMonth) {
        this.dateMonth = dateMonth;
    }

    public int getDateDay() {
        return dateDay;
    }

    public void setDateDay(int dateDay) {
        this.dateDay = dateDay;
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
