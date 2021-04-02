package com.openclassrooms.realestatemanager;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.List;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback, ApiCalls.Callbacks {

    private TextView detailSurface;
    private TextView detailRoom;
    private TextView detailCity;
    private TextView detailAdress;

    private ImageView detailPicture;
    private String estateUri;

    private String estateSurface;
    private String estateRoom;
    private String estateCity;
    private String estateAdress;

    private GoogleMap map;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailSurface = findViewById(R.id.surfaceText);
        detailRoom = findViewById(R.id.numberRoomText);
        detailCity = findViewById(R.id.locationCity);
        detailAdress = findViewById(R.id.locationAdress);
        detailPicture = (ImageView) findViewById(R.id.pictureView);

        back = findViewById(R.id.back);

     //  back.setOnClickListener(new View.OnClickListener() {
     //      @Override
     //      public void onClick(View view) {
     //          finish();
     //      }
     //  });

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);
        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);

        Intent intent = getIntent();

        estateSurface = intent.getStringExtra("estateSurface");
        estateRoom = intent.getStringExtra("estateRoom");
        estateCity = intent.getStringExtra("estateCity");
        estateAdress = intent.getStringExtra("estateAdress");
        estateUri = intent.getStringExtra("estatePicture");

        detailSurface.setText(estateSurface + " sq m");
        detailRoom.setText(estateRoom);
        detailCity.setText(estateCity);
        detailAdress.setText(estateAdress);

        Uri pictureUri = Uri.parse(estateUri);
        detailPicture.setImageURI(pictureUri);
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

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f));
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