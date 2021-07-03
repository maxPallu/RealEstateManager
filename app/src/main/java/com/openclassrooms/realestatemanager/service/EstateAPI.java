package com.openclassrooms.realestatemanager.service;

import android.content.Context;

import com.openclassrooms.realestatemanager.ui.EstateItem;

import java.util.List;

public interface EstateAPI {

    List<EstateItem> getEstates();
    void createEstate(EstateItem item);

    void editEstate(EstateItem item, Context context);

}
