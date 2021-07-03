package com.openclassrooms.realestatemanager.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.ui.EstateItem;
import com.openclassrooms.realestatemanager.ui.ImageAdapter;
import com.openclassrooms.realestatemanager.Injection.Injection;
import com.openclassrooms.realestatemanager.Injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.ItemViewModel;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Result;
import com.openclassrooms.realestatemanager.util.ApiCalls;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback, ApiCalls.Callbacks {

    private TextView detailSurface;
    private TextView detailRoom;
    private TextView detailCity;
    private TextView detailAdress;
    private TextView detailDescription;

    private int estateSurface;
    private int estateRoom;
    private int estateYear;
    private int estateMonth;
    private int estateDay;
    private int estateEntryYear;
    private int estateEntryMonth;
    private int estateEntryDay;
    private int estatePrice;
    private String estateCity;
    private String estateAdress;
    private String estateDescription;
    private String estateSeller;
    private String estateAvailable;
    private String estateType;
    private String estateUri;
    private TextView detailSeller;
    private ImageButton back;
    private ImageButton edit;
    private TextView date;
    private TextView detailEntry;

    private int estateId;

    private GoogleMap map;

    public DetailActivity() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);

        getDatas();

        String textSurface = String.valueOf(estateSurface);
        String textRoom = String.valueOf(estateRoom);

        detailSurface.setText(textSurface + " sq m");
        detailRoom.setText(textRoom);
        detailCity.setText(estateCity);
        detailAdress.setText(estateAdress);
        detailDescription.setText(estateDescription);

        if(estateAvailable.contains("Sold")) {
            date.setText(estateDay+"/"+estateMonth+"/"+estateYear);
        } else {
            date.setText("Still available");
        }
        detailEntry.setText(estateEntryDay+"/"+estateEntryMonth+"/"+estateEntryYear);
        detailSeller.setText(estateSeller);

        RecyclerView mRecyclerView = findViewById(R.id.photoRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.Adapter mAdapter = new ImageAdapter(getApplicationContext(), estateUri);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        back.setOnClickListener(v -> finish());

        editItem();
    }

    private void initView() {
        detailSurface = findViewById(R.id.surfaceText);
        detailRoom = findViewById(R.id.numberRoomText);
        detailCity = findViewById(R.id.locationCity);
        detailAdress = findViewById(R.id.locationAdress);
        detailDescription = findViewById(R.id.detailDescription);
        detailSeller = findViewById(R.id.sellerText);
        back = findViewById(R.id.back);
        edit = findViewById(R.id.editButton);
        date = findViewById(R.id.showDate);
        detailEntry = findViewById(R.id.entryText);
    }

    private void editItem() {
        edit.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), EditActivity.class);
            intent1.putExtra("estateCity", estateCity);
            intent1.putExtra("estateType", estateType);
            intent1.putExtra("estateDescription", estateDescription);
            intent1.putExtra("estateAdress", estateAdress);
            intent1.putExtra("estateRoom", estateRoom);
            intent1.putExtra("estateSurface", estateSurface);
            intent1.putExtra("estatePrice", estatePrice);
            intent1.putExtra("estateYear", estateYear);
            intent1.putExtra("estateMonth", estateMonth);
            intent1.putExtra("estateDay", estateDay);
            intent1.putExtra("estateEntryDay", estateEntryDay);
            intent1.putExtra("estateEntryMonth", estateEntryMonth);
            intent1.putExtra("estateEntryYear", estateEntryYear);
            intent1.putExtra("estateSeller", estateSeller);
            intent1.putExtra("estateAvailable", estateAvailable);
            intent1.putExtra("estateId", estateId);
            startActivity(intent1);
        });
    }

    private void getDatas() {
        Intent intent = getIntent();

        estatePrice = intent.getIntExtra("estatePrice", 0);
        estateType = intent.getStringExtra("estateType");
        estateId = intent.getIntExtra("estateId", 0);
        estateSurface = intent.getIntExtra("estateSurface", 0);
        estateRoom = intent.getIntExtra("estateRoom", 0);
        estateCity = intent.getStringExtra("estateCity");
        estateAdress = intent.getStringExtra("estateAdress");
        estateUri = intent.getStringExtra("estatePicture");
        estateDescription = intent.getStringExtra("estateDescription");
        estateSeller = intent.getStringExtra("estateSeller");
        estateYear = intent.getIntExtra("estateYear", 0);
        estateMonth = intent.getIntExtra("estateMonth", 0);
        estateDay = intent.getIntExtra("estateDay", 0);
        estateEntryYear = intent.getIntExtra("estateEntryYear", 0);
        estateEntryMonth = intent.getIntExtra("estateEntryMonth", 0);
        estateEntryDay = intent.getIntExtra("estateEntryDay", 0);
        estateAvailable = intent.getStringExtra("estateAvailable");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<EstateItem> mEstates = new ArrayList<>();

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        ItemViewModel itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(this, estateItems -> {
            mEstates.clear();
            mEstates.addAll(estateItems);

            Intent intent = getIntent();
            estateId = intent.getIntExtra("estateId", 0);

            String textSurface = String.valueOf(mEstates.get(estateId).getEstateSurface());
            String textRoom = String.valueOf(mEstates.get(estateId).getEstateRoom());

            detailSurface.setText(textSurface + " sq m");
            detailRoom.setText(textRoom);
            detailCity.setText(mEstates.get(estateId).getEstateCity());
            detailAdress.setText(mEstates.get(estateId).getEstateAdress());
            detailDescription.setText(mEstates.get(estateId).getEstateDescription());
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        getLocation();
    }

    private void getLocation() {
        ApiCalls.fetchLocations(this, estateAdress);
    }

    private void setupMarkers(List<Result> locations) {
        if(locations != null && !locations.isEmpty()) {
            for(int i =0; i<locations.size(); i++) {
                double lat = locations.get(i).getGeometry().getLocation().getLat();
                double lng = locations.get(i).getGeometry().getLocation().getLng();

                LatLng latLng = new LatLng(lat, lng);
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(estateCity));

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            }
        }
    }

    @Override
    public void onResponse(@Nullable List<Result> locations) {
        setupMarkers(locations);
    }

    @Override
    public void onFailure() {

    }
}