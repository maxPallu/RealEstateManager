package com.openclassrooms.realestatemanager.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.openclassrooms.realestatemanager.EstateItem;
import com.openclassrooms.realestatemanager.database.dao.EstateDao;

import javax.security.auth.callback.Callback;

@android.arch.persistence.room.Database(entities = EstateItem.class, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static volatile Database INSTANCE;

    public abstract EstateDao estateDao();

    public static Database getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase() {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues contentValues = new ContentValues();
                contentValues.put("id", 0);
                contentValues.put("estateType", "House");
                contentValues.put("estatePrice", "250000");
                contentValues.put("estateCity", "Los Angeles");

                db.insert("Estate", OnConflictStrategy.IGNORE, contentValues);
            }
        };
    }

}
