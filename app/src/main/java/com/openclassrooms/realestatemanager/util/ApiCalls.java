package com.openclassrooms.realestatemanager.util;

import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.models.Location;
import com.openclassrooms.realestatemanager.models.Result;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCalls {

    public interface Callbacks {
        void onResponse(@Nullable List<Result> locations);
        void onFailure();
    }

    public static void fetchLocations(Callbacks callbacks) {

        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

        LocationService locationService = LocationService.retrofit.create(LocationService.class);

        Call<Location> call = locationService.getLocation();

        call.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if(callbacksWeakReference.get() != null) {
                    callbacksWeakReference.get().onResponse(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {

            }
        });
    }
}
