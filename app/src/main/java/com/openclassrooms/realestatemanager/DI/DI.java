package com.openclassrooms.realestatemanager.DI;

import com.openclassrooms.realestatemanager.repositories.ItemDataRepository;
import com.openclassrooms.realestatemanager.service.EstateAPI;
import com.openclassrooms.realestatemanager.service.EstateAPIService;

import java.util.concurrent.Executor;

public class DI {

    private static ItemDataRepository itemDataSource;
    private static Executor executor;

    private static EstateAPI service = new EstateAPIService(itemDataSource, executor);

    public static EstateAPI getEstateApiService() { return service; }
}
