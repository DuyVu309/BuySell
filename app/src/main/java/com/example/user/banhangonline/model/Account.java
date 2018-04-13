package com.example.user.banhangonline.model;

public class Account {

    private String ID;
    private String name;
    private String email;
    private String phoneNumber;

    public Account(String AcountID, String Name, String Email, String PhoneNumber) {
        this.ID = AcountID;
        this.name = Name;
        this.email = Email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
