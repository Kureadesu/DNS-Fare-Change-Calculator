package com.example.dnsfarecalculator;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BulakanGuiguinto extends AppCompatActivity {

    private final int REGULAR_FARE_AMOUNT = 13;
    private final int DISCOUNTED_FARE_DIFFERENCE = 2;
    private final int DEFAULT_BUTTON_COLOR = 0xFF814622;
    private final int SELECTED_BUTTON_COLOR = 0xFF218C17;

    private int calculatedChange;

    private Button discountedButton, regularButton, clearButton, backButton;
    private TextView displayChangeEditText;
    
    // Buttons for Amount Paid
    private Button btnAmount20, btnAmount50, btnAmount100;
    // Buttons for Passengers
    private Button btnPass1, btnPass2, btnPass3, btnPass4, btnPass5;

    private int selectedPassengerCount = 1;
    private int selectedAmountPaid = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bulakan_guiguinto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        discountedButton = findViewById(R.id.discounted);
        regularButton = findViewById(R.id.regular);
        displayChangeEditText = findViewById(R.id.displayChange);
        clearButton = findViewById(R.id.clear);
        backButton = findViewById(R.id.Back);

        // Init Amount Buttons
        btnAmount20 = findViewById(R.id.btnAmount20);
        btnAmount50 = findViewById(R.id.btnAmount50);
        btnAmount100 = findViewById(R.id.btnAmount100);

        // Init Passenger Buttons
        btnPass1 = findViewById(R.id.btnPass1);
        btnPass2 = findViewById(R.id.btnPass2);
        btnPass3 = findViewById(R.id.btnPass3);
        btnPass4 = findViewById(R.id.btnPass4);
        btnPass5 = findViewById(R.id.btnPass5);

        // Set Listeners for Amount
        btnAmount20.setOnClickListener(v -> updateAmountSelection(20));
        btnAmount50.setOnClickListener(v -> updateAmountSelection(50));
        btnAmount100.setOnClickListener(v -> updateAmountSelection(100));

        // Set Listeners for Passengers
        btnPass1.setOnClickListener(v -> updatePassengerSelection(1));
        btnPass2.setOnClickListener(v -> updatePassengerSelection(2));
        btnPass3.setOnClickListener(v -> updatePassengerSelection(3));
        btnPass4.setOnClickListener(v -> updatePassengerSelection(4));
        btnPass5.setOnClickListener(v -> updatePassengerSelection(5));
        
        // Initial Selection
        updateAmountSelection(20);
        updatePassengerSelection(1);

        clearButton.setOnClickListener(v -> {
            updateAmountSelection(20);
            updatePassengerSelection(1);
            displayChangeEditText.setText("");
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        discountedButton.setOnClickListener(v -> calculateFare(true));
        regularButton.setOnClickListener(v -> calculateFare(false));
    }

    private void updateAmountSelection(int amount) {
        selectedAmountPaid = amount;
        
        // Reset colors
        setButtonColor(btnAmount20, false);
        setButtonColor(btnAmount50, false);
        setButtonColor(btnAmount100, false);
        
        // Set selected color
        if (amount == 20) setButtonColor(btnAmount20, true);
        else if (amount == 50) setButtonColor(btnAmount50, true);
        else if (amount == 100) setButtonColor(btnAmount100, true);
    }

    private void updatePassengerSelection(int count) {
        selectedPassengerCount = count;
        
        // Reset colors
        setButtonColor(btnPass1, false);
        setButtonColor(btnPass2, false);
        setButtonColor(btnPass3, false);
        setButtonColor(btnPass4, false);
        setButtonColor(btnPass5, false);

        // Set selected color
        if (count == 1) setButtonColor(btnPass1, true);
        else if (count == 2) setButtonColor(btnPass2, true);
        else if (count == 3) setButtonColor(btnPass3, true);
        else if (count == 4) setButtonColor(btnPass4, true);
        else if (count == 5) setButtonColor(btnPass5, true);
    }
    
    private void setButtonColor(Button btn, boolean isSelected) {
        if (isSelected) {
            btn.setBackgroundTintList(ColorStateList.valueOf(SELECTED_BUTTON_COLOR));
        } else {
            btn.setBackgroundTintList(ColorStateList.valueOf(DEFAULT_BUTTON_COLOR));
        }
    }

    private void calculateFare(boolean isDiscounted) {
        calculatedChange = computeFare(selectedAmountPaid, selectedPassengerCount, isDiscounted);
        
        if (calculatedChange < 0) {
             displayChangeEditText.setText("Short");
             Toast.makeText(this, "Amount paid is insufficient", Toast.LENGTH_SHORT).show();
        } else {
             displayChangeEditText.setText(String.valueOf(calculatedChange));
        }
    }

    public int computeFare(int paidAmount, int numPassengers, boolean isDiscounted) {
        if (numPassengers <= 0) return 0;

        int totalFare = REGULAR_FARE_AMOUNT * numPassengers;

        if (isDiscounted) {
            totalFare = Math.max(0, totalFare - (DISCOUNTED_FARE_DIFFERENCE * numPassengers));
        }

        return paidAmount - totalFare;
    }
}
