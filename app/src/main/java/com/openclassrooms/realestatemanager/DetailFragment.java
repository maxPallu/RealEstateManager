package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.openclassrooms.realestatemanager.models.Result;
import com.openclassrooms.realestatemanager.util.ApiCalls;

import java.util.List;

// import com.google.android.gms.maps.GoogleMapOptions;
// import com.google.android.gms.maps.SupportMapFragment;

public class DetailFragment extends Fragment implements OnMapReadyCallback, ApiCalls.Callbacks {

    private TextView detailSurface;
    private TextView detailRoom;
    private TextView detailCity;
    private TextView detailAdress;
    private TextView detailDescription;
    private TextView date;
    private TextView detailSeller;
    private TextView detailEntry;

    private ImageView detailPicture;
    private String estateUri;

    private int estateSurface;
    private int estateRoom;
    private String estateCity;
    private String estateAdress;
    private String estateDescription;
    private String estateSeller;
    private String estateAvailable;
    private String estateType;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private GoogleMap map;
    private ImageButton back;
    private ImageButton edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        detailSurface =  view.findViewById(R.id.surfaceText);
        detailRoom = view.findViewById(R.id.numberRoomText);
        detailCity = view.findViewById(R.id.locationCity);
        detailAdress = view.findViewById(R.id.locationAdress);
        detailDescription = view.findViewById(R.id.detailDescription);
        detailSeller = view.findViewById(R.id.sellerText);
        back = view.findViewById(R.id.back);
        edit = view.findViewById(R.id.editButton);
        date = view.findViewById(R.id.showDate);
        detailEntry = view.findViewById(R.id.entryText);

        mRecyclerView = view.findViewById(R.id.photoRecyclerView);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);
        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);

        createMap(view.getContext());

     //   GoogleMapOptions options = new GoogleMapOptions().liteMode(true);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = this.getArguments();

        if(bundle != null) {
            int estatePrice = bundle.getInt("estatePrice");
            estateType = bundle.getString("estateType");
            estateSurface = bundle.getInt("estateSurface");
            estateRoom = bundle.getInt("estateRoom");
            estateCity = bundle.getString("estateCity");
            estateAdress = bundle.getString("estateAdress");
            estateDescription= bundle.getString("estateDescription");
            estateSeller = bundle.getString("estateSeller");
            estateUri = bundle.getString("estatePicture");
            int estateYear = bundle.getInt("estateYear", 0);
            int estateMonth = bundle.getInt("estateMonth", 0);
            int estateDay = bundle.getInt("estateDay", 0);
            int estateEntryYear = bundle.getInt("estateEntryYear", 0);
            int estateEntryMonth = bundle.getInt("estateEntryMonth", 0);
            int estateEntryDay = bundle.getInt("estateEntryDay", 0);
            estateAvailable = bundle.getString("estateAvailable");
            int estateId = bundle.getInt("estateId");

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
            ApiCalls.fetchLocations(this, estateAdress);

            mLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
            mAdapter = new ImageAdapter(this.getContext(), estateUri);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), EditActivity.class);
                    intent.putExtra("estateCity", estateCity);
                    intent.putExtra("estateType", estateType);
                    intent.putExtra("estateDescription", estateDescription);
                    intent.putExtra("estateAdress", estateAdress);
                    intent.putExtra("estateRoom", estateRoom);
                    intent.putExtra("estateSurface", estateSurface);
                    intent.putExtra("estatePrice", estatePrice);
                    intent.putExtra("estateYear", estateYear);
                    intent.putExtra("estateMonth", estateMonth);
                    intent.putExtra("estateDay", estateDay);
                    intent.putExtra("estateEntryDay", estateEntryDay);
                    intent.putExtra("estateEntryMonth", estateEntryMonth);
                    intent.putExtra("estateEntryYear", estateEntryYear);
                    intent.putExtra("estateSeller", estateSeller);
                    intent.putExtra("estateAvailable", estateAvailable);
                    intent.putExtra("estateId", estateId);
                    startActivity(intent);
                }
            });
        }
        }

    public void createMap(Context context) {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
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
    public void onResponse(List<Result> locations) {
        setupMarkers(locations);
    }

    @Override
    public void onFailure() {

    }
}