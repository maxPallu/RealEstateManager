package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.EstateItem;

import java.util.List;

@Dao
public interface EstateDao {

    @Query("SELECT * FROM EstateItem WHERE id = :estateId")
    LiveData<List<EstateItem>> getItems(long estateId);

    @Query("SELECT * FROM EstateItem")
    LiveData<List<EstateItem>> getAllItems();

    @Insert
    long insertItem(EstateItem item);

    @Update
    int updateItem(EstateItem item);

    @Query("DELETE FROM EstateItem WHERE id = :itemId")
    int deleteItem(long itemId);

    @Query("SELECT * FROM EstateItem WHERE id = :estateId")
    Cursor getItemsWithCursors(long estateId);

}
