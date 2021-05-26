package com.openclassrooms.realestatemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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

    private Button search;

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

        search = findViewById(R.id.searchButton);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                getMinPrice = minPrice.getText().toString();
                getMaxPrice = maxPrice.getText().toString();
                getMinSurface = minSurface.getText().toString();
                getMaxSurface = maxSurface.getText().toString();
                getMinRoom = minRoom.getText().toString();
                getMaxRoom = maxRoom.getText().toString();

                intent.putExtra("minPrice", getMinPrice);
                intent.putExtra("maxPrice", getMaxPrice);
                intent.putExtra("minSurface", getMinSurface);
                intent.putExtra("maxSurface", getMaxSurface);
                intent.putExtra("minRoom", getMinRoom);
                intent.putExtra("maxRoom", getMaxRoom);

                finish();
            }
        });
    }
}