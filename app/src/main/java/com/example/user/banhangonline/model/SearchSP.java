package com.example.user.banhangonline.model;

import java.io.Serializable;

public class SearchSP implements Serializable{
    private String idSp;
    private String headerSp;

    public SearchSP(String idSp, String headerSp) {
        this.idSp = idSp;
        this.headerSp = headerSp;
    }

    public String getIdSp() {
        return idSp;
    }

    public void setIdSp(String idSp) {
        this.idSp = idSp;
    }

    public String getHeaderSp() {
        return headerSp;
    }

    public void setHeaderSp(String headerSp) {
        this.headerSp = headerSp;
    }
}
