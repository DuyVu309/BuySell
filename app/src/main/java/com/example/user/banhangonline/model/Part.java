package com.example.user.banhangonline.model;


import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Part implements Serializable {
    private String IDCategory;
    private String IDPart;
    private int url;
    private String title;

    public Part(String IDCategory, String IDPart, int url, String title) {
        this.IDCategory = IDCategory;
        this.IDPart = IDPart;
        this.url = url;
        this.title = title;
    }

    public String getIDCategory() {
        return IDCategory;
    }

    public void setIDCategory(String IDCategory) {
        this.IDCategory = IDCategory;
    }

    public String getIDPart() {
        return IDPart;
    }

    public void setIDPart(String IDPart) {
        this.IDPart = IDPart;
    }

    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
