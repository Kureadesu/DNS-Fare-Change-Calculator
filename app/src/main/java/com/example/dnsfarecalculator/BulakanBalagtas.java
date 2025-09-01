package com.example.dnsfarecalculator;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log; // Import Log
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BulakanBalagtas extends AppCompatActivity {

    private static final String TAG = "FareLogic"; // Tag for Logcat

    private static final int CATEGORY_PROMPT = 0;
    private static final int CATEGORY_WAWA = 1;
    private static final int CATEGORY_OTHER = 2;

    private static final int FARE_TIER1_REGULAR = 13;
    private static final int FARE_TIER2_REGULAR = 15;
    private static final int DISCOUNT_AMOUNT = 2;

    private Spinner pickupSpinner;
    private Spinner destinationSpinner;
    private EditText amountPaidEditText;
    private EditText numPassengersEditText;
    private Button calculateRegularButton;
    private Button calculateDiscountedButton;
    private TextView displayChangeTextView;

    private String[] locationNames;
    private int[] locationCategories;

    private int selectedPickupCategory = CATEGORY_PROMPT;
    private int selectedDestinationCategory = CATEGORY_PROMPT;
    private String selectedPickupName = "";
    private String selectedDestinationName = "";

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
        amountPaidEditText = findViewById(R.id.amount);
        numPassengersEditText = findViewById(R.id.pass);
        calculateRegularButton = findViewById(R.id.regular);
        calculateDiscountedButton = findViewById(R.id.discounted);
        displayChangeTextView = findViewById(R.id.displayChange);

        locationNames = getResources().getStringArray(R.array.location_names_bb);
        locationCategories = getResources().getIntArray(R.array.location_categories_bb);

        Log.d(TAG, "Location Names: " + java.util.Arrays.toString(locationNames));
        Log.d(TAG, "Location Categories: " + java.util.Arrays.toString(locationCategories));


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, locationNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickupSpinner.setAdapter(adapter);
        destinationSpinner.setAdapter(adapter);

        pickupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPickupCategory = locationCategories[position];
                selectedPickupName = locationNames[position];
                Log.d(TAG, "Pickup Selected: Name='" + selectedPickupName + "', Category=" + selectedPickupCategory + ", Position=" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPickupCategory = CATEGORY_PROMPT;
                selectedPickupName = "";
                Log.d(TAG, "Pickup: Nothing selected");
            }
        });

        destinationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDestinationCategory = locationCategories[position];
                selectedDestinationName = locationNames[position];
                Log.d(TAG, "Destination Selected: Name='" + selectedDestinationName + "', Category=" + selectedDestinationCategory + ", Position=" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedDestinationCategory = CATEGORY_PROMPT;
                selectedDestinationName = "";
                Log.d(TAG, "Destination: Nothing selected");
            }
        });

        calculateRegularButton.setOnClickListener(v -> calculateAndDisplaySimpleChange(false));
        calculateDiscountedButton.setOnClickListener(v -> calculateAndDisplaySimpleChange(true));
    }

    private void calculateAndDisplaySimpleChange(boolean isDiscounted) {
        Log.d(TAG, "---- calculateAndDisplaySimpleChange Called ----");
        Log.d(TAG, "isDiscounted: " + isDiscounted);
        Log.d(TAG, "Current Pickup: Name='" + selectedPickupName + "', Category=" + selectedPickupCategory);
        Log.d(TAG, "Current Destination: Name='" + selectedDestinationName + "', Category=" + selectedDestinationCategory);


        // 1. Validate Selections
        if (selectedPickupCategory == CATEGORY_PROMPT || selectedDestinationCategory == CATEGORY_PROMPT) {
            Toast.makeText(this, "Please select locations.", Toast.LENGTH_SHORT).show();
            displayChangeTextView.setText("0");
            Log.d(TAG, "Validation failed: Spinners not selected.");
            return;
        }

        // 2. Validate Inputs (Amount Paid & Passengers)
        String amountPaidStr = amountPaidEditText.getText().toString();
        String numPassengersStr = numPassengersEditText.getText().toString();

        if (TextUtils.isEmpty(amountPaidStr) || TextUtils.isEmpty(numPassengersStr)) {
            Toast.makeText(this, "Enter amount and passengers.", Toast.LENGTH_SHORT).show();
            displayChangeTextView.setText("0");
            Log.d(TAG, "Validation failed: Amount or passengers empty.");
            return;
        }

        int amountPaid;
        int numPassengers;
        try {
            amountPaid = Integer.parseInt(amountPaidStr);
            numPassengers = Integer.parseInt(numPassengersStr);
            if (numPassengers <= 0) {
                Toast.makeText(this, "Passengers must be > 0.", Toast.LENGTH_SHORT).show();
                displayChangeTextView.setText("0");
                Log.d(TAG, "Validation failed: Passengers <= 0.");
                return;
            }
            Log.d(TAG, "Inputs: AmountPaid=" + amountPaid + ", NumPassengers=" + numPassengers);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format.", Toast.LENGTH_SHORT).show();
            displayChangeTextView.setText("0");
            Log.d(TAG, "Validation failed: NumberFormatException.");
            return;
        }

        // 3. Determine Base Fare Per Passenger
        int baseRegularFarePerPassenger = -1; // Initialize to an invalid state

        Log.d(TAG, "Evaluating fare rules...");
        Log.d(TAG, "Comparing selectedPickupName ('" + selectedPickupName + "') with selectedDestinationName ('" + selectedDestinationName + "')");

        if (selectedPickupName.equals(selectedDestinationName)) {
            Log.d(TAG, "Rule Matched: Same location.");
            baseRegularFarePerPassenger = 0;
        }
        // Rule: "bagumbayan / san jose UP TO panginay balagtas = 13 regular" (OTHER to OTHER)
        else if (selectedPickupCategory == CATEGORY_OTHER && selectedDestinationCategory == CATEGORY_OTHER) {
            Log.d(TAG, "Rule Matched: OTHER to OTHER.");
            baseRegularFarePerPassenger = FARE_TIER1_REGULAR;
        }
        // Rule: FROM BULAKAN - BALAGTAS: "wawa = 15 regular" (OTHER to WAWA)
        else if (selectedPickupCategory == CATEGORY_OTHER && selectedDestinationCategory == CATEGORY_WAWA) {
            Log.d(TAG, "Rule Matched: OTHER to WAWA.");
            baseRegularFarePerPassenger = FARE_TIER2_REGULAR;
        }
        // Rule: FROM BALAGTAS - BULAKAN (Pickup is WAWA)
        else if (selectedPickupCategory == CATEGORY_WAWA && selectedDestinationCategory == CATEGORY_OTHER) {
            Log.d(TAG, "Rule Matched: WAWA to OTHER. Checking sub-rules...");
            // Sub-rule: "bagumbayan / san jose = 15 regular" (when destination is Bagumbayan/SJ FROM WAWA)
            Log.d(TAG, "Comparing selectedDestinationName ('" + selectedDestinationName + "') with 'Bagumbayan / San Jose'");
            if (selectedDestinationName.equals("Bagumbayan / San Jose")) {
                Log.d(TAG, "Sub-Rule Matched: WAWA to Bagumbayan / San Jose.");
                baseRegularFarePerPassenger = FARE_TIER2_REGULAR;
            }
            // Sub-rule: "wawa UP TO matungao = 13 regular" (WAWA to OTHER that is NOT Bagumbayan/San Jose)
            else {
                Log.d(TAG, "Sub-Rule Matched: WAWA to Other (not Bagumbayan / San Jose).");
                baseRegularFarePerPassenger = FARE_TIER1_REGULAR;
            }
        } else {
            Log.d(TAG, "No specific fare rule matched the top-level conditions.");
            // This else means none of the primary category combinations were met.
            // This should ideally not be reached if categories cover all locations.
        }

        Log.d(TAG, "BaseRegularFarePerPassenger after rules: " + baseRegularFarePerPassenger);

        // Check if a fare rule was matched
        if (baseRegularFarePerPassenger == -1) {
            Toast.makeText(this, "Fare rule not found for this specific combination.", Toast.LENGTH_LONG).show();
            displayChangeTextView.setText("0"); // Or "Error"
            Log.d(TAG, "Error: baseRegularFarePerPassenger is still -1. No rule was definitively matched.");
            return;
        }

        // 4. Apply Discount if applicable
        int actualFarePerPassenger = baseRegularFarePerPassenger;
        if (isDiscounted && baseRegularFarePerPassenger > 0) {
            actualFarePerPassenger -= DISCOUNT_AMOUNT;
            if (actualFarePerPassenger < 0) {
                actualFarePerPassenger = 0;
            }
        }
        Log.d(TAG, "ActualFarePerPassenger (after discount if any): " + actualFarePerPassenger);


        // 5. Calculate Total Fare
        int totalFare = actualFarePerPassenger * numPassengers;
        Log.d(TAG, "TotalFare: " + totalFare);

        // 6. Calculate Change
        int change = amountPaid - totalFare;
        Log.d(TAG, "Change: " + change);

        // 7. Display Change
        if (change < 0) {
            Toast.makeText(this, "Amount paid is less than total fare (₱" + totalFare + ")", Toast.LENGTH_LONG).show();
            displayChangeTextView.setText("Short");
            Log.d(TAG, "Displaying: Short");
        } else {
            displayChangeTextView.setText(String.valueOf(change));
            Log.d(TAG, "Displaying Change: " + change);
        }
        Log.d(TAG, "---- calculateAndDisplaySimpleChange Finished ----");
    }
}
