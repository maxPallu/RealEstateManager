package com.openclassrooms.realestatemanager.service;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.realestatemanager.DI.DI;
import com.openclassrooms.realestatemanager.EstateItem;
import com.openclassrooms.realestatemanager.Injection.Injection;
import com.openclassrooms.realestatemanager.Injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.ItemViewModel;
import com.openclassrooms.realestatemanager.database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.security.auth.callback.Callback;

import kotlinx.coroutines.Dispatchers;

public class EstateAPIService implements EstateAPI {

    private List<EstateItem> estates = new ArrayList<>();
    private ItemViewModel itemViewModel;
    private Database database;

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

    @Override
    public void editEstate(EstateItem item, Context context) {
        database = Database.getInstance(context);
        this.database.estateDao().updateItem(item);
    }


    public EstateAPIService() {
    }
}
