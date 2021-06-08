package com.openclassrooms.realestatemanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.DI.DI;
import com.openclassrooms.realestatemanager.Injection.Injection;
import com.openclassrooms.realestatemanager.Injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.service.EstateAPI;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String type;
    private TextInputLayout price;
    private TextInputLayout surface;
    private TextInputLayout city;
    private String numberRoom;
    private String numberSurface;
    private TextInputLayout description;
    private TextInputLayout adress;
    private ImageView viewPhoto;
    private Spinner typeSpinner;
    private Button edit;
    private int  position;

    private EstateAPI mApi;
    private ItemViewModel itemViewModel;
    private EstateItem item;

    private ArrayList<EstateItem> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mApi = DI.getEstateApiService();

        typeSpinner = findViewById(R.id.editSpinnerType);
        Spinner roomSpinner = findViewById(R.id.editSpinnerRoom);
        edit = findViewById(R.id.editButton);

        description = findViewById(R.id.editTextDescription);
        price = findViewById(R.id.editTextPrice);
        surface = findViewById(R.id.editTextSurface);
        city = findViewById(R.id.editCityText);
        adress = findViewById(R.id.editTextAdress);

        Intent intent = getIntent();
        String getAdress = intent.getStringExtra("estateAdress");
        String getCity = intent.getStringExtra("estateCity");
        String getDescription = intent.getStringExtra("estateDescription");
        numberRoom = intent.getStringExtra("estateRoom");
        numberSurface = intent.getStringExtra("estateSurface");
        String getPrice = intent.getStringExtra("estatePrice");
        position = intent.getIntExtra("estateId", 0);

        price.getEditText().setText(getPrice);
        adress.getEditText().setText(getAdress);
        city.getEditText().setText(getCity);
        description.getEditText().setText(getDescription);
        surface.getEditText().setText(numberSurface);

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        this.itemViewModel.getAllItems().observe(this, new Observer<List<EstateItem>>() {
            @Override
            public void onChanged(@Nullable List<EstateItem> estateItems) {
                mItems.clear();
                mItems.addAll(estateItems);
            }
        });

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.type));
        ArrayAdapter<String> roomAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.number));

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        roomSpinner.setAdapter(roomAdapter);

        typeSpinner.setOnItemSelectedListener(this);
        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int number = Integer.parseInt(numberRoom);
                roomSpinner.setSelection(number);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getSurface = surface.getEditText().toString();
                int intSurface = Integer.parseInt(getSurface);
                String getPrice = price.getEditText().toString();
                int intPrice = Integer.parseInt(getPrice);
                int room = Integer.parseInt(numberRoom);

                EstateItem item = new EstateItem(position, type, intPrice,
                        intSurface, room, city.getEditText().getText().toString(), adress.getEditText().getText().toString());

                //item.setEstatePictureUri(image_uri.toString());
                item.setEstateDescription(description.getEditText().getText().toString());

                mApi.editEstate(item, getApplicationContext());
                itemViewModel.updateItem(item, getApplicationContext());

                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        typeSpinner.setSelection(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}