package com.openclassrooms.realestatemanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.DI.DI;
import com.openclassrooms.realestatemanager.Injection.Injection;
import com.openclassrooms.realestatemanager.Injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.service.EstateAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String type;
    private TextInputLayout price;
    private TextInputLayout surface;
    private TextInputLayout city;
    private int numberRoom;
    private int numberSurface;
    private TextInputLayout description;
    private TextInputLayout adress;
    private ImageView viewPhoto;
    private Spinner typeSpinner;
    private Button edit;
    private int  position;
    private Spinner sellerSpinner;
    private Spinner availableSpinner;
    private String uri;
    private String estateType;

    private EstateAPI mApi;
    private ItemViewModel itemViewModel;
    private EstateItem item;
    private String getSeller;
    private String getAvailable;

    private ArrayList<EstateItem> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mApi = DI.getEstateApiService();

        typeSpinner = findViewById(R.id.editSpinnerType);
        Spinner roomSpinner = findViewById(R.id.editSpinnerRoom);
        sellerSpinner = findViewById(R.id.editSpinnerSeller);
        availableSpinner = findViewById(R.id.editSpinnerAvailable);
        edit = findViewById(R.id.editButton);

        description = findViewById(R.id.editTextDescription);
        price = findViewById(R.id.editTextPrice);
        surface = findViewById(R.id.editTextSurface);
        city = findViewById(R.id.editCityText);
        adress = findViewById(R.id.editTextAdress);
        viewPhoto = findViewById(R.id.editViewPhoto);

        Intent intent = getIntent();
        String getAdress = intent.getStringExtra("estateAdress");
        estateType = intent.getStringExtra("estateType");
        String getCity = intent.getStringExtra("estateCity");
        String getDescription = intent.getStringExtra("estateDescription");
        numberRoom = intent.getIntExtra("estateRoom", 0);
        numberSurface = intent.getIntExtra("estateSurface", 0);
        int getPrice = intent.getIntExtra("estatePrice", 0);
        getSeller = intent.getStringExtra("estateSeller");
        uri = intent.getStringExtra("estatePicture");
        int estateYear = intent.getIntExtra("estateYear", 0);
        int estateMonth = intent.getIntExtra("estateMonth", 0);
        int estateDay = intent.getIntExtra("estateDay", 0);
        int estateEntryYear = intent.getIntExtra("estateEntryYear", 0);
        int estateEntryMonth = intent.getIntExtra("estateEntryMonth", 0);
        int estateEntryDay = intent.getIntExtra("estateEntryDay", 0);
        getAvailable = intent.getStringExtra("estateAvailable");
        position = intent.getIntExtra("estateId", 0);

        Glide.with(this.getApplicationContext()).load(uri).into(viewPhoto);

        price.getEditText().setText(String.valueOf(getPrice));
        adress.getEditText().setText(getAdress);
        city.getEditText().setText(getCity);
        description.getEditText().setText(getDescription);
        surface.getEditText().setText(String.valueOf(numberSurface));

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
        ArrayAdapter<String> sellerAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sellers));
        ArrayAdapter<String> availableAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.available));

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sellerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        availableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        roomSpinner.setAdapter(roomAdapter);
        sellerSpinner.setAdapter(sellerAdapter);
        availableSpinner.setAdapter(availableAdapter);

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
               ArrayList<String> sellerArray = new ArrayList<String>(Arrays.asList(sellers));
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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getSurface = surface.getEditText().toString();
                int intSurface = Integer.parseInt(getSurface);
                String getPrice = price.getEditText().toString();
                int intPrice = Integer.parseInt(getPrice);

                EstateItem item = new EstateItem(position, type, intPrice,
                        intSurface, numberRoom, city.getEditText().getText().toString(), adress.getEditText().getText().toString());

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
        Resources res = getResources();
        String[] type = res.getStringArray(R.array.type);
        ArrayList<String> typeArray = new ArrayList<String>(Arrays.asList(type));
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