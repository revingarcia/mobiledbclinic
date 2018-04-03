package com.example.mp.clincdatabase;


import java.util.ArrayList;

public class Prescriptions{

    private String name;
    private String conditions;
    private String notes;

    public Prescriptions(){

    }

    public Prescriptions(String name, String conditions, String notes) {
        this.name = name;
        this.conditions = conditions;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


}
