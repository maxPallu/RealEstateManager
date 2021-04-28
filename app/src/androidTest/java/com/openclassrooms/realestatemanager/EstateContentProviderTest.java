package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.database.Database;
import com.openclassrooms.realestatemanager.provider.EstateContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class EstateContentProviderTest {

    private ContentResolver mContentResolver;

    public static long USER_ID = 1;

    @Before
    public void setup() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(), Database.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver();
    }

    @Test
    public void getItemsWhenNoItemInserted() {
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(EstateContentProvider.URI_ITEM, USER_ID), null,
                null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("estateType")), is("House"));
    }

    private ContentValues generateItem() {
        final ContentValues values = new ContentValues();
        values.put("estateType", "House");
        values.put("estateCity", "Rouen");
        values.put("estatePrice", "17850000");
        values.put("estateAdress", "Rue du Gros-Horloge");
        return values;
    }
}
