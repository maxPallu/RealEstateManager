package com.openclassrooms.realestatemanager.database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;

import com.openclassrooms.realestatemanager.EstateItem;
import com.openclassrooms.realestatemanager.ItemRawDao;
import com.openclassrooms.realestatemanager.database.dao.EstateDao;

import javax.security.auth.callback.Callback;

@androidx.room.Database(entities = EstateItem.class, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static volatile Database INSTANCE;

    public abstract EstateDao estateDao();
    public abstract ItemRawDao itemRawDao();

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
                contentValues.put("estateId", 0);
                contentValues.put("estateType", "House");
                contentValues.put("estatePrice", 250000);
                contentValues.put("estateSurface", 20);
                contentValues.put("estateRoom", 5);
                contentValues.put("estateYear", 2020);
                contentValues.put("estateMonth", 10);
                contentValues.put("estateDay", 25);
                contentValues.put("estateEntryDay", 15);
                contentValues.put("estateEntryMonth", 8);
                contentValues.put("estateEntryYear", 2020);
                contentValues.put("estateCity", "Los Angeles");
                contentValues.put("estateAvailable", "Available");
                contentValues.put("estatePictureUri", "https://thumbs.dreamstime.com/b/berlin-headquarters-deutsche-bahn-potsdamer-platz-56075878.jpg");

                db.insert("EstateItem", OnConflictStrategy.IGNORE, contentValues);
            }
        };
    }

}
