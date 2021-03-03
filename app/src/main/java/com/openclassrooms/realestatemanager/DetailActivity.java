package com.openclassrooms.realestatemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView detailSurface;
    private TextView detailRoom;
    private TextView detailCity;
    private TextView detailAdress;

    private String estateSurface;
    private String estateRoom;
    private String estateCity;
    private String estateAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailSurface = findViewById(R.id.surfaceText);
        detailRoom = findViewById(R.id.numberRoomText);
        detailCity = findViewById(R.id.locationCity);
        detailAdress = findViewById(R.id.locationAdress);

        Intent intent = getIntent();

        estateSurface = intent.getStringExtra("estateSurface");
        estateRoom = intent.getStringExtra("estateRoom");
        estateCity = intent.getStringExtra("estateCity");
        estateAdress = intent.getStringExtra("estateAdress");

        detailSurface.setText(estateSurface + " sq m");
        detailRoom.setText(estateRoom);
        detailCity.setText(estateCity);
        detailAdress.setText(estateAdress);
    }
}