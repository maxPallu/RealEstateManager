package com.openclassrooms.realestatemanager.util;

import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Location;
import com.openclassrooms.realestatemanager.models.Result;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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

        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

        LocationService locationService = LocationService.retrofit.create(LocationService.class);

        Call<Result> call = locationService.getLocation(adress);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(callbacksWeakReference != null) {
                    ArrayList<Result> results = new ArrayList<>();
                    results.add(response.body());
                    callbacksWeakReference.get().onResponse(results);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
               if(callbacksWeakReference != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}
