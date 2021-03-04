package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.DI.DI;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    private List<EstateItem> mEstates;
    private MainFragment mainFragment;
    private DetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.configureFragment();
        this.configureDetailFragment();

        mEstates = DI.getEstateApiService().getEstates();

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
        mEstates.clear();
        mEstates.addAll(DI.getEstateApiService().getEstates());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        final MenuItem addItem = menu.findItem(R.id.menuAdd);

        addItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(mContext, AddActivity.class);
                startActivity(intent);

                return false;
            }
        });

        return true;
    }

    private void configureFragment() {

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_main_mobile);

        if(mainFragment == null) {

            mainFragment = new MainFragment();

            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_main_mobile, mainFragment).commit();
        }
    }

    private void configureDetailFragment() {

        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_detail);

        if(detailFragment == null && findViewById(R.id.frame_layout_detail) != null) {
            detailFragment = new DetailFragment();

            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_detail, detailFragment).commit();
        }
    }
}
