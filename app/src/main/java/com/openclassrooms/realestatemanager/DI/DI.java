package com.openclassrooms.realestatemanager.DI;

import com.openclassrooms.realestatemanager.service.EstateAPI;
import com.openclassrooms.realestatemanager.service.EstateAPIService;

public class DI {

    private static EstateAPI service = new EstateAPIService();

    public static EstateAPI getEstateApiService() { return service; }
}
