package com.openclassrooms.realestatemanager;

import android.content.ContentValues;
import android.net.Uri;
import android.widget.ImageView;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EstateItem {

    @PrimaryKey(autoGenerate = true)
    private long estateId;
    private String estateType;
    private int estatePrice;
    private int estateSurface;
    private int estateRoom;
    private String estateCity;
    private String estateAdress;
    private String estatePictureUri;
    private String estateDescription;

    public EstateItem(String type, int price, int surface, int numberRoom, String city, String adress) {
        estateType = type;
        estatePrice = price;
        estateSurface = surface;
        estateRoom = numberRoom;
        estateCity = city;
        estateAdress = adress;
    }

    public EstateItem(long id, String type, int price, int surface, int numberRoom, String city, String adress) {
        estateId = id;
        estateType = type;
        estatePrice = price;
        estateSurface = surface;
        estateRoom = numberRoom;
        estateCity = city;
        estateAdress = adress;
    }

    public static EstateItem fromContentValues(ContentValues values) {
        final EstateItem item = new EstateItem();
        if (values.containsKey("estateType")) item.setEstateType(values.getAsString("estateType"));
        if (values.containsKey("estateCity")) item.setEstateCity(values.getAsString("estateCity"));
        if (values.containsKey("estatePrice")) item.setEstatePrice(values.getAsInteger("estatePrice"));
        if (values.containsKey("estateAdress")) item.setEstateAdress(values.getAsString("estateAdress"));
        return item;
    }

    public EstateItem() {

    }

    public String getEstateDescription() {
        return estateDescription;
    }

    public void setEstateDescription(String estateDescription) {
        this.estateDescription = estateDescription;
    }

    public String getEstatePictureUri() {
        return estatePictureUri;
    }

    public void setEstatePictureUri(String estatePictureUri) {
        this.estatePictureUri = estatePictureUri;
    }

    public long getEstateId() {
        return estateId;
    }

    public void setEstateId(long id) {
        this.estateId = id;
    }

    public String getEstateType() {
        return estateType;
    }

    public void setEstateType(String estateType) {
        this.estateType = estateType;
    }

    public int getEstatePrice() {
        return estatePrice;
    }

    public void setEstatePrice(int estatePrice) {
        this.estatePrice = estatePrice;
    }

    public int getEstateSurface() {
        return estateSurface;
    }

    public void setEstateSurface(int estateSurface) {
        this.estateSurface = estateSurface;
    }

    public int getEstateRoom() {
        return estateRoom;
    }

    public void setEstateRoom(int estateRoom) {
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
