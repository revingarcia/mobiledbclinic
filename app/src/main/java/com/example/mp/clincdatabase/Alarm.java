package com.example.mp.clincdatabase;

import java.util.ArrayList;

/**
 * Created by waboy on 4/2/2018.
 */

public class Alarm {

    private String am_pm;
    private int hour;
    private int minutes;
    private boolean isOn;
    private ArrayList<String> days;
    private String type;

    public Alarm(){
        days = new ArrayList<>();
    }

    public Alarm (String am_pm, int hour, int minutes, boolean isOn, ArrayList<String> days){
        this.am_pm = am_pm;
        this.hour = hour;
        this.minutes = minutes;
        this.isOn = isOn;
        this.days = days;
    }


    public String getAm_pm() {
        return am_pm;
    }

    public void setAm_pm(String am_pm) {
        this.am_pm = am_pm;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
    }

}
