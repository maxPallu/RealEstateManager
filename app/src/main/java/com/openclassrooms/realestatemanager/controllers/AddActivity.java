package com.openclassrooms.realestatemanager.controllers;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.openclassrooms.realestatemanager.ui.EstateItem;
import com.openclassrooms.realestatemanager.Injection.Injection;
import com.openclassrooms.realestatemanager.Injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.ui.ItemViewModel;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.service.EstateAPI;

import java.util.ArrayList;
import java.util.Objects;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private String type;
    private TextInputLayout price;
    private TextInputLayout surface;
    private TextInputEditText city;
    private String numberRoom;
    private String sellerName;
    private String available;
    private TextInputLayout description;
    private TextInputEditText address;
    private ImageView viewPhoto;
    private int day;
    private int month;
    private int year;
    private int entryYear;
    private int entryMonth;
    private int entryDay;
    private Spinner typeSpinner;
    private Spinner roomSpinner;
    private Spinner sellerSpinner;
    private Spinner availableSpinner;
    private Button add;
    private Button mTakePhoto;
    private Button galleryPhoto;

    private TextView date;
    private TextView entryDate;
    private int picker = 0;

    private static final int PERMISSION_CODE = 1000;
    private static int selectType = 0;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int PICK_IMAGE = 1002;

    Uri image_uri;

    private EstateAPI mApi;
    private ItemViewModel itemViewModel;
    private EstateItem item;

    private final ArrayList<EstateItem> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mApi = DI.getEstateApiService();

        initView();

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        this.itemViewModel.getAllItems().observe(this, estateItems -> {
            mItems.clear();
            assert estateItems != null;
            mItems.addAll(estateItems);
        });

        configureSpinners();

        buttonsClickListeners();

        addItem();
    }

    public void showDatePickerDialog(View v) {
        picker = 0;
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showEntryDatePickerDialog(View v) {
        picker = 1;
        DialogFragment fragmentEntry = new EntryDatePickerFragment();
        fragmentEntry.show(getSupportFragmentManager(), "entryDatePicker");
    }

    private void buttonsClickListeners() {
        mTakePhoto.setOnClickListener(view -> {
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
        });

        galleryPhoto.setOnClickListener(v -> {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_DENIED &&
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED) {
                    selectType = 2;
                    pickImageFromGallery();
                } else {
                    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permission, PERMISSION_CODE);
                }
            } else {
                selectType = 2;
                pickImageFromGallery();
            }
        });
    }

    private void addItem() {
        add.setOnClickListener(view -> {

            String getSurface = Objects.requireNonNull(surface.getEditText()).getText().toString();
            int intSurface = Integer.parseInt(getSurface);
            String getPrice = Objects.requireNonNull(price.getEditText()).getText().toString();
            int intPrice = Integer.parseInt(getPrice);
            int room = Integer.parseInt(numberRoom);

            item = new EstateItem(type, intPrice,
                    intSurface, room, Objects.requireNonNull(city.getText()).toString(), Objects.requireNonNull(address.getText()).toString(), entryYear, entryMonth, entryDay,year, month, day, sellerName, available);

            item.setEstatePictureUri(image_uri.toString());
            item.setEstateDescription(Objects.requireNonNull(description.getEditText()).getText().toString());

            mApi.createEstate(item);
            itemViewModel.createItem(item);

            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker datePicker, int y, int m, int dayOfMonth) {
        if(picker == 0) {
            year = y;
            month = m+1;
            day = dayOfMonth;
            date.setText(day+"/"+month+"/"+year);
        } else if (picker == 1) {
            entryYear = y;
            entryMonth = m+1;
            entryDay = dayOfMonth;
            entryDate.setText(entryDay+"/"+entryMonth+"/"+entryYear);
        }
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
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (selectType == 1) {
                    openCamera();
                } else if (selectType == 2) {
                    pickImageFromGallery();
                }
            } else {
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {

        typeSpinner = findViewById(R.id.spinnerType);
        roomSpinner = findViewById(R.id.spinnerRoom);
        sellerSpinner = findViewById(R.id.spinnerSeller);
        availableSpinner = findViewById(R.id.spinnerAvailable);
        add = findViewById(R.id.addButton);
        mTakePhoto = findViewById(R.id.buttonCamera);
        viewPhoto = findViewById(R.id.viewPhoto);
        galleryPhoto = findViewById(R.id.buttonGallery);
        date = findViewById(R.id.displayDate);
        entryDate = findViewById(R.id.displayEntry);

        description = findViewById(R.id.textDescription);

        price = findViewById(R.id.textPrice);
        surface = findViewById(R.id.textSurface);
        city = findViewById(R.id.cityText);
        address = findViewById(R.id.textAdress);
    }

    private void configureSpinners() {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(AddActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.type));
        ArrayAdapter<String> roomAdapter;
        roomAdapter = new ArrayAdapter<>(AddActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.number));
        ArrayAdapter<String> sellerAdapter = new ArrayAdapter<>(AddActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sellers));
        ArrayAdapter<String> availableAdapter = new ArrayAdapter<>(AddActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.available));

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
        availableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                available = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && selectType == 1) {
            viewPhoto.setImageURI(image_uri);
        } else if(resultCode == RESULT_OK && selectType == 2) {
            assert data != null;
            image_uri = data.getData();
            viewPhoto.setImageURI(data.getData());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        type = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}