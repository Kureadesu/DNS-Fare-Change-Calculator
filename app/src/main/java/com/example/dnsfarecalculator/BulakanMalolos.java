package com.example.dnsfarecalculator;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BulakanMalolos extends AppCompatActivity {

    private static final String TAG = "FareLogic_BM";

    private static final int CATEGORY_PROMPT = 0;
    private static final int BASE_FARE = 13;
    private static final int FARE_INCREMENT = 2;
    private static final int BASE_DISTANCE_STEPS = 3;
    private static final int DISCOUNT_AMOUNT = 2;
    
    private final int DEFAULT_BUTTON_COLOR = 0xFF814622;
    private final int SELECTED_BUTTON_COLOR = 0xFF218C17;

    // UI Elements
    private Spinner pickupSpinnerBM;
    private Spinner destinationSpinnerBM;
    private Button calculateRegularButtonBM;
    private Button calculateDiscountedButtonBM;
    private Button goBack;
    private Button clear;
    private TextView displayChangeTextViewBM;

    // Buttons for Amount Paid
    private Button btnAmount20, btnAmount50, btnAmount100;
    // Buttons for Passengers
    private Button btnPass1, btnPass2, btnPass3, btnPass4, btnPass5;

    // Data arrays
    private String[] allLocationDisplayNames;
    private int[] allLocationCategories;
    
    private int selectedPickupCategoryBM = CATEGORY_PROMPT;
    private String selectedPickupNameBM = "";
    private int selectedDestinationCategoryBM = CATEGORY_PROMPT;
    private String selectedDestinationNameBM = "";
    private int selectedPassengerCount = 1;
    private int selectedAmountPaid = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bulakan_malolos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        goBack = findViewById(R.id.Back);
        goBack.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));

        try {
            allLocationDisplayNames = getResources().getStringArray(R.array.all_location_display_names);
            allLocationCategories = getResources().getIntArray(R.array.all_location_categories);
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error loading string/integer arrays: " + e.getMessage());
            Toast.makeText(this, "Error loading location data!", Toast.LENGTH_LONG).show();
            finish(); return;
        }

        // Initialize UI Elements
        pickupSpinnerBM = findViewById(R.id.pickup);
        destinationSpinnerBM = findViewById(R.id.destination);
        calculateRegularButtonBM = findViewById(R.id.regular);
        calculateDiscountedButtonBM = findViewById(R.id.discounted);
        displayChangeTextViewBM = findViewById(R.id.displayChange);
        clear = findViewById(R.id.clear);
        
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

        clear.setOnClickListener(v -> {
            updateAmountSelection(20);
            updatePassengerSelection(1);
            displayChangeTextViewBM.setText("0");
            pickupSpinnerBM.setSelection(0);
            destinationSpinnerBM.setSelection(0);
        });

        // Initialize Spinner Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, allLocationDisplayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickupSpinnerBM.setAdapter(adapter);
        destinationSpinnerBM.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean isPickup = parent.getId() == R.id.pickup;
                if (position >= 0 && position < allLocationCategories.length) {
                    if (isPickup) {
                        selectedPickupCategoryBM = allLocationCategories[position];
                        selectedPickupNameBM = allLocationDisplayNames[position];
                    } else {
                        selectedDestinationCategoryBM = allLocationCategories[position];
                        selectedDestinationNameBM = allLocationDisplayNames[position];
                    }
                } else {
                    if (isPickup) selectedPickupCategoryBM = CATEGORY_PROMPT;
                    else selectedDestinationCategoryBM = CATEGORY_PROMPT;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (parent.getId() == R.id.pickup) selectedPickupCategoryBM = CATEGORY_PROMPT;
                else selectedDestinationCategoryBM = CATEGORY_PROMPT;
            }
        };

        pickupSpinnerBM.setOnItemSelectedListener(itemSelectedListener);
        destinationSpinnerBM.setOnItemSelectedListener(itemSelectedListener);

        calculateRegularButtonBM.setOnClickListener(v -> calculateAndDisplayChange(false));
        calculateDiscountedButtonBM.setOnClickListener(v -> calculateAndDisplayChange(true));
    }
    
    private void updateAmountSelection(int amount) {
        selectedAmountPaid = amount;
        setButtonColor(btnAmount20, amount == 20);
        setButtonColor(btnAmount50, amount == 50);
        setButtonColor(btnAmount100, amount == 100);
    }

    private void updatePassengerSelection(int count) {
        selectedPassengerCount = count;
        setButtonColor(btnPass1, count == 1);
        setButtonColor(btnPass2, count == 2);
        setButtonColor(btnPass3, count == 3);
        setButtonColor(btnPass4, count == 4);
        setButtonColor(btnPass5, count == 5);
    }
    
    private void setButtonColor(Button btn, boolean isSelected) {
        if (isSelected) {
            btn.setBackgroundTintList(ColorStateList.valueOf(SELECTED_BUTTON_COLOR));
        } else {
            btn.setBackgroundTintList(ColorStateList.valueOf(DEFAULT_BUTTON_COLOR));
        }
    }

    private void calculateAndDisplayChange(boolean isDiscounted) {
        Log.d(TAG, "---- calculateAndDisplayChange (BM) Called ----");
        
        // 1. Validate Location Selections
        if (selectedPickupCategoryBM == CATEGORY_PROMPT || selectedDestinationCategoryBM == CATEGORY_PROMPT) {
            Toast.makeText(this, "Please select locations.", Toast.LENGTH_SHORT).show();
            displayChangeTextViewBM.setText("0");
            return;
        }

        // 3. Calculate Base Fare Logic
        int farePerPassenger = 0;

        if (selectedPickupCategoryBM == selectedDestinationCategoryBM) {
             farePerPassenger = 0;
        } else {
            // Calculate distance in steps (based on category ID difference)
            // Categories 10-19 are linear.
            int steps = Math.abs(selectedPickupCategoryBM - selectedDestinationCategoryBM);
            
            // Base fare for first 3 steps (or less) is 13.
            farePerPassenger = BASE_FARE; // 13
            
            if (steps > BASE_DISTANCE_STEPS) { // > 3
                int extraSteps = steps - BASE_DISTANCE_STEPS;
                farePerPassenger += extraSteps * FARE_INCREMENT; // +2 per extra step
            }
        }

        Log.d(TAG, "Steps: " + Math.abs(selectedPickupCategoryBM - selectedDestinationCategoryBM) + ", Base Regular Fare: " + farePerPassenger);

        // 4. Apply Discount
        if (isDiscounted && farePerPassenger > 0) {
            farePerPassenger -= DISCOUNT_AMOUNT;
            if (farePerPassenger < 0) farePerPassenger = 0;
        }

        // 5. Calculate Total and Change
        int totalFare = farePerPassenger * selectedPassengerCount;
        int change = selectedAmountPaid - totalFare;

        if (change < 0) {
            Toast.makeText(this, "Amount paid is less than total fare (₱" + totalFare + ")", Toast.LENGTH_LONG).show();
            displayChangeTextViewBM.setText("Short");
        } else {
            displayChangeTextViewBM.setText(String.valueOf(change));
        }
    }
}
