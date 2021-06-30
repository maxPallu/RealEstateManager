package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.ui.EstateItem;

import java.util.List;

@Dao
public interface ItemRawDao {
    @RawQuery(observedEntities = EstateItem.class)
    LiveData<List<EstateItem>> getItems(SupportSQLiteQuery query);
}
