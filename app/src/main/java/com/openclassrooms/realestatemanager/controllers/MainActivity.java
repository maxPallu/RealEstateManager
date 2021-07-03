package com.openclassrooms.realestatemanager.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.google.android.material.navigation.NavigationView;
import com.openclassrooms.realestatemanager.Injection.Injection;
import com.openclassrooms.realestatemanager.Injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.database.Database;
import com.openclassrooms.realestatemanager.database.dao.ItemRawDao;
import com.openclassrooms.realestatemanager.ui.EstateAdapter;
import com.openclassrooms.realestatemanager.ui.EstateItem;
import com.openclassrooms.realestatemanager.ui.ItemViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Context mContext;
    private ArrayList<EstateItem> mEstates = new ArrayList<>();
    private DrawerLayout drawer;
    private ItemRawDao itemRawDao;
    private DetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        ItemViewModel itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(this, estateItems -> {
            mEstates.clear();
            mEstates.addAll(estateItems);
        });

        this.configureFragment();
        this.configureDetailFragment();
        itemRawDao = Database.getInstance(this).itemRawDao();

        drawer = findViewById(R.id.activity_main_drawer_layout);
        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        mContext = this.getApplicationContext();

        mRecyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new EstateAdapter(mEstates);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        final MenuItem addItem = menu.findItem(R.id.menuAdd);
        final MenuItem searchItem = menu.findItem(R.id.menuSearch);

        addItem.setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(mContext, AddActivity.class);
            startActivity(intent);

            return false;
        });

        searchItem.setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivityForResult(intent, 8);

            return false;
        });

        return true;
    }

    private void configureFragment() {

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_main);

        if(mainFragment == null) {

            mainFragment = new MainFragment();

            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_main, mainFragment).commit();
        }
    }

    private void configureDetailFragment() {

        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_detail);

        if(detailFragment == null && findViewById(R.id.frame_layout_detail) != null) {
            detailFragment = new DetailFragment();

            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_detail, detailFragment, "DetailFragment").commit();
        }
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void searchItems(Intent data) {

        if(data != null) {

            String getMinSurface = data.getStringExtra("minSurface");
            String getMaxSurface = data.getStringExtra("maxSurface");
            String getMinPrice = data.getStringExtra("minPrice");
            String getMaxPrice = data.getStringExtra("maxPrice");
            String getMinRoom = data.getStringExtra("minRoom");
            String getMaxRoom = data.getStringExtra("maxRoom");

            int minSurface = Integer.parseInt(getMinSurface);
            int maxSurface = Integer.parseInt(getMaxSurface);
            int minPrice = Integer.parseInt(getMinPrice);
            int maxPrice = Integer.parseInt(getMaxPrice);
            int minRoom = Integer.parseInt(getMinRoom);
            int maxRoom = Integer.parseInt(getMaxRoom);
            SimpleSQLiteQuery myQuery = new SimpleSQLiteQuery("SELECT * FROM EstateItem WHERE estateRoom >= "+minRoom+" AND estateRoom <= " +maxRoom+
                    " AND estatePrice >= "+minPrice+" AND estatePrice <= "+maxPrice+ " AND estateSurface >= "+minSurface+" AND estateSurface <= "+maxSurface);
            LiveData<List<EstateItem>> estateItems = itemRawDao.getItems(myQuery);
            estateItems.observe(this, estateItems1 -> {
                mEstates = (ArrayList<EstateItem>) estateItems1;
                mAdapter = new EstateAdapter(mEstates);
                mRecyclerView.setAdapter(mAdapter);
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 8) {
            if(resultCode == Activity.RESULT_OK) {
                searchItems(data);
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_estates:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new MainFragment()).commit();
                break;
            case R.id.menu_map:
                boolean is_tablet = getResources().getBoolean(R.bool.is_tablet);
                if(is_tablet) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_detail, new MapFragment(detailFragment)).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new MapFragment(detailFragment)).commit();
                }
                break;
            case R.id.loan_menu:
                Intent intent = new Intent(this, MortgageActivity.class);
                startActivity(intent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
