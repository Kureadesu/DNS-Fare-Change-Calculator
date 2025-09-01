package com.example.dnsfarecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.Arrays;

public class BulakanMalolos extends AppCompatActivity {

    private static final String TAG = "FareLogic_BM";

    // UPDATED Categories to match your latest strings.xml
    private static final int CATEGORY_PROMPT = 0;
    private static final int CATEGORY_BULAKAN_SAN_JOSE = 10;
    // Values below are shifted based on your latest strings.xml
    private static final int CATEGORY_MALOLOS_MAYSANTOL = 11;  // Was 19
    private static final int CATEGORY_MALOLOS_SANNICOLAS = 12; // Was 18
    private static final int CATEGORY_MALOLOS_PITPITAN = 13;    // Was 11
    private static final int CATEGORY_MALOLOS_MAMBOG = 14;     // Was 12
    private static final int CATEGORY_MALOLOS_MATIMBO = 15;    // Was 13
    private static final int CATEGORY_MALOLOS_PANASAHAN = 16;  // Was 14
    private static final int CATEGORY_MALOLOS_BAGNA = 17;      // Was 15
    private static final int CATEGORY_MALOLOS_ATLAG = 18;      // Was 16
    private static final int CATEGORY_MALOLOS_SANJUAN_STOROSARIO = 19; // Was 17


    private static final int DISCOUNT_AMOUNT = 2;

    // UI Elements
    private Spinner pickupSpinnerBM;
    private Spinner destinationSpinnerBM;
    private EditText amountPaidEditTextBM;
    private EditText numPassengersEditTextBM;
    private Button calculateRegularButtonBM;
    private Button calculateDiscountedButtonBM;
    private Button goBack;
    private TextView displayChangeTextViewBM;

    // Data arrays
    private String[] allLocationDisplayNames;
    private int[] allLocationCategories;

    // Selected item details
    private int selectedPickupCategoryBM = CATEGORY_PROMPT;
    private String selectedPickupNameBM = "";
    private int selectedDestinationCategoryBM = CATEGORY_PROMPT;
    private String selectedDestinationNameBM = "";

    // Fare constants - names remain the same, their usage in logic will map to new categories
    private int FARE_BM_B_MAYSANTOL; // Will be used when selectedDestinationCategoryBM is CATEGORY_MALOLOS_MAYSANTOL (now 11)
    private int FARE_BM_B_SANNICOLAS; // Will be used when selectedDestinationCategoryBM is CATEGORY_MALOLOS_SANNICOLAS (now 12)
    private int FARE_BM_B_PITPITAN;
    private int FARE_BM_B_MAMBOG;
    private int FARE_BM_B_MATIMBO;
    private int FARE_BM_B_PANASAHAN;
    private int FARE_BM_B_BAGNA;
    private int FARE_BM_B_ATLAG;
    private int FARE_BM_B_SANJUAN; // This corresponds to San Juan/Sto. Rosario

    // Malolos to Bulakan fares
    private int FARE_MB_SJ_SANJUAN; // Fare when pickup is SanJuan, dest is SanJuan (part of "UP TO" logic)
    private int FARE_MB_SJ_ATLAG;   // Fare when pickup is SanJuan, dest is Atlag (part of "UP TO" logic)
    private int FARE_MB_SJ_BAGNA;   // Fare when pickup is SanJuan, dest is Bagna (part of "UP TO" logic)
    private int FARE_MB_SJ_PANASAHAN; // This should be the general "UP TO Panasahan" fare from San Juan
    private int FARE_MB_MATIMBO;
    private int FARE_MB_MAMBOG;
    private int FARE_MB_PITPITAN;
    private int FARE_MB_SANNICOLAS; // Fare when pickup is SanJuan, dest is SanNicolas
    private int FARE_MB_MAYSANTOL;  // Fare when pickup is SanJuan, dest is Maysantol
    private int FARE_MB_SANJOSE;    // Fare when pickup is SanJuan, dest is SanJose (Bulakan)


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
        Intent intent = new Intent(this, MainActivity.class);
        goBack.setOnClickListener(v -> startActivity(intent));
        Log.d(TAG, "onCreate: Starting BulakanMalolos");

