package com.openclassrooms.realestatemanager.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Geocoder {
    @SerializedName("results")
    private final List<Result> results;

    public Geocoder(List<Result> results) {
        this.results = results;
    }

    public List<Result> getResults() {
        return results;
    }

}
