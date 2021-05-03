package com.openclassrooms.realestatemanager.service;

import com.openclassrooms.realestatemanager.EstateItem;
import com.openclassrooms.realestatemanager.ItemViewModel;
import com.openclassrooms.realestatemanager.repositories.ItemDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class EstateAPIService implements EstateAPI {

    private List<EstateItem> estates = new ArrayList<>();
    private final ItemDataRepository itemDataSource;
    private final Executor executor;
    private ItemViewModel itemViewModel;

    public EstateAPIService(ItemDataRepository itemDataSource, Executor executor) {
        itemViewModel = new ItemViewModel(itemDataSource, executor);
        this.itemDataSource = itemDataSource;
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
    public void deleteEstate(EstateItem item) {
        estates.remove(item);
    }

    @Override
    public void editEstate(EstateItem item) {
        executor.execute(() -> {
            itemViewModel.updateItem(item);
        });
    }
}
