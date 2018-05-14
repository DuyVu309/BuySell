package com.example.user.banhangonline.model;

public class DonHang {
    private String idDonHang;
    private String idNguoiMua;
    private String idNguoiBan;
    private String thoiGian;
    private String diaChi;
    private String soDienThoai;

    public DonHang(String idDonHang, String idNguoiMua, String idNguoiBan, String thoiGian, String diaChi, String soDienThoai) {
        this.idDonHang = idDonHang;
        this.idNguoiMua = idNguoiMua;
        this.idNguoiBan = idNguoiBan;
        this.thoiGian = thoiGian;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
    }

    public String getIdDonHang() {
        return idDonHang;
    }

    public void setIdDonHang(String idDonHang) {
        this.idDonHang = idDonHang;
    }

    public String getIdNguoiMua() {
        return idNguoiMua;
    }

    public void setIdNguoiMua(String idNguoiMua) {
        this.idNguoiMua = idNguoiMua;
    }

    public String getIdNguoiBan() {
        return idNguoiBan;
    }

    public void setIdNguoiBan(String idNguoiBan) {
        this.idNguoiBan = idNguoiBan;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
}
