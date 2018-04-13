package com.example.user.banhangonline.model;

public class Pay {
    private String IDCategory;
    private String IDPay;
    private String titlePay;
    private String imgLink;
    private Integer total;

    public Pay(String IDCategory, String IDPay, String titlePay, String imgLink, Integer total) {
        this.IDCategory = IDCategory;
        this.IDPay = IDPay;
        this.titlePay = titlePay;
        this.imgLink = imgLink;
        this.total = total;
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

    public String getTitlePay() {
        return titlePay;
    }

    public void setTitlePay(String titlePay) {
        this.titlePay = titlePay;
    }

    public Integer getAdress() {
        return total;
    }

    public void setAdress(Integer total) {
        this.total = total;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }
}
