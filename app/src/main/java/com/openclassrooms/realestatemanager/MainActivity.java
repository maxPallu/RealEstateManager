package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.openclassrooms.realestatemanager.Injection.Injection;
import com.openclassrooms.realestatemanager.Injection.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    private ArrayList<EstateItem> mEstates = new ArrayList<>();
    private MainFragment mainFragment;
    private DetailFragment detailFragment;
    private ItemViewModel itemViewModel;
    private DrawerLayout drawer;
    private ItemRawDao itemRawDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        this.itemViewModel.getAllItems().observe(this, new Observer<List<EstateItem>>() {
            @Override
            public void onChanged(List<EstateItem> estateItems) {
                mEstates.clear();
                mEstates.addAll(estateItems);
            }
        });

        this.configureFragment();
        this.configureDetailFragment();

        drawer = findViewById(R.id.activity_main_drawer_layout);
        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
        View headerLayout = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        mContext = this.getApplicationContext();

        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new EstateAdapter(mEstates);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        searchItems();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        final MenuItem addItem = menu.findItem(R.id.menuAdd);
        final MenuItem searchItem = menu.findItem(R.id.menuSearch);

        addItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(mContext, AddActivity.class);
                startActivity(intent);

                return false;
            }
        });

        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);

                return false;
            }
        });

        return true;
    }

    private void configureFragment() {

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_main);

        if(mainFragment == null) {

            mainFragment = new MainFragment();

            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_main, mainFragment).commit();
        }
    }

    private void configureDetailFragment() {

        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_detail);

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

    private void searchItems() {
        String getMinSurface;
        String getMaxSurface;
        String getMinPrice;
        String getMaxPrice;

      // int minSurface = Integer.parseInt(getMinSurface);
      // int maxSurface = Integer.parseInt(getMaxSurface);
      // int minPrice = Integer.parseInt(getMinPrice);
      // int maxPrice = Integer.parseInt(getMaxPrice);

       // SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM EstateItem WHERE estateSurface BETWEEN "+minSurface+" AND " +maxSurface+ " WHERE estatePrice BETWEEN "+minPrice+" AND " +maxPrice);
       // LiveData<List<EstateItem>> items = itemRawDao.getItems(query);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_estates:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new MainFragment()).commit();
                break;
            case R.id.menu_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new MapFragment()).commit();
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
