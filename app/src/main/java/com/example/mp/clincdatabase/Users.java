package com.example.mp.clincdatabase;

import java.util.ArrayList;


public class Users {

    private String Fname;
    private String password;
    private String username;
    private String birthday;
    private String userID;
    private String contact;
    private ArrayList<Records> records;

    public Users(){

    }

    public Users(String fname, String birthday, String contact, String password, String username, String userID, ArrayList<Records> records) {
        this.Fname = fname;
        this.birthday = birthday;
        this.contact = contact;
        this.password = password;
        this.username = username;
        this.userID = userID;
        this.records = records;
    }

    public String getContact() { return contact; }

    public void setContact(String contact) { this.contact = contact; }

    public String getBirthday() { return birthday; }

    public void setBirthday(String birthday) { this.birthday = birthday; }
    public String getFname(){
        return Fname;
    }

    public void setFname(String fname) {
        Fname = fname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserID() { return userID; }

    public void setUserID(String userID) { this.userID = userID; }

    public ArrayList<Records> getRecords() { return records; }

    public void setRecords(ArrayList<Records> records) { this.records = records; }

}
