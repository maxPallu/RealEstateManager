package com.openclassrooms.realestatemanager;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class EstateItem {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String estateType;
    private String estatePrice;
    private String estateSurface;
    private String estateRoom;
    private String estateCity;
    private String estateAdress;

    public EstateItem(String type, String price, String surface, String numberRoom, String city, String adress) {
        estateType = type;
        estatePrice = price;
        estateSurface = surface;
        estateRoom = numberRoom;
        estateCity = city;
        estateAdress = adress;
    }

    public String getEstateType() {
        return estateType;
    }

    public void setEstateType(String estateType) {
        this.estateType = estateType;
    }

    public String getEstatePrice() {
        return estatePrice;
    }

    public void setEstatePrice(String estatePrice) {
        this.estatePrice = estatePrice;
    }

    public String getEstateSurface() {
        return estateSurface;
    }

    public void setEstateSurface(String estateSurface) {
        this.estateSurface = estateSurface;
    }

    public String getEstateRoom() {
        return estateRoom;
    }

    public void setEstateRoom(String estateRoom) {
        this.estateRoom = estateRoom;
    }

    public String getEstateCity() {
        return estateCity;
    }

    public void setEstateCity(String estateCity) {
        this.estateCity = estateCity;
    }

    public String getEstateAdress() {
        return estateAdress;
    }

    public void setEstateAdress(String estateAdress) {
        this.estateAdress = estateAdress;
    }
}
