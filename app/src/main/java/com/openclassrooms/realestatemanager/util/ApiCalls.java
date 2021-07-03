package com.openclassrooms.realestatemanager.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.models.Geocoder;
import com.openclassrooms.realestatemanager.models.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCalls {

    public interface Callbacks {
        void onResponse(@Nullable List<Result> locations);
        void onFailure();
    }

    public static void fetchLocations(Callbacks callbacks, String adress) {

        LocationService locationService = LocationService.retrofit.create(LocationService.class);

        Call<Geocoder> call = locationService.getLocation(adress);

        call.enqueue(new Callback<Geocoder>() {
            @Override
            public void onResponse(@NonNull Call<Geocoder> call, @NonNull Response<Geocoder> response) {
                if(callbacks != null) {
                    assert response.body() != null;
                    callbacks.onResponse(response.body().getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Geocoder> call, @NonNull Throwable t) {
                if(callbacks != null) callbacks.onFailure();
            }
        });
    }
}
