package com.openclassrooms.realestatemanager.controllers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.google.android.gms.tasks.Task;
import com.openclassrooms.realestatemanager.Injection.Injection;
import com.openclassrooms.realestatemanager.Injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Result;
import com.openclassrooms.realestatemanager.ui.EstateItem;
import com.openclassrooms.realestatemanager.ui.ItemViewModel;
import com.openclassrooms.realestatemanager.util.ApiCalls;
import com.openclassrooms.realestatemanager.util.PermissionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnPoiClickListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, ApiCalls.Callbacks {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final ArrayList<EstateItem> mEstateItems = new ArrayList<>();
    long estateId;

    public MapFragment(DetailFragment detailFragment) {
    }

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
        ItemViewModel itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(getViewLifecycleOwner(), estateItems -> {
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
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getDeviceLocation();
        enableMyLocation();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        googleMap.setOnMyLocationButtonClickListener(() -> {
            LatLng latLng = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
            mMap.moveCamera(cameraUpdate);
            return false;
        });
        googleMap.setOnMyLocationClickListener(this);
        enableMyLocation();

        mMap.setOnInfoWindowClickListener(marker -> {
            int estateId = Integer.parseInt(Objects.requireNonNull(marker.getTag()).toString());

            boolean is_tablet = getResources().getBoolean(R.bool.is_tablet);

            if(is_tablet) {
                for(EstateItem item: mEstateItems) {
                    if(item.getEstateId() == estateId) {
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
                        intent.putExtra("estateYear", item.getEstateYear());
                        intent.putExtra("estateMonth", item.getEstateMonth());
                        intent.putExtra("estateDay", item.getEstateDay());
                        intent.putExtra("estateEntryYear", item.getEstateEntryYear());
                        intent.putExtra("estateEntryMonth", item.getEstateEntryMonth());
                        intent.putExtra("estateEntryDay", item.getEstateEntryDay());
                        intent.putExtra("estateSeller", item.getEstateSeller());
                        intent.putExtra("estateAvailable", item.getEstateAvailable());
                        intent.putExtra("estateId", estateId);
                        startActivity(intent);
                    }
                }
            } else {
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
                        intent.putExtra("estateYear", item.getEstateYear());
                        intent.putExtra("estateMonth", item.getEstateMonth());
                        intent.putExtra("estateDay", item.getEstateDay());
                        intent.putExtra("estateEntryYear", item.getEstateEntryYear());
                        intent.putExtra("estateEntryMonth", item.getEstateEntryMonth());
                        intent.putExtra("estateEntryDay", item.getEstateEntryDay());
                        intent.putExtra("estateSeller", item.getEstateSeller());
                        intent.putExtra("estateAvailable", item.getEstateAvailable());
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
        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
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
        if(locations != null && !locations.isEmpty()) {
            double lat = locations.get(0).getGeometry().getLocation().getLat();
            double lng = locations.get(0).getGeometry().getLocation().getLng();
            LatLng latLng = new LatLng(lat, lng);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(item.getEstateType()));
            marker.setTag(item.getEstateId());
        }
    }

    private void createFragment(Context context, Bundle bundle) {
        DetailFragment detailFragment;

        detailFragment = (DetailFragment) ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag("DetailFragment");

        if(detailFragment != null) {
            detailFragment.setArguments(bundle);
            detailFragment.onResume();
        }

        if(detailFragment == null && ((FragmentActivity) context).findViewById(R.id.frame_layout_detail) != null) {
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(bundle);
            FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.map, fragment).commit();
        }
    }

    private void getDeviceLocation() {
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext());

        try {
            boolean permissionDenied = false;
            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Location currentLocation = (Location) task.getResult();

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 16));
                }
            });
        } catch (SecurityException ignored) {

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