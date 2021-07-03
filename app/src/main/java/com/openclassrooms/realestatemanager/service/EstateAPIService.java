package com.openclassrooms.realestatemanager.service;

import android.content.Context;

import com.openclassrooms.realestatemanager.ui.EstateItem;
import com.openclassrooms.realestatemanager.ui.ItemViewModel;
import com.openclassrooms.realestatemanager.repositories.ItemDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class EstateAPIService implements EstateAPI {

    private final List<EstateItem> estates = new ArrayList<>();
    private final Executor executor;
    private final ItemViewModel itemViewModel;

    public EstateAPIService(ItemDataRepository itemDataSource, Executor executor) {
        itemViewModel = new ItemViewModel(itemDataSource, executor);
        this.executor = executor;
    }

    @Override
    public List<EstateItem> getEstates() {
        return new ArrayList<>(estates);
    }

    @Override
    public void createEstate(EstateItem item) {
        estates.add(item);
    }

    @Override
    public void editEstate(EstateItem item, Context context) {
        executor.execute(() -> itemViewModel.updateItem(item, context));
    }
}
