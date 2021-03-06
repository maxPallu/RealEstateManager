package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.ui.EstateItem;
import com.openclassrooms.realestatemanager.database.dao.EstateDao;

import java.util.List;

public class ItemDataRepository {

    private final EstateDao estateDao;

    public ItemDataRepository(EstateDao estateDao) {
        this.estateDao = estateDao;
    }

    public LiveData<List<EstateItem>> getItems(long id) { return  this.estateDao.getItems(id); }

    public LiveData<List<EstateItem>> getAllItems() { return this.estateDao.getAllItems(); }

    public void createItem(EstateItem item) { estateDao.insertItem(item); }

    public void deleteItem(long id) { estateDao.deleteItem(id); }

    public void updateItem(EstateItem item) { estateDao.updateItem(item); }
}
