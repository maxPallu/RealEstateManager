package com.openclassrooms.realestatemanager;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.DI.DI;
import com.openclassrooms.realestatemanager.Injection.Injection;
import com.openclassrooms.realestatemanager.Injection.ViewModelFactory;
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

    private ImageView detailPicture;
    private String estateUri;

    private String estateSurface;
    private String estateRoom;
    private String estateCity;
    private String estateAdress;
    private String estateDescription;

    private ItemViewModel itemViewModel;

    private int estateId;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private GoogleMap map;
    private ImageButton back;
    private ImageButton edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailSurface = findViewById(R.id.surfaceText);
        detailRoom = findViewById(R.id.numberRoomText);
        detailCity = findViewById(R.id.locationCity);
        detailAdress = findViewById(R.id.locationAdress);
        detailPicture = (ImageView) findViewById(R.id.pictureView);
        detailDescription = findViewById(R.id.detailDescription);
        back = findViewById(R.id.back);
        edit = findViewById(R.id.editButton);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);
        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);

        Intent intent = getIntent();

        String estatePrice = intent.getStringExtra("estatePrice");
        estateId = intent.getIntExtra("estateId", 0);
        estateSurface = intent.getStringExtra("estateSurface");
        estateRoom = intent.getStringExtra("estateRoom");
        estateCity = intent.getStringExtra("estateCity");
        estateAdress = intent.getStringExtra("estateAdress");
        estateUri = intent.getStringExtra("estatePicture");
        estateDescription = intent.getStringExtra("estateDescription");

        detailSurface.setText(estateSurface + " sq m");
        detailRoom.setText(estateRoom);
        detailCity.setText(estateCity);
        detailAdress.setText(estateAdress);
        detailDescription.setText(estateDescription);

        //Uri pictureUri = Uri.parse(estateUri);
        // detailPicture.setImageURI(pictureUri);

        mRecyclerView = findViewById(R.id.photoRecyclerView);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new ImageAdapter(getApplicationContext(), estateUri);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                intent.putExtra("estateCity", estateCity);
                intent.putExtra("estateDescription", estateDescription);
                intent.putExtra("estateAdress", estateAdress);
                intent.putExtra("estateRoom", estateRoom);
                intent.putExtra("estateSurface", estateSurface);
                intent.putExtra("estatePrice", estatePrice);
                intent.putExtra("estateId", estateId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<EstateItem> mEstates = new ArrayList<>();

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        this.itemViewModel.getAllItems().observe(this, new Observer<List<EstateItem>>() {
            @Override
            public void onChanged(List<EstateItem> estateItems) {
                mEstates.clear();
                mEstates.addAll(estateItems);

                Intent intent = getIntent();
                estateId = intent.getIntExtra("estateId", 0);

                detailSurface.setText(mEstates.get(estateId).getEstateSurface() + " sq m");
                detailRoom.setText(mEstates.get(estateId).getEstateRoom());
                detailCity.setText(mEstates.get(estateId).getEstateCity());
                detailAdress.setText(mEstates.get(estateId).getEstateAdress());
                detailDescription.setText(mEstates.get(estateId).getEstateDescription());
            }
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
        if(locations != null && locations.isEmpty() == false) {
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