package com.openclassrooms.realestatemanager;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.DI.DI;
import com.openclassrooms.realestatemanager.database.Database;
import com.openclassrooms.realestatemanager.repositories.ItemDataRepository;
import com.openclassrooms.realestatemanager.service.EstateAPI;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String type;
    private TextInputLayout price;
    private TextInputLayout surface;
    private TextInputEditText city;
    private String numberRoom;
    private TextInputEditText adress;

    private EstateAPI mApi;
    private ItemDataRepository itemDataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mApi = DI.getEstateApiService();

        Spinner typeSpinner = findViewById(R.id.spinnerType);
        Spinner roomSpinner = findViewById(R.id.spinnerRoom);
        Button add = findViewById(R.id.addButton);

        price = findViewById(R.id.textPrice);
        surface = findViewById(R.id.textSurface);
        city = findViewById(R.id.cityText);
        adress = findViewById(R.id.textAdress);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(AddActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.type));
        ArrayAdapter<String> roomAdapter = new ArrayAdapter<String>(AddActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.number));

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        roomSpinner.setAdapter(roomAdapter);

        typeSpinner.setOnItemSelectedListener(this);
        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numberRoom = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EstateItem item = new EstateItem(type, price.getEditText().getText().toString(),
                        surface.getEditText().getText().toString(), numberRoom, city.getText().toString(), adress.getText().toString());

                mApi.createEstate(item);
                itemDataRepository.createItem(item);

                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String typeSelected = adapterView.getItemAtPosition(i).toString();
        type = typeSelected;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}