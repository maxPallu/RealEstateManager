package com.openclassrooms.realestatemanager;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.DI.DI;

public class DetailActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailSurface = findViewById(R.id.surfaceText);
        detailRoom = findViewById(R.id.numberRoomText);
        detailCity = findViewById(R.id.locationCity);
        detailAdress = findViewById(R.id.locationAdress);
        detailPicture = (ImageView) findViewById(R.id.pictureView);

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
}