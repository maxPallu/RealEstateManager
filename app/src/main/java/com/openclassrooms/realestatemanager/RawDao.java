package com.openclassrooms.realestatemanager;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

public class RawDao {

    @Dao
    interface itemRawDao {
        @RawQuery(observedEntities = EstateItem.class)
        LiveData<List<EstateItem>> getItems(SupportSQLiteQuery query);
    }

}
