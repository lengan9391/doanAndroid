package com.example.quanlynhanvien.model;

import java.io.Serializable;

public class PhongBan implements Serializable {
    private String mapb;
    private String tenpb;

    public PhongBan(String mapb, String tenpb) {
        this.mapb = mapb;
        this.tenpb = tenpb;
    }

    public PhongBan(String tenpb) {
        this.tenpb = tenpb;
    }
    public PhongBan(){

    }

    public String getMapb() {
        return mapb;
    }

    public void setMapb(String mapb) {
        this.mapb = mapb;
    }

    public String getTenpb() {
        return tenpb;
    }

    public void setTenpb(String tenpb) {
        this.tenpb = tenpb;
    }
    @Override
    public String toString(){
        return mapb+"\n"+tenpb;
    }
}
