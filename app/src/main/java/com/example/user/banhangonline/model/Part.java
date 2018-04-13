package com.example.user.banhangonline.model;

import android.graphics.drawable.Drawable;

public class Part {
    private String IDCategory;
    private String IDPay;
    private int url;
    private String title;

    public Part(String IDCategory, String IDPay, int url, String title) {
        this.IDCategory = IDCategory;
        this.IDPay = IDPay;
        this.url = url;
        this.title = title;
    }

    public String getIDCategory() {
        return IDCategory;
    }

    public void setIDCategory(String IDCategory) {
        this.IDCategory = IDCategory;
    }

    public String getIDPay() {
        return IDPay;
    }

    public void setIDPay(String IDPay) {
        this.IDPay = IDPay;
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
