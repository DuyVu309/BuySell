package com.example.user.banhangonline.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class DonHang implements Serializable {
    private String diaChi;
    private String header;
    private String idDonHang;
    private String idNguoiBan;
    private String idNguoiMua;
    private String nameNguoiMua;
    private String soDienThoai;
    private String thoiGian;
    private String urlImg;
    private String gia;
    private String soLuong;
    private double latitude;
    private double longitude;

    public DonHang() {
    }

    public DonHang(String diaChi,
                   String header,
                   String idDonHang,
                   String idNguoiBan,
                   String idNguoiMua,
                   String nameNguoiMua,
                   String soDienThoai,
                   String thoiGian,
                   String urlImg,
                   String gia,
                   String soLuong,
                   double latitude,
                   double longitude) {
        this.diaChi = diaChi;
        this.header = header;
        this.idDonHang = idDonHang;
        this.idNguoiBan = idNguoiBan;
        this.idNguoiMua = idNguoiMua;
        this.nameNguoiMua = nameNguoiMua;
        this.soDienThoai = soDienThoai;
        this.thoiGian = thoiGian;
        this.urlImg = urlImg;
        this.gia = gia;
        this.soLuong = soLuong;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getIdDonHang() {
        return idDonHang;
    }

    public void setIdDonHang(String idDonHang) {
        this.idDonHang = idDonHang;
    }

    public String getIdNguoiBan() {
        return idNguoiBan;
    }

    public void setIdNguoiBan(String idNguoiBan) {
        this.idNguoiBan = idNguoiBan;
    }

    public String getIdNguoiMua() {
        return idNguoiMua;
    }

    public void setIdNguoiMua(String idNguoiMua) {
        this.idNguoiMua = idNguoiMua;
    }

    public String getNameNguoiMua() {
        return nameNguoiMua;
    }

    public void setNameNguoiMua(String nameNguoiMua) {
        this.nameNguoiMua = nameNguoiMua;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
