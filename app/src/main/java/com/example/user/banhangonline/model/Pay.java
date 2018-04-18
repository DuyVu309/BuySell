package com.example.user.banhangonline.model;

public class Pay {
    private String idCategory;
    private String idPay;
    private String titlePay;
    private String imgLink;
    private Integer total;

    public Pay(String idCategory, String idPart, String titlePay, String imgLink, Integer total) {
        this.idCategory = idCategory;
        this.idPay = idPart;
        this.titlePay = titlePay;
        this.imgLink = imgLink;
        this.total = total;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getIdPay() {
        return idPay;
    }

    public void setIdPay(String idPay) {
        this.idPay = idPay;
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