        try {
            allLocationDisplayNames = getResources().getStringArray(R.array.all_location_display_names);
            allLocationCategories = getResources().getIntArray(R.array.all_location_categories);

            Log.d(TAG, "onCreate: Loaded allLocationDisplayNames: " + Arrays.toString(allLocationDisplayNames));
            Log.d(TAG, "onCreate: Loaded allLocationCategories: " + Arrays.toString(allLocationCategories));
            if (allLocationDisplayNames.length != allLocationCategories.length) {
                Log.e(TAG, "onCreate: WARNING! Display names and categories array lengths differ!");
                Toast.makeText(this, "Data Error: Array length mismatch!", Toast.LENGTH_LONG).show();
                finish(); // Critical error, stop activity
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error loading string/integer arrays: " + e.getMessage());
            Toast.makeText(this, "Error loading location data!", Toast.LENGTH_LONG).show();
            finish(); // Critical error, stop activity
            return;
        }

        loadFareConstants();

        pickupSpinnerBM = findViewById(R.id.pickup);
        destinationSpinnerBM = findViewById(R.id.destination);
        amountPaidEditTextBM = findViewById(R.id.amount);
        numPassengersEditTextBM = findViewById(R.id.pass);
        calculateRegularButtonBM = findViewById(R.id.regular);
        calculateDiscountedButtonBM = findViewById(R.id.discounted);
        displayChangeTextViewBM = findViewById(R.id.displayChange);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, allLocationDisplayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickupSpinnerBM.setAdapter(adapter);
        destinationSpinnerBM.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Determine if it's pickup or destination spinner
                boolean isPickup = parent.getId() == R.id.pickup;

                if (position >= 0 && position < allLocationCategories.length) { // Ensure position is valid for categories
                    if (isPickup) {
                        selectedPickupCategoryBM = allLocationCategories[position];
                        selectedPickupNameBM = allLocationDisplayNames[position];
                        Log.d(TAG, "PICKUP Set: Name='" + selectedPickupNameBM + "', Category=" + selectedPickupCategoryBM + " at pos " + position);
                    } else {
                        selectedDestinationCategoryBM = allLocationCategories[position];
                        selectedDestinationNameBM = allLocationDisplayNames[position];
                        Log.d(TAG, "DESTINATION Set: Name='" + selectedDestinationNameBM + "', Category=" + selectedDestinationCategoryBM + " at pos " + position);
                    }
                } else {
                    String spinnerName = isPickup ? "PICKUP" : "DESTINATION";
                    Log.e(TAG, spinnerName + " Error: Position " + position + " out of bounds for categories array (len=" + allLocationCategories.length + ")");
                    if (isPickup) {
                        selectedPickupCategoryBM = CATEGORY_PROMPT;
                        selectedPickupNameBM = (position >= 0 && position < allLocationDisplayNames.length) ? allLocationDisplayNames[position] : "--error--";
                    } else {
                        selectedDestinationCategoryBM = CATEGORY_PROMPT;
                        selectedDestinationNameBM = (position >= 0 && position < allLocationDisplayNames.length) ? allLocationDisplayNames[position] : "--error--";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (parent.getId() == R.id.pickup) {
                    selectedPickupCategoryBM = CATEGORY_PROMPT;
                    selectedPickupNameBM = "";
                } else {
                    selectedDestinationCategoryBM = CATEGORY_PROMPT;
                    selectedDestinationNameBM = "";
                }
            }
        };

        pickupSpinnerBM.setOnItemSelectedListener(itemSelectedListener);
        destinationSpinnerBM.setOnItemSelectedListener(itemSelectedListener);

        calculateRegularButtonBM.setOnClickListener(v -> calculateAndDisplayChange(false));
        calculateDiscountedButtonBM.setOnClickListener(v -> calculateAndDisplayChange(true));
    }

