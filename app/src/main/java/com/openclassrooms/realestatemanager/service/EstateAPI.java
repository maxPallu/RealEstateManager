package com.openclassrooms.realestatemanager.service;

import com.openclassrooms.realestatemanager.EstateItem;

import java.util.List;

public interface EstateAPI {

    List<EstateItem> getEstates();
    void createEstate(EstateItem item);
    void deleteEstate(EstateItem item);

}
