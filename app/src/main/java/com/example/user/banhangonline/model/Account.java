package com.example.user.banhangonline.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Account {

    private String ID;
    private String name;
    private String phoneNumber;

    public Account() {
    }

    public Account(String AcountID, String Name, String PhoneNumber) {
        this.ID = AcountID;
        this.name = Name;
        this.phoneNumber = PhoneNumber;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