    private void loadFareConstants() {
        // Bulakan to Malolos fares - XML names match these constants
        FARE_BM_B_MAYSANTOL = getResources().getInteger(R.integer.fare_bm_b_maysantol);
        FARE_BM_B_SANNICOLAS = getResources().getInteger(R.integer.fare_bm_b_sannicolas);
        FARE_BM_B_PITPITAN = getResources().getInteger(R.integer.fare_bm_b_pitpitan);
        FARE_BM_B_MAMBOG = getResources().getInteger(R.integer.fare_bm_b_mambog);
        FARE_BM_B_MATIMBO = getResources().getInteger(R.integer.fare_bm_b_matimbo);
        FARE_BM_B_PANASAHAN = getResources().getInteger(R.integer.fare_bm_b_panasahan);
        FARE_BM_B_BAGNA = getResources().getInteger(R.integer.fare_bm_b_bagna);
        FARE_BM_B_ATLAG = getResources().getInteger(R.integer.fare_bm_b_atlag);
        FARE_BM_B_SANJUAN = getResources().getInteger(R.integer.fare_bm_b_sanjuan); // For San Juan/Sto. Rosario

        // Malolos to Bulakan fares (mostly from San Juan/Sto. Rosario as pickup)
        FARE_MB_SJ_SANJUAN = getResources().getInteger(R.integer.fare_mb_sj_sanjuan);
        FARE_MB_SJ_ATLAG = getResources().getInteger(R.integer.fare_mb_sj_atlag);
        FARE_MB_SJ_BAGNA = getResources().getInteger(R.integer.fare_mb_sj_bagna);
        FARE_MB_SJ_PANASAHAN = getResources().getInteger(R.integer.fare_mb_sj_panasahan); // General "UP TO" fare for first few stops from SJ
        FARE_MB_MATIMBO = getResources().getInteger(R.integer.fare_mb_matimbo);
        FARE_MB_MAMBOG = getResources().getInteger(R.integer.fare_mb_mambog);
        FARE_MB_PITPITAN = getResources().getInteger(R.integer.fare_mb_pitpitan);       // Specific fare for Pitpitan from SJ
        FARE_MB_SANNICOLAS = getResources().getInteger(R.integer.fare_mb_sannicolas); // Specific fare for SanNicolas from SJ
        FARE_MB_MAYSANTOL = getResources().getInteger(R.integer.fare_mb_maysantol);   // Specific fare for Maysantol from SJ
        FARE_MB_SANJOSE = getResources().getInteger(R.integer.fare_mb_sanjose);       // Specific fare for SanJose (Bulakan) from SJ
    }


