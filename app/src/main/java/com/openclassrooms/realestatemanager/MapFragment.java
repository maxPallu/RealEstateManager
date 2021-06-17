package com.openclassrooms.realestatemanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.openclassrooms.realestatemanager.DI.DI;
import com.openclassrooms.realestatemanager.Injection.Injection;
import com.openclassrooms.realestatemanager.Injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.database.Database;
import com.openclassrooms.realestatemanager.models.Result;
import com.openclassrooms.realestatemanager.util.ApiCalls;
import com.openclassrooms.realestatemanager.util.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnPoiClickListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, ApiCalls.Callbacks {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean permissionDenied = false;
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ArrayList<EstateItem> mEstateItems = new ArrayList<>();
    private ItemViewModel itemViewModel;
    long estateId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this.getContext());
        this.itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        this.itemViewModel.getAllItems().observe(getViewLifecycleOwner(), new Observer<List<EstateItem>>() {
            @Override
            public void onChanged(List<EstateItem> estateItems) {
                mEstateItems.addAll(estateItems);

                for(EstateItem item: mEstateItems) {
                    String address = item.getEstateAdress()+" "+item.getEstateCity();
                    ApiCalls.fetchLocations(new ApiCalls.Callbacks() {
                        @Override
                        public void onResponse(List<Result> locations) {
                            setupMarkers(locations, item);
                        }

                        @Override
                        public void onFailure() {

                        }
                    }, address);
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getDeviceLocation();
        enableMyLocation();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                LatLng latLng = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                mMap.moveCamera(cameraUpdate);
                return false;
            }
        });
        googleMap.setOnMyLocationClickListener(this);
        enableMyLocation();

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int estateId = Integer.parseInt(marker.getTag().toString());

                for(EstateItem item: mEstateItems) {
                    String city = String.valueOf(item.getEstateCity());
                    if (item.getEstateId() == estateId) {
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("estatePrice", item.getEstatePrice());
                        intent.putExtra("estateType", marker.getTitle());
                        intent.putExtra("estateCity", item.getEstateCity());
                        intent.putExtra("estatePrice", item.getEstatePrice());
                        intent.putExtra("estateRoom", item.getEstateRoom());
                        intent.putExtra("estateSurface", item.getEstateSurface());
                        intent.putExtra("estateAdress", item.getEstateAdress());
                        intent.putExtra("estatePicture", item.getEstatePictureUri());
                        intent.putExtra("estateDescription", item.getEstateDescription());
                        intent.putExtra("estateId", estateId);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {
        Toast.makeText(this.getContext(), "Clicked: " +
                        pointOfInterest.name + "\nPlace ID:" + pointOfInterest.placeId +
                        "\nLatitude:" + pointOfInterest.latLng.latitude +
                        " Longitude:" + pointOfInterest.latLng.longitude,
                Toast.LENGTH_SHORT).show();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            PermissionUtils.requestPermission((AppCompatActivity) this.getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    private void setupMarkers(List<Result> locations, EstateItem item) {
        if(locations != null && locations.isEmpty() == false) {
            double lat = locations.get(0).getGeometry().getLocation().getLat();
            double lng = locations.get(0).getGeometry().getLocation().getLng();
            LatLng latLng = new LatLng(lat, lng);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(item.getEstateType()));
            marker.setTag(item.getEstateId());
        }
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getContext());

        try {
            if(permissionDenied == false) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 16));
                        }
                    }
                });
            }
        } catch (SecurityException e) {

        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }


    @Override
    public void onMyLocationClick(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        mMap.moveCamera(cameraUpdate);
    }


    @Override
    public void onResponse(@Nullable List<Result> locations) {

    }

    @Override
    public void onFailure() {

    }
}