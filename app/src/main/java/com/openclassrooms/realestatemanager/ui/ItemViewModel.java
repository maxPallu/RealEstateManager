package com.openclassrooms.realestatemanager.ui;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.Injection.Injection;
import com.openclassrooms.realestatemanager.repositories.ItemDataRepository;
import com.openclassrooms.realestatemanager.ui.EstateItem;

import java.util.List;
import java.util.concurrent.Executor;

public class ItemViewModel extends ViewModel {

    private ItemDataRepository itemDataSource;
    private final Executor executor;

    @Nullable
    private LiveData<List<EstateItem>> currentItem;

    public ItemViewModel(ItemDataRepository itemDataSource, Executor executor) {
        this.itemDataSource = itemDataSource;
        this.executor = executor;
    }

    public void init(long id) {
        if(this.currentItem != null) {
            return;
        }
        currentItem = itemDataSource.getItems(id);
    }

    public LiveData<List<EstateItem>> getItem(long id) { return itemDataSource.getItems(id); }

    public void createItem(EstateItem item) {
        executor.execute(() -> {
            itemDataSource.createItem(item);
        });
    }

    public void deleteItem(long id) {
        executor.execute(() -> {
            itemDataSource.deleteItem(id);
        });
    }

    public void updateItem(EstateItem item, Context context) {
        this.itemDataSource = Injection.provideItemDataSource(context);
        executor.execute(() -> {
            itemDataSource.updateItem(item);
        });
    }

    public LiveData<List<EstateItem>> getAllItems() {
        currentItem = itemDataSource.getAllItems();
        return currentItem;
    }

}
