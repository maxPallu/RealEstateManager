package com.openclassrooms.realestatemanager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.DI.DI;
import com.openclassrooms.realestatemanager.Injection.Injection;
import com.openclassrooms.realestatemanager.Injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.service.EstateAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private String type;
    private TextInputLayout price;
    private TextInputLayout surface;
    private TextInputEditText city;
    private String numberRoom;
    private String sellerName;
    private TextInputLayout description;
    private TextInputEditText adress;
    private Button mTakePhoto;
    private Button galleryPhoto;
    private ImageView viewPhoto;
    private int day;
    private int month;
    private int year;
    private TextView date;

    private static final int PERMISSION_CODE = 1000;
    private static int selectType = 0;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int PICK_IMAGE = 1002;

    Uri image_uri;

    private EstateAPI mApi;
    private ItemViewModel itemViewModel;
    private EstateItem item;

    private ArrayList<EstateItem> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mApi = DI.getEstateApiService();

        Spinner typeSpinner = findViewById(R.id.spinnerType);
        Spinner roomSpinner = findViewById(R.id.spinnerRoom);
        Spinner sellerSpinner = findViewById(R.id.spinnerSeller);
        Button add = findViewById(R.id.addButton);
        mTakePhoto = findViewById(R.id.buttonCamera);
        viewPhoto = findViewById(R.id.viewPhoto);
        galleryPhoto = findViewById(R.id.buttonGallery);
        date = findViewById(R.id.displayDate);

        description = findViewById(R.id.textDescription);

        price = findViewById(R.id.textPrice);
        surface = findViewById(R.id.textSurface);
        city = findViewById(R.id.cityText);
        adress = findViewById(R.id.textAdress);

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        this.itemViewModel.getAllItems().observe(this, new Observer<List<EstateItem>>() {
            @Override
            public void onChanged(@Nullable List<EstateItem> estateItems) {
                mItems.clear();
                mItems.addAll(estateItems);
            }
        });

        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        selectType = 1;
                        openCamera();
                    }
                } else {
                    selectType = 1;
                    openCamera();
                }
            }
        });

        galleryPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        selectType = 2;
                        pickImageFromGallery();
                    }
                } else {
                    selectType = 2;
                    pickImageFromGallery();
                }
            }
        });


        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(AddActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.type));
        ArrayAdapter<String> roomAdapter = new ArrayAdapter<String>(AddActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.number));
        ArrayAdapter<String> sellerAdapter = new ArrayAdapter<>(AddActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sellers));

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sellerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        roomSpinner.setAdapter(roomAdapter);
        sellerSpinner.setAdapter(sellerAdapter);

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
        sellerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sellerName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getSurface = surface.getEditText().getText().toString();
                int intSurface = Integer.parseInt(getSurface);
                String getPrice = price.getEditText().getText().toString();
                int intPrice = Integer.parseInt(getPrice);
                int room = Integer.parseInt(numberRoom);

                item = new EstateItem(type, intPrice,
                        intSurface, room, city.getText().toString(), adress.getText().toString(), year, month, day, sellerName);

                item.setEstatePictureUri(image_uri.toString());
                item.setEstateDescription(description.getEditText().getText().toString());

                mApi.createEstate(item);
                itemViewModel.createItem(item);

                finish();
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int y, int m, int dayOfMonth) {
        year = y;
        month = m+1;
        day = dayOfMonth;
        date.setText(day+"/"+month+"/"+year);
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    private void pickImageFromGallery() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the gallery");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(selectType == 1) {
                        openCamera();
                    } else if(selectType == 2) {
                        pickImageFromGallery();
                    }
                } else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && selectType == 1) {
            viewPhoto.setImageURI(image_uri);
        } else if(resultCode == RESULT_OK && selectType == 2) {
            image_uri = data.getData();
            viewPhoto.setImageURI(data.getData());
        }
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