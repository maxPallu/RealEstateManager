package com.openclassrooms.realestatemanager.Injection;

import android.content.Context;

import com.openclassrooms.realestatemanager.database.Database;
import com.openclassrooms.realestatemanager.repositories.ItemDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static ItemDataRepository provideItemDataSource(Context context) {
        Database database = Database.getInstance(context);
        return new ItemDataRepository(database.estateDao());
    }

    public static Executor provideExecutor() { return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        ItemDataRepository itemDataRepository = provideItemDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(itemDataRepository, executor);
    }
}
