package com.openclassrooms.realestatemanager.util;

import com.openclassrooms.realestatemanager.models.Location;
import com.openclassrooms.realestatemanager.models.Result;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocationService {

    @GET("/maps/api/geocode/json?&key=AIzaSyC_xgD6R-yxQK5ZkL4gknpSH5ND2-BFZck")
    Call<Result> getLocation(@Query("adress") String adress);

    public static final Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl("https://maps.googleapis.com")
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
}
