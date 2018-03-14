package com.example.mp.clincdatabase;


public class Prescriptions {

    private String name;
    private String frequency;
    private String conditions;
    private String notes;

    public Prescriptions(){

    }

    public Prescriptions(String name, String frequency, String conditions, String notes){
        this.name = name;
        this.frequency = frequency;
        this.conditions = conditions;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
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
