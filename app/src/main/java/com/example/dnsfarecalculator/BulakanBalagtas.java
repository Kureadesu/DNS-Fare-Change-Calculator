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

public class BulakanBalagtas extends AppCompatActivity {

    private static final String TAG = "FareLogic";

    private static final int CATEGORY_PROMPT = 0;
    
    private static final int FARE_TIER1_REGULAR = 13;
    private static final int FARE_TIER2_REGULAR = 15;
    private static final int DISCOUNT_AMOUNT = 2;
    
    private final int DEFAULT_BUTTON_COLOR = 0xFF814622;
    private final int SELECTED_BUTTON_COLOR = 0xFF218C17;

    private Spinner pickupSpinner;
    private Spinner destinationSpinner;
    private Button calculateRegularButton;
    private Button calculateDiscountedButton;
    private Button backBtn;
    private Button clearBtn;
    private TextView displayChangeTextView;
    
    // Buttons for Amount Paid
    private Button btnAmount20, btnAmount50, btnAmount100;
    // Buttons for Passengers
    private Button btnPass1, btnPass2, btnPass3, btnPass4, btnPass5;

    private String[] locationNames;
    private int[] locationCategories;

    private int selectedPickupCategory = CATEGORY_PROMPT;
    private int selectedDestinationCategory = CATEGORY_PROMPT;
    private String selectedPickupName = "";
    private String selectedDestinationName = "";
    private int selectedPassengerCount = 1;
    private int selectedAmountPaid = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bulakan_balagtas);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pickupSpinner = findViewById(R.id.pickup);
        destinationSpinner = findViewById(R.id.destination);
        calculateRegularButton = findViewById(R.id.regular);
        calculateDiscountedButton = findViewById(R.id.discounted);
        displayChangeTextView = findViewById(R.id.displayChange);
        backBtn = findViewById(R.id.Back);
        clearBtn = findViewById(R.id.clear);
        
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

        backBtn.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        
        clearBtn.setOnClickListener(v -> {
            updateAmountSelection(20);
            updatePassengerSelection(1);
            displayChangeTextView.setText("0");
            pickupSpinner.setSelection(0);
            destinationSpinner.setSelection(0);
        });

        locationNames = getResources().getStringArray(R.array.location_names_bb);
        locationCategories = getResources().getIntArray(R.array.location_categories_bb);

        // Locations Adapter
        ArrayAdapter<String> locAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, locationNames);
        locAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickupSpinner.setAdapter(locAdapter);
        destinationSpinner.setAdapter(locAdapter);


        pickupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPickupCategory = locationCategories[position];
                selectedPickupName = locationNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPickupCategory = CATEGORY_PROMPT;
                selectedPickupName = "";
            }
        });

        destinationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDestinationCategory = locationCategories[position];
                selectedDestinationName = locationNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedDestinationCategory = CATEGORY_PROMPT;
                selectedDestinationName = "";
            }
        });

        calculateRegularButton.setOnClickListener(v -> calculateAndDisplaySimpleChange(false));
        calculateDiscountedButton.setOnClickListener(v -> calculateAndDisplaySimpleChange(true));
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

    private void calculateAndDisplaySimpleChange(boolean isDiscounted) {
        // 1. Validate Selections
        if (selectedPickupCategory == CATEGORY_PROMPT || selectedDestinationCategory == CATEGORY_PROMPT) {
            Toast.makeText(this, "Please select locations.", Toast.LENGTH_SHORT).show();
            displayChangeTextView.setText("0");
            return;
        }

        // 3. Determine Base Fare Per Passenger
        int baseRegularFarePerPassenger = FARE_TIER1_REGULAR; // Default 13

        String locationBagumbayanSanJose = "Bagumbayan | San Jose";
        String locationWawa = "Wawa";

        boolean isPickupBSJ = selectedPickupName.equals(locationBagumbayanSanJose);
        boolean isDestWawa = selectedDestinationName.equals(locationWawa);
        boolean isPickupWawa = selectedPickupName.equals(locationWawa);
        boolean isDestBSJ = selectedDestinationName.equals(locationBagumbayanSanJose);

        // Rule 1: Bagumbayan/San Jose <-> Wawa
        if ((isPickupBSJ && isDestWawa) || (isPickupWawa && isDestBSJ)) {
            baseRegularFarePerPassenger = FARE_TIER2_REGULAR; // 15
        }

        // Same location check (Fare 0)
        if (selectedPickupName.equals(selectedDestinationName)) {
            baseRegularFarePerPassenger = 0;
        }

        // 4. Apply Discount if applicable
        int actualFarePerPassenger = baseRegularFarePerPassenger;
        if (isDiscounted && baseRegularFarePerPassenger > 0) {
            actualFarePerPassenger -= DISCOUNT_AMOUNT;
            if (actualFarePerPassenger < 0) {
                actualFarePerPassenger = 0;
            }
        }

        // 5. Calculate Total Fare using selectedPassengerCount
        int totalFare = actualFarePerPassenger * selectedPassengerCount;

        // 6. Calculate Change
        int change = selectedAmountPaid - totalFare;

        // 7. Display Change
        if (change < 0) {
            Toast.makeText(this, "Amount paid is less than total fare (₱" + totalFare + ")", Toast.LENGTH_LONG).show();
            displayChangeTextView.setText("Short");
        } else {
            displayChangeTextView.setText(String.valueOf(change));
        }
    }
}
