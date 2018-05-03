package com.example.user.banhangonline.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Account {

    private String id;
    private String name;
    private String phoneNumber;
    private String urlAvt;
    private String nameAvt;
    private String urlLanscape;
    private String nameLans;
    private String address;

    public Account() {
    }

    public Account(String id, String name, String phoneNumber, String urlAvt, String nameAvt, String urlLanscape, String nameLans, String address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.urlAvt = urlAvt;
        this.nameAvt = nameAvt;
        this.urlLanscape = urlLanscape;
        this.nameLans = nameLans;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUrlAvt() {
        return urlAvt;
    }

    public void setUrlAvt(String urlAvt) {
        this.urlAvt = urlAvt;
    }

    public String getNameAvt() {
        return nameAvt;
    }

    public void setNameAvt(String nameAvt) {
        this.nameAvt = nameAvt;
    }

    public String getUrlLanscape() {
        return urlLanscape;
    }

    public void setUrlLanscape(String urlLanscape) {
        this.urlLanscape = urlLanscape;
    }

    public String getNameLans() {
        return nameLans;
    }

    public void setNameLans(String nameLans) {
        this.nameLans = nameLans;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
