package com.openclassrooms.realestatemanager;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.Database;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class EstateDaoTest {

    private Database database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), Database.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    private static int ESTATE_ID = 1;
    private static EstateItem itemDemo = new EstateItem(ESTATE_ID, "House", "250000", "250", "4", "Los Angeles", "25 Test Street");

    @Test
    public void insertAndGetItem() throws InterruptedException {
        this.database.estateDao().insertItem(itemDemo);

        List<EstateItem> item = LiveDataTestUtil.getValue(this.database.estateDao().getItems(ESTATE_ID));
        assertTrue(item.get(0).getEstateCity().equals(itemDemo.getEstateCity()));
    }

    @Test
    public void insertAndDeleteItem() throws InterruptedException {
        this.database.estateDao().insertItem(itemDemo);
        EstateItem estateItem = LiveDataTestUtil.getValue(this.database.estateDao().getItems(ESTATE_ID)).get(0);
        this.database.estateDao().deleteItem(estateItem.getId());

        List<EstateItem> items = LiveDataTestUtil.getValue(this.database.estateDao().getItems(ESTATE_ID));
        assertTrue(items.isEmpty());
    }

}
