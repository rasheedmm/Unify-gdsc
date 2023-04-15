package com.nexus.unify.ModelClasses;

import java.io.Serializable;

public class ItemModel implements Serializable {
    private String image;
    private String nama, usia, kota;

    public ItemModel() {
    }

    public ItemModel(String image, String nama, String usia, String kota) {
        this.image = image;
        this.nama = nama;
        this.usia = usia;
        this.kota = kota;
    }

    public String getImage() {
        return image;
    }

    public String getNama() {
        return nama;
    }

    public String getUsia() {
        return usia;
    }

    public String getKota() {
        return kota;
    }
}
