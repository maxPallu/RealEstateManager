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
    private String estatePrice;
    private String estateSurface;
    private String estateRoom;
    private String estateCity;
    private String estateAdress;
    private String estatePictureUri;
    private String estateDescription;

    public EstateItem(String type, String price, String surface, String numberRoom, String city, String adress) {
        estateType = type;
        estatePrice = price;
        estateSurface = surface;
        estateRoom = numberRoom;
        estateCity = city;
        estateAdress = adress;
    }

    public EstateItem(long id, String type, String price, String surface, String numberRoom, String city, String adress) {
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
        if (values.containsKey("estatePrice")) item.setEstatePrice(values.getAsString("estatePrice"));
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
