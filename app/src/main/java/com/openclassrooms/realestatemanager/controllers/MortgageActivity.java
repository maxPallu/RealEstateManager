package com.openclassrooms.realestatemanager.controllers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;

import java.text.DecimalFormat;

public class MortgageActivity extends AppCompatActivity {


    private double mPrice;
    private double mContribution;
    private double mLength;
    private double mRate;

    private TextInputEditText price;
    private TextInputEditText contribution;
    private TextInputEditText length;
    private TextInputEditText rate;
    private TextView mortgageText;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mortgage);

        price = findViewById(R.id.inputMortgagePrice);
        contribution = findViewById(R.id.inputContribution);
        length = findViewById(R.id.inputLength);
        rate = findViewById(R.id.inputRate);
        Button calculate = findViewById(R.id.mortgageButton);
        mortgageText = findViewById(R.id.mortgageText);
        ImageButton back = findViewById(R.id.finish);

        back.setOnClickListener(view -> finish());

        calculate.setOnClickListener(view -> {
            String getPrice = price.getEditableText().toString();
            String getContribution = contribution.getEditableText().toString();
            String getLength = length.getEditableText().toString();
            String getRate = rate.getEditableText().toString();

            mPrice = Integer.parseInt(getPrice);
            mContribution = Integer.parseInt(getContribution);
            mLength = Integer.parseInt(getLength);
            mRate = Integer.parseInt(getRate);

            double ratePercentage = 1 + (mRate/100);

            double mortgage = ((mPrice-mContribution) * ratePercentage) / mLength;

            int length = (int) mLength;

            mortgageText.setText("You'll pay " + new DecimalFormat("##.##").format(mortgage) + " $ per months during " +length+ " months.");
        });
    }
}