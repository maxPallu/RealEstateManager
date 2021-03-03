package com.openclassrooms.realestatemanager.service;

import com.openclassrooms.realestatemanager.EstateItem;

import java.util.ArrayList;
import java.util.List;

public class EstateAPIService implements EstateAPI {

    private List<EstateItem> estates = new ArrayList<>();

    @Override
    public List<EstateItem> getEstates() {
        return new ArrayList<>(estates);
    }

    @Override
    public void createEstate(EstateItem item) {
        estates.add(item);
    }

    @Override
    public void deleteEstate(EstateItem item) {
        estates.remove(item);
    }
}
