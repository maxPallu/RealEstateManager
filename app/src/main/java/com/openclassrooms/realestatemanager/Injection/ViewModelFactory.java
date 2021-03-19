package com.openclassrooms.realestatemanager.Injection;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.openclassrooms.realestatemanager.ItemViewModel;
import com.openclassrooms.realestatemanager.repositories.ItemDataRepository;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final ItemDataRepository itemDataSource;
    private final Executor executor;

    public ViewModelFactory(ItemDataRepository itemDataRepository, Executor executor) {
        this.itemDataSource = itemDataRepository;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ItemViewModel.class)) {
            return (T) new ItemViewModel(itemDataSource, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
