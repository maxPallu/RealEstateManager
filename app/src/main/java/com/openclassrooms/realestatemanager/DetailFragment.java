package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// import com.google.android.gms.maps.GoogleMapOptions;
// import com.google.android.gms.maps.SupportMapFragment;

public class DetailFragment extends Fragment {

    private TextView detailSurface;
    private TextView detailRoom;
    private TextView detailCity;
    private TextView detailAdress;

    private String estateSurface;
    private String estateRoom;
    private String estateCity;
    private String estateAdress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        detailSurface =  view.findViewById(R.id.surfaceText);
        detailRoom = view.findViewById(R.id.numberRoomText);
        detailCity = view.findViewById(R.id.locationCity);
        detailAdress = view.findViewById(R.id.locationAdress);

        createMap(view.getContext());

     //   GoogleMapOptions options = new GoogleMapOptions().liteMode(true);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = this.getArguments();

        if(bundle != null) {
            estateSurface = bundle.getString("estateSurface");
            estateRoom = bundle.getString("estateRoom");
            estateCity = bundle.getString("estateCity");
            estateAdress = bundle.getString("estateAdress");

            detailSurface.setText(estateSurface + " sq m");
            detailRoom.setText(estateRoom);
            detailCity.setText(estateCity);
            detailAdress.setText(estateAdress);
        }
    }

    public void createMap(Context context) {
        // SupportMapFragment mapFragment = SupportMapFragment.newInstance();

    }
}