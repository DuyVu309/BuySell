package com.example.user.banhangonline.model.search;

public class SearchAccount {
    private String urlAvt;
    private String nameAc;

    public SearchAccount(String urlAvt, String nameAc) {
        this.urlAvt = urlAvt;
        this.nameAc = nameAc;
    }

    public String getUrlAvt() {
        return urlAvt;
    }

    public void setUrlAvt(String urlAvt) {
        this.urlAvt = urlAvt;
    }

    public String getNameAc() {
        return nameAc;
    }

    public void setNameAc(String nameAc) {
        this.nameAc = nameAc;
    }
}
