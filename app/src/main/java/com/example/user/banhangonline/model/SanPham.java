package com.example.user.banhangonline.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

@IgnoreExtraProperties
public class SanPham implements Serializable, Comparator<SanPham>{
    private String idNguoiban;
    private String idSanPham;
    private String idCategory;
    private String idPart;
    private String header;
    private String mota;
    private String time;
    private ListFileImages listFiles;

    public SanPham() {
    }

    public SanPham(String idNguoiban, String idSanPham, String idCategory, String idPart, String header, String mota, String time, ListFileImages listFiles) {
        this.idNguoiban = idNguoiban;
        this.idSanPham = idSanPham;
        this.idCategory = idCategory;
        this.idPart = idPart;
        this.header = header;
        this.mota = mota;
        this.time = time;
        this.listFiles = listFiles;
    }


    public String getIdNguoiban() {
        return idNguoiban;
    }

    public void setIdNguoiban(String idNguoiban) {
        this.idNguoiban = idNguoiban;
    }

    public String getIdSanPham() {
        return idSanPham;
    }

    public void setIdSanPham(String idSanPham) {
        this.idSanPham = idSanPham;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getIdPart() {
        return idPart;
    }

    public void setIdPart(String idPart) {
        this.idPart = idPart;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ListFileImages getListFiles() {
        return listFiles;
    }

    public void setListFiles(ListFileImages listFiles) {
        this.listFiles = listFiles;
    }

    @Override
    public int compare(SanPham sanPham, SanPham t1) {
        return sanPham.getTime().compareTo(t1.getTime());
    }
}
