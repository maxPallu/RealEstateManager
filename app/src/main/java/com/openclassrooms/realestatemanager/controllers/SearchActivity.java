package com.openclassrooms.realestatemanager.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;

import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    private TextInputEditText minPrice;
    private TextInputEditText maxPrice;
    private TextInputEditText minSurface;
    private TextInputEditText maxSurface;
    private TextInputEditText minRoom;
    private TextInputEditText maxRoom;

    private String getMinPrice;
    private String getMaxPrice;
    private String getMinSurface;
    private String getMaxSurface;
    private String getMinRoom;
    private String getMaxRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        minPrice = findViewById(R.id.inputMinPrice);
        maxPrice = findViewById(R.id.inputMaxPrice);
        minSurface = findViewById(R.id.inputMinSurface);
        maxSurface = findViewById(R.id.inputMaxSurface);
        minRoom = findViewById(R.id.minNumberRoom);
        maxRoom = findViewById(R.id.maxNumberRoom);

        Button search = findViewById(R.id.searchButton);

        search.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            getMinPrice = Objects.requireNonNull(minPrice.getText()).toString();
            getMaxPrice = Objects.requireNonNull(maxPrice.getText()).toString();
            getMinSurface = Objects.requireNonNull(minSurface.getText()).toString();
            getMaxSurface = Objects.requireNonNull(maxSurface.getText()).toString();
            getMinRoom = Objects.requireNonNull(minRoom.getText()).toString();
            getMaxRoom = Objects.requireNonNull(maxRoom.getText()).toString();

            intent.putExtra("minPrice", getMinPrice);
            intent.putExtra("maxPrice", getMaxPrice);
            intent.putExtra("minSurface", getMinSurface);
            intent.putExtra("maxSurface", getMaxSurface);
            intent.putExtra("minRoom", getMinRoom);
            intent.putExtra("maxRoom", getMaxRoom);

            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }
}