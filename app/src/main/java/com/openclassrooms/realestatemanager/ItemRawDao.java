package com.openclassrooms.realestatemanager;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface ItemRawDao {
    @RawQuery(observedEntities = EstateItem.class)
    List<EstateItem> getItems(SupportSQLiteQuery query);
}