    private void calculateAndDisplayChange(boolean isDiscounted) {
        Log.d(TAG, "---- calculateAndDisplayChange (BM) Called ----");
        Log.d(TAG, "Pickup Cat: " + selectedPickupCategoryBM + " (" + selectedPickupNameBM + ")");
        Log.d(TAG, "Dest Cat: " + selectedDestinationCategoryBM + " (" + selectedDestinationNameBM + ")");

        // --- Start of validation ---
        if (selectedPickupCategoryBM == CATEGORY_PROMPT || selectedDestinationCategoryBM == CATEGORY_PROMPT) {
            Toast.makeText(this, "Please select locations.", Toast.LENGTH_SHORT).show();
            displayChangeTextViewBM.setText("0"); return;
        }
        String amountPaidStr = amountPaidEditTextBM.getText().toString();
        String numPassengersStr = numPassengersEditTextBM.getText().toString();
        if (TextUtils.isEmpty(amountPaidStr) || TextUtils.isEmpty(numPassengersStr)) {
            Toast.makeText(this, "Enter amount and passengers.", Toast.LENGTH_SHORT).show();
            displayChangeTextViewBM.setText("0"); return;
        }
        int amountPaid;
        int numPassengers;
        try {
            amountPaid = Integer.parseInt(amountPaidStr);
            numPassengers = Integer.parseInt(numPassengersStr);
            if (numPassengers <= 0) {
                Toast.makeText(this, "Passengers must be > 0.", Toast.LENGTH_SHORT).show();
                displayChangeTextViewBM.setText("0"); return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format.", Toast.LENGTH_SHORT).show();
            displayChangeTextViewBM.setText("0"); return;
        }

        int baseRegularFarePerPassenger = -1; // Initialize to -1

        // --- Bulakan (San Jose) to Malolos Route ---
        if (selectedPickupCategoryBM == CATEGORY_BULAKAN_SAN_JOSE) { // Cat 10
            Log.d(TAG, "Route: Bulakan (San Jose) to Malolos");
            // If selectedDestinationCategoryBM is also CATEGORY_BULAKAN_SAN_JOSE (10),
            // it will not match any of the explicit Malolos destination cases below.
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_MAYSANTOL: // Now 11
                    baseRegularFarePerPassenger = FARE_BM_B_MAYSANTOL;
                    break;
                case CATEGORY_MALOLOS_SANNICOLAS: // Now 12
                    baseRegularFarePerPassenger = FARE_BM_B_SANNICOLAS;
                    break;
                case CATEGORY_MALOLOS_PITPITAN:   // Now 13
                    baseRegularFarePerPassenger = FARE_BM_B_PITPITAN;
                    break;
                case CATEGORY_MALOLOS_MAMBOG:     // Now 14
                    baseRegularFarePerPassenger = FARE_BM_B_MAMBOG;
                    break;
                case CATEGORY_MALOLOS_MATIMBO:    // Now 15
                    baseRegularFarePerPassenger = FARE_BM_B_MATIMBO;
                    break;
                case CATEGORY_MALOLOS_PANASAHAN:  // Now 16
                    baseRegularFarePerPassenger = FARE_BM_B_PANASAHAN;
                    break;
                case CATEGORY_MALOLOS_BAGNA:      // Now 17
                    baseRegularFarePerPassenger = FARE_BM_B_BAGNA;
                    break;
                case CATEGORY_MALOLOS_ATLAG:      // Now 18
                    baseRegularFarePerPassenger = FARE_BM_B_ATLAG;
                    break;
                case CATEGORY_MALOLOS_SANJUAN_STOROSARIO: // Now 19
                    baseRegularFarePerPassenger = FARE_BM_B_SANJUAN;
                    break;
                default:
                    // This 'default' will be hit if the destination is San Jose (Cat 10)
                    // OR any other category not explicitly listed for this pickup.
                    Log.d(TAG, "B(SJ)->M: Dest Cat " + selectedDestinationCategoryBM + " (" + selectedDestinationNameBM + ") not in defined matrix for this pickup.");
                    // baseRegularFarePerPassenger remains -1
                    break;
            }
            if (baseRegularFarePerPassenger != -1) Log.d(TAG, "B(SJ)->M: Fare determined: " + baseRegularFarePerPassenger);
        }
        // --- Malolos to Bulakan Route (Pickup from San Juan/Sto. Rosario) ---
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_SANJUAN_STOROSARIO) { // Cat 19
            Log.d(TAG, "Route: Malolos (San Juan/Sto. Rosario) to Bulakan");
            // If selectedDestinationCategoryBM is also CATEGORY_MALOLOS_SANJUAN_STOROSARIO (19),
            // the first 'if' might handle it if your FARE_MB_SJ_PANASAHAN applies,
            // otherwise it might fall to the final 'else' or a specific case.
            // This block needs careful review if "San Juan to San Juan" should be an error.

            // "UP TO" logic from San Juan
            if (selectedDestinationCategoryBM == CATEGORY_MALOLOS_SANJUAN_STOROSARIO || // 19
                    selectedDestinationCategoryBM == CATEGORY_MALOLOS_ATLAG ||            // 18
                    selectedDestinationCategoryBM == CATEGORY_MALOLOS_BAGNA ||            // 17
                    selectedDestinationCategoryBM == CATEGORY_MALOLOS_PANASAHAN) {        // 16
                baseRegularFarePerPassenger = FARE_MB_SJ_PANASAHAN; // Using one of the 13-peso fare constants
            }
            // Other specific destinations from San Juan/Sto. Rosario
            else if (selectedDestinationCategoryBM == CATEGORY_MALOLOS_MATIMBO) { // 15
                baseRegularFarePerPassenger = FARE_MB_MATIMBO;
            } else if (selectedDestinationCategoryBM == CATEGORY_MALOLOS_MAMBOG) { // 14
                baseRegularFarePerPassenger = FARE_MB_MAMBOG;
            } else if (selectedDestinationCategoryBM == CATEGORY_MALOLOS_PITPITAN) { // 13
                baseRegularFarePerPassenger = FARE_MB_PITPITAN;
            } else if (selectedDestinationCategoryBM == CATEGORY_MALOLOS_SANNICOLAS) { // 12
                baseRegularFarePerPassenger = FARE_MB_SANNICOLAS;
            } else if (selectedDestinationCategoryBM == CATEGORY_MALOLOS_MAYSANTOL) { // 11
                baseRegularFarePerPassenger = FARE_MB_MAYSANTOL;
            } else if (selectedDestinationCategoryBM == CATEGORY_BULAKAN_SAN_JOSE) { // 10
                baseRegularFarePerPassenger = FARE_MB_SANJOSE;
            } else {
                // This 'else' is hit if destination is not covered by above from San Juan pickup
                Log.d(TAG, "M(SJ/SR)->B: Dest Cat " + selectedDestinationCategoryBM + " (" + selectedDestinationNameBM + ") not in matrix for this pickup.");
                // baseRegularFarePerPassenger remains -1
            }
            if (baseRegularFarePerPassenger != -1) Log.d(TAG, "M(SJ/SR)->B: Fare determined: " + baseRegularFarePerPassenger);
        }
        // --- ADD FARE LOGIC IF PICKUP IS FROM OTHER MALOLOS LOCATIONS ---
        // ... (Maysantol, San Nicolas, etc. as pickups) ...
        // These will also result in -1 if the destination isn't explicitly handled
        // for that specific pickup (including if dest is same as pickup).

