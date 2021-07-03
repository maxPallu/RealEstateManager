package com.openclassrooms.realestatemanager.controllers;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.DI.DI;
import com.openclassrooms.realestatemanager.Injection.Injection;
import com.openclassrooms.realestatemanager.Injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.service.EstateAPI;
import com.openclassrooms.realestatemanager.ui.EstateItem;
import com.openclassrooms.realestatemanager.ui.ItemViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class EditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String type;
    private TextInputLayout price;
    private TextInputLayout surface;
    private TextInputLayout city;
    private int numberRoom;
    private TextInputLayout description;
    private TextInputLayout adress;
    private Spinner typeSpinner;
    private int  position;
    private Spinner sellerSpinner;
    private Spinner availableSpinner;
    private String estateType;

    private EstateAPI mApi;
    private ItemViewModel itemViewModel;
    private String getSeller;
    private String getAvailable;
    private ImageView viewPhoto;

    private Spinner roomSpinner;
    private Button edit;
    private String getAdress;
    private String getCity;
    private String getDescription;
    private int numberSurface;
    private int getPrice;
    private String uri;

    private final ArrayList<EstateItem> mItems = new ArrayList<>();

    public EditActivity(String type) {
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mApi = DI.getEstateApiService();

        initView();
        getDatas();


        Glide.with(this.getApplicationContext()).load(uri).into(viewPhoto);

        Objects.requireNonNull(price.getEditText()).setText(String.valueOf(getPrice));
        Objects.requireNonNull(adress.getEditText()).setText(getAdress);
        Objects.requireNonNull(city.getEditText()).setText(getCity);
        Objects.requireNonNull(description.getEditText()).setText(getDescription);
        Objects.requireNonNull(surface.getEditText()).setText(String.valueOf(numberSurface));

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        this.itemViewModel.getAllItems().observe(this, estateItems -> {
            mItems.clear();
            mItems.addAll(estateItems);
        });

        configureSpinners();

        itemSelectedListeners();

        editItem();
    }

    private void editItem() {
        edit.setOnClickListener(view -> {
            String getSurface = Objects.requireNonNull(surface.getEditText()).toString();
            int intSurface = Integer.parseInt(getSurface);
            String getPrice1 = Objects.requireNonNull(price.getEditText()).toString();
            int intPrice = Integer.parseInt(getPrice1);

            EstateItem item = new EstateItem(position, type, intPrice,
                    intSurface, numberRoom, Objects.requireNonNull(city.getEditText()).getText().toString(), Objects.requireNonNull(adress.getEditText()).getText().toString());

            item.setEstateDescription(Objects.requireNonNull(description.getEditText()).getText().toString());

            mApi.editEstate(item, getApplicationContext());
            itemViewModel.updateItem(item, getApplicationContext());

            finish();
        });
    }

    private void itemSelectedListeners() {
        typeSpinner.setOnItemSelectedListener(this);
        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                roomSpinner.setSelection(numberRoom);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sellerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Resources res = getResources();
                String[] sellers = res.getStringArray(R.array.sellers);
                ArrayList<String> sellerArray = new ArrayList<>(Arrays.asList(sellers));
                for(int j=0; j<sellerArray.size(); j++) {
                    if(sellerArray.get(j).contains(getSeller)) {
                        sellerSpinner.setSelection(j);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        availableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(getAvailable.contains("Sold")) {
                    availableSpinner.setSelection(1);
                } else {
                    availableSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void configureSpinners() {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(EditActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.type));
        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(EditActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.number));
        ArrayAdapter<String> sellerAdapter = new ArrayAdapter<>(EditActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sellers));
        ArrayAdapter<String> availableAdapter = new ArrayAdapter<>(EditActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.available));

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sellerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        availableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        roomSpinner.setAdapter(roomAdapter);
        sellerSpinner.setAdapter(sellerAdapter);
        availableSpinner.setAdapter(availableAdapter);
    }

    private void initView() {
        typeSpinner = findViewById(R.id.editSpinnerType);
        roomSpinner = findViewById(R.id.editSpinnerRoom);
        sellerSpinner = findViewById(R.id.editSpinnerSeller);
        availableSpinner = findViewById(R.id.editSpinnerAvailable);
        edit = findViewById(R.id.editButton);

        description = findViewById(R.id.editTextDescription);
        price = findViewById(R.id.editTextPrice);
        surface = findViewById(R.id.editTextSurface);
        city = findViewById(R.id.editCityText);
        adress = findViewById(R.id.editTextAdress);
        viewPhoto = findViewById(R.id.editViewPhoto);
    }

    private void getDatas() {
        Intent intent = getIntent();
        getAdress = intent.getStringExtra("estateAdress");
        estateType = intent.getStringExtra("estateType");
        getCity = intent.getStringExtra("estateCity");
        getDescription = intent.getStringExtra("estateDescription");
        numberRoom = intent.getIntExtra("estateRoom", 0);
        numberSurface = intent.getIntExtra("estateSurface", 0);
        getPrice = intent.getIntExtra("estatePrice", 0);
        getSeller = intent.getStringExtra("estateSeller");
        uri = intent.getStringExtra("estatePicture");
        getAvailable = intent.getStringExtra("estateAvailable");
        position = intent.getIntExtra("estateId", 0);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Resources res = getResources();
        String[] type = res.getStringArray(R.array.type);
        ArrayList<String> typeArray = new ArrayList<>(Arrays.asList(type));
        for(int j=0; j<typeArray.size(); j++) {
            if(typeArray.get(j).contains(estateType)) {
                typeSpinner.setSelection(j);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}