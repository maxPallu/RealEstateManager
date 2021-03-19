package com.openclassrooms.realestatemanager.database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;

import com.openclassrooms.realestatemanager.EstateItem;
import com.openclassrooms.realestatemanager.database.dao.EstateDao;

import javax.security.auth.callback.Callback;

@androidx.room.Database(entities = EstateItem.class, version = 1, exportSchema = false)
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

                db.insert("EstateItem", OnConflictStrategy.IGNORE, contentValues);
            }
        };
    }

}
