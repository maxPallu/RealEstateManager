package com.openclassrooms.realestatemanager.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.openclassrooms.realestatemanager.EstateItem;

import java.util.List;

@Dao
public interface EstateDao {

    @Query("SELECT * FROM EstateItem WHERE id = :estateId")
    LiveData<List<EstateItem>> getItems(long estateId);

    @Insert
    long insertItem(EstateItem item);

    @Update
    int updateItem(EstateItem item);

    @Query("DELETE FROM EstateItem WHERE id = :itemId")
    int deleteItem(long itemId);

}
