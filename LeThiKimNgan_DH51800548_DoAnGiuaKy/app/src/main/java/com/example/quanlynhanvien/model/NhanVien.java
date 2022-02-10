package com.example.quanlynhanvien.model;

import java.io.Serializable;

public class NhanVien implements Serializable {
    private String tennv;
    private String manv;
    private int luong;
    private String sdt;
    private String gioiTinh;
    private byte[] anh;
    private String phongban;

    public NhanVien(String tennv, String manv, int luong, String sdt, String gioiTinh, byte[] anh, String phongban) {
        this.tennv = tennv;
        this.manv = manv;
        this.luong = luong;
        this.sdt = sdt;
        this.gioiTinh = gioiTinh;
        this.anh = anh;
        this.phongban = phongban;
    }
    public NhanVien(){

    }

    public String getTennv() {
        return tennv;
    }

    public void setTennv(String tennv) {
        this.tennv = tennv;
    }

    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    public int getLuong() {
        return luong;
    }

    public void setLuong(int luong) {
        this.luong = luong;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public byte[] getAnh() {
        return anh;
    }

    public void setAnh(byte[] anh) {
        this.anh = anh;
    }

    public String getPhongban() {
        return phongban;
    }

    public void setPhongban(String phongban) {
        this.phongban = phongban;
    }
}