        // --- NO EXPLICIT "SAME LOCATION" HANDLER ---
        // If none of the above route-specific conditions set baseRegularFarePerPassenger,
        // it will remain -1. This includes cases where pickup and destination are the same
        // and that specific "route" (e.g., San Jose to San Jose) is not defined.

        Log.d(TAG, "BaseRegularFarePerPassenger after rules: " + baseRegularFarePerPassenger);

        if (baseRegularFarePerPassenger == -1) {
            Toast.makeText(this, "Fare rule not found for this route.", Toast.LENGTH_LONG).show();
            displayChangeTextViewBM.setText("0"); // Or you could set a specific error message like "Invalid route"
            return;
        }
        // --- ADD FARE LOGIC IF PICKUP IS FROM OTHER MALOLOS LOCATIONS ---
        // Example: if Pickup is Maysantol (Cat 11)
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_MAYSANTOL) { // Cat 11
            Log.d(TAG, "Route: Malolos (Maysantol) to Bulakan - NO FARE RULES DEFINED YET");
            // Add specific logic: if (selectedDestinationCategoryBM == CATEGORY_BULAKAN_SAN_JOSE) baseRegularFarePerPassenger = ... ;
        }
        // Example: if Pickup is San Nicolas (Cat 12)
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_SANNICOLAS) { // Cat 12
            Log.d(TAG, "Route: Malolos (San Nicolas) to Bulakan - NO FARE RULES DEFINED YET");
            // Add specific logic: if (selectedDestinationCategoryBM == CATEGORY_BULAKAN_SAN_JOSE) baseRegularFarePerPassenger = ... ;
        }
        // ... add for Pitpitan, Mambog, Matimbo, Panasahan, Bagna, Atlag if they can be pickups with defined fares to Bulakan

        // --- Same location ---
        else if (selectedPickupNameBM.equals(selectedDestinationNameBM)) {
            Log.d(TAG, "Rule: Same location selected.");
            baseRegularFarePerPassenger = 0;
        }

        Log.d(TAG, "BaseRegularFarePerPassenger after rules: " + baseRegularFarePerPassenger);

        if (baseRegularFarePerPassenger == -1) {
            Toast.makeText(this, "Fare rule not found for this route.", Toast.LENGTH_LONG).show();
            displayChangeTextViewBM.setText("0");
            return;
        }

        int actualFarePerPassenger = baseRegularFarePerPassenger;
        if (isDiscounted && baseRegularFarePerPassenger > 0) {
            actualFarePerPassenger -= DISCOUNT_AMOUNT;
            if (actualFarePerPassenger < 0) actualFarePerPassenger = 0; // Fare cannot be negative
        }
        Log.d(TAG, "ActualFarePerPassenger (after discount): " + actualFarePerPassenger);

        int totalFare = actualFarePerPassenger * numPassengers;
        int change = amountPaid - totalFare;
        Log.d(TAG, "TotalFare: " + totalFare + ", Change: " + change);

        if (change < 0) {
            Toast.makeText(this, "Amount paid is less than total fare (₱" + totalFare + ")", Toast.LENGTH_LONG).show();
            displayChangeTextViewBM.setText("Short");
        } else {
            displayChangeTextViewBM.setText(String.valueOf(change));
        }
        Log.d(TAG, "---- calculateAndDisplayChange (BM) Finished ----");
    }
}
