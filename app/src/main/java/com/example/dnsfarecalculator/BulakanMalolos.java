package com.example.dnsfarecalculator;

import android.content.Intent;
import android.content.res.Resources;
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
    private static final int CATEGORY_MALOLOS_MAYSANTOL = 11;
    private static final int CATEGORY_MALOLOS_SANNICOLAS = 12;
    private static final int CATEGORY_MALOLOS_PITPITAN = 13;
    private static final int CATEGORY_MALOLOS_MAMBOG = 14;
    private static final int CATEGORY_MALOLOS_MATIMBO = 15;
    private static final int CATEGORY_MALOLOS_PANASAHAN = 16;
    private static final int CATEGORY_MALOLOS_BAGNA = 17;
    private static final int CATEGORY_MALOLOS_ATLAG = 18;
    private static final int CATEGORY_MALOLOS_SANJUAN_STOROSARIO = 19;

    private static final int DISCOUNT_AMOUNT = 2;
    private static final int CATEGORY_MALOLOS_SAN_JOSE = 10;
    private static int FARE_BM_B_SANJOSE;


    // UI Elements
    private Spinner pickupSpinnerBM;
    private Spinner destinationSpinnerBM;
    private EditText amountPaidEditTextBM;
    private EditText numPassengersEditTextBM;
    private Button calculateRegularButtonBM;
    private Button calculateDiscountedButtonBM;
    private Button goBack;
    private Button clear;
    private TextView displayChangeTextViewBM;

    // Data arrays
    private String[] allLocationDisplayNames;
    private int selectedPickupCategoryBM = CATEGORY_PROMPT;
    private String selectedPickupNameBM = "";
    private int selectedDestinationCategoryBM = CATEGORY_PROMPT;
    private int[] allLocationCategories;
    private String selectedDestinationNameBM = "";


    // FARE constant declarations
    // Bulakan-Malolos Fares (Pickup from San Jose - Cat 10)
    //private int FARE_BM_B_SANJOSE; // San Jose to San Jose (if ever needed, usually 0 or error)
    private int FARE_BM_B_MAYSANTOL;
    private int FARE_BM_B_SANNICOLAS;
    private int FARE_BM_B_PITPITAN;
    private int FARE_BM_B_MAMBOG;
    private int FARE_BM_B_MATIMBO;
    private int FARE_BM_B_PANASAHAN;
    private int FARE_BM_B_BAGNA;
    private int FARE_BM_B_ATLAG;
    private int FARE_BM_B_SANJUAN; // To San Juan/Sto. Rosario

    // Pickup from Maysantol (Cat 11) - Bulakan-Malolos direction
    private int FARE_BM_MY_SN; // Maysantol to San Nicolas
    private int FARE_BM_MY_P;  // Maysantol to Pitpitan
    private int FARE_BM_MY_MM; // Maysantol to Mambog
    private int FARE_BM_MY_MT; // Maysantol to Matimbo
    private int FARE_BM_MY_PS; // Maysantol to Panasahan
    private int FARE_BM_MY_B;  // Maysantol to Bagna
    private int FARE_BM_MY_A;  // Maysantol to Atlag
    private int FARE_BM_MY_SJ_SANJUAN; // Maysantol to San Juan/Sto. Rosario

    // Pickup from San Nicolas (Cat 12) - Bulakan-Malolos direction
    private int FARE_BM_SN_P;
    private int FARE_BM_SN_MM;
    private int FARE_BM_SN_MT;
    private int FARE_BM_SN_PS;
    private int FARE_BM_SN_B;
    private int FARE_BM_SN_A;
    private int FARE_BM_SN_SJ_SANJUAN;

    // Pickup from Pitpitan (Cat 13) - Bulakan-Malolos direction
    private int FARE_BM_P_MM;
    private int FARE_BM_P_MT;
    private int FARE_BM_P_PS;
    private int FARE_BM_P_B;
    private int FARE_BM_P_A;
    private int FARE_BM_P_SJ_SANJUAN;

    // Pickup from Mambog (Cat 14) - Bulakan-Malolos direction
    private int FARE_BM_MM_MT;
    private int FARE_BM_MM_PS;
    private int FARE_BM_MM_B;
    private int FARE_BM_MM_A;
    private int FARE_BM_MM_SJ_SANJUAN;

    // Pickup from Matimbo (Cat 15) - Bulakan-Malolos direction
    private int FARE_BM_MT_PS;
    private int FARE_BM_MT_B;
    private int FARE_BM_MT_A;
    private int FARE_BM_MT_SJ_SANJUAN;

    // Pickup from Panasahan (Cat 16) - Bulakan-Malolos direction
    private int FARE_BM_PS_B;
    private int FARE_BM_PS_A;
    private int FARE_BM_PS_SJ_SANJUAN;

    // pickup from Bagna (Cat 17) - Bulakan-Malolos direction
    private int FARE_BM_B_A;
    private int FARE_BM_B_SJ_SANJUAN;

    // pickup from Atlag (Cat 18) - Bulakan-Malolos direction
    private int FARE_BM_A_SJ_SANJUAN;


    // Malolos-Bulakan Fares (Pickup from San Juan/Sto. Rosario - Cat 19)

    private int FARE_MB_SJ_ATLAG;
    private int FARE_MB_SJ_BAGNA;
    private int FARE_MB_SJ_PANASAHAN;
    private int FARE_MB_MATIMBO;  // SJ to Matimbo
    private int FARE_MB_MAMBOG;   // SJ to Mambog
    private int FARE_MB_PITPITAN; // SJ to Pitpitan
    private int FARE_MB_SANNICOLAS; // SJ to San Nicolas
    private int FARE_MB_MAYSANTOL;  // SJ to Maysantol
    private int FARE_MB_SANJOSE;    // SJ to Bagumbayan/San Jose

    // Pickup from Atlag (Cat 18) - Malolos-Bulakan direction
    private int FARE_MB_AT_B;
    private int FARE_MB_AT_PS;
    private int FARE_MB_AT_MT;
    private int FARE_MB_AT_MM;
    private int FARE_MB_AT_P;
    private int FARE_MB_AT_SN;
    private int FARE_MB_AT_MY;
    private int FARE_MB_AT_SJ_SANJOSE;

    // Pickup from Bagna (Cat 17) - Malolos-Bulakan direction
    private int FARE_MB_BG_PS;
    private int FARE_MB_BG_MT;
    private int FARE_MB_BG_MM;
    private int FARE_MB_BG_P;
    private int FARE_MB_BG_SN;
    private int FARE_MB_BG_MY;
    private int FARE_MB_BG_SJ_SANJOSE;

    // Pickup from Panasahan (Cat 16) - Malolos-Bulakan direction
    private int FARE_MB_PS_MT;
    private int FARE_MB_PS_MM;
    private int FARE_MB_PS_P;
    private int FARE_MB_PS_SN;
    private int FARE_MB_PS_MY;
    private int FARE_MB_PS_SJ_SANJOSE;

    // Pickup from Matimbo (Cat 15) - Malolos-Bulakan direction
    private int FARE_MB_MT_MM;
    private int FARE_MB_MT_P;
    private int FARE_MB_MT_SN;
    private int FARE_MB_MT_MY;
    private int FARE_MB_MT_SJ_SANJOSE;

    // Pickup from Mambog (Cat 14) - Malolos-Bulakan direction
    private int FARE_MB_MM_P;
    private int FARE_MB_MM_SN;
    private int FARE_MB_MM_MY;
    private int FARE_MB_MM_SJ_SANJOSE;

    // Pickup from Pitpitan (Cat 13) - Malolos-Bulakan direction
    private int FARE_MB_P_SN;
    private int FARE_MB_P_MY;
    private int FARE_MB_P_SJ_SANJOSE;

    // pickup from san nicolas (cat 12) - bulakan-malolos direction
    private int FARE_MB_SN_MY;
    private int FARE_MB_SN_SJ_SANJOSE;

    // pickup from maysantol (cat 11) - bulakan-malolos direction
    private int FARE_MB_MY_SJ_SANJOSE;


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
        Log.d(TAG, "onCreate: Starting MainActivity");


        Log.d(TAG, "onCreate: Starting BulakanMalolos");

        try {
            allLocationDisplayNames = getResources().getStringArray(R.array.all_location_display_names);
            allLocationCategories = getResources().getIntArray(R.array.all_location_categories);
            Log.d(TAG, "onCreate: Loaded allLocationDisplayNames: " + Arrays.toString(allLocationDisplayNames));
            Log.d(TAG, "onCreate: Loaded allLocationCategories: " + Arrays.toString(allLocationCategories));
            if (allLocationDisplayNames.length != allLocationCategories.length) {
                Log.e(TAG, "onCreate: WARNING! Display names and categories array lengths differ!");
                Toast.makeText(this, "Data Error: Array length mismatch!", Toast.LENGTH_LONG).show();
                finish(); return;
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error loading string/integer arrays: " + e.getMessage());
            Toast.makeText(this, "Error loading location data!", Toast.LENGTH_LONG).show();
            finish(); return;
        }

        loadFareConstants(); // Load ALL fare constants

        // Initialize UI Elements (ensure IDs match your activity_bulakan_malolos.xml)
        pickupSpinnerBM = findViewById(R.id.pickup);
        destinationSpinnerBM = findViewById(R.id.destination);
        amountPaidEditTextBM = findViewById(R.id.amount);
        numPassengersEditTextBM = findViewById(R.id.pass);
        calculateRegularButtonBM = findViewById(R.id.regular);
        calculateDiscountedButtonBM = findViewById(R.id.discounted);
        displayChangeTextViewBM = findViewById(R.id.displayChange);
        clear = findViewById(R.id.clear);

        clear.setOnClickListener(v -> {
            amountPaidEditTextBM.setText("");
            numPassengersEditTextBM.setText("");
            displayChangeTextViewBM.setText("0");
        });


        // Initialize Spinner Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, allLocationDisplayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickupSpinnerBM.setAdapter(adapter);
        destinationSpinnerBM.setAdapter(adapter);
        Log.d(TAG, "onCreate: Spinner adapter initialized");

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            // --- Initialize itemSelectedListener HERE ---
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    boolean isPickup = parent.getId() == R.id.pickup; // Check which spinner triggered this
                    String spinnerName = isPickup ? "PICKUP" : "DESTINATION";

                    if (position >= 0 && position < allLocationCategories.length && position < allLocationDisplayNames.length) {
                        if (isPickup) {
                            selectedPickupCategoryBM = allLocationCategories[position];
                            selectedPickupNameBM = allLocationDisplayNames[position];
                            Log.d(TAG, spinnerName + " Set: Name='" + selectedPickupNameBM + "', Category=" + selectedPickupCategoryBM + " at pos " + position);
                        } else { // It's the destination spinner
                            selectedDestinationCategoryBM = allLocationCategories[position];
                            selectedDestinationNameBM = allLocationDisplayNames[position];
                            Log.d(TAG, spinnerName + " Set: Name='" + selectedDestinationNameBM + "', Category=" + selectedDestinationCategoryBM + " at pos " + position);
                        }
                    } else {
                        Log.e(TAG, spinnerName + " Error: Position " + position + " is out of bounds for location arrays.");
                        // Fallback to prompt if position is invalid
                        if (isPickup) {
                            selectedPickupCategoryBM = CATEGORY_PROMPT;
                            selectedPickupNameBM = (allLocationDisplayNames.length > 0 && allLocationDisplayNames[0] != null) ? allLocationDisplayNames[0] : "--select--"; // Default to prompt
                        } else {
                            selectedDestinationCategoryBM = CATEGORY_PROMPT;
                            selectedDestinationNameBM = (allLocationDisplayNames.length > 0 && allLocationDisplayNames[0] != null) ? allLocationDisplayNames[0] : "--select--"; // Default to prompt
                        }
                        Toast.makeText(BulakanMalolos.this, "Invalid selection, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    boolean isPickup = parent.getId() == R.id.pickup;
                    if (isPickup) {
                        selectedPickupCategoryBM = CATEGORY_PROMPT;
                        selectedPickupNameBM = "";
                        Log.d(TAG, "PICKUP: Nothing selected.");
                    } else {
                        selectedDestinationCategoryBM = CATEGORY_PROMPT;
                        selectedDestinationNameBM = "";
                        Log.d(TAG, "DESTINATION: Nothing selected.");
                    }
                }
            };
            // --- End of itemSelectedListener initialization ---
        pickupSpinnerBM.setOnItemSelectedListener(itemSelectedListener);
        destinationSpinnerBM.setOnItemSelectedListener(itemSelectedListener);

        // button on click listener
        calculateRegularButtonBM.setOnClickListener(v -> calculateAndDisplayChange(false));
        calculateDiscountedButtonBM.setOnClickListener(v -> calculateAndDisplayChange(true));
}

    private void calculateAndDisplayChange(boolean isDiscounted) {
        Log.d(TAG, "---- calculateAndDisplayChange (BM) Called ----");
        Log.d(TAG, "Pickup Cat: " + selectedPickupCategoryBM + " (" + selectedPickupNameBM + ")");
        Log.d(TAG, "Dest Cat: " + selectedDestinationCategoryBM + " (" + selectedDestinationNameBM + ")");

        if (selectedPickupCategoryBM == CATEGORY_PROMPT || selectedDestinationCategoryBM == CATEGORY_PROMPT) {
            Toast.makeText(this, "Please select locations.", Toast.LENGTH_SHORT).show();
            displayChangeTextViewBM.setText("0");
            return;
        }
        // ... (input validation for amount and passengers as before) ...
        String amountPaidStr = amountPaidEditTextBM.getText().toString();
        String numPassengersStr = numPassengersEditTextBM.getText().toString();
        if (TextUtils.isEmpty(amountPaidStr) || TextUtils.isEmpty(numPassengersStr)) {
            Toast.makeText(this, "Enter amount and passengers.", Toast.LENGTH_SHORT).show();
            displayChangeTextViewBM.setText("0");
            return;
        }
        int amountPaid;
        int numPassengers;
        try {
            amountPaid = Integer.parseInt(amountPaidStr);
            numPassengers = Integer.parseInt(numPassengersStr);
            if (numPassengers <= 0) {
                Toast.makeText(this, "Passengers must be > 0.", Toast.LENGTH_SHORT).show();
                displayChangeTextViewBM.setText("0");
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format.", Toast.LENGTH_SHORT).show();
            displayChangeTextViewBM.setText("0");
            return;
        }

        int baseRegularFarePerPassenger = -1;

        // --- Bulakan-Malolos Direction Fares ---
        if (selectedPickupCategoryBM == CATEGORY_BULAKAN_SAN_JOSE) { // Cat 10 (San Jose)
            Log.d(TAG, "Pickup: San Jose (B->M)");
            switch (selectedDestinationCategoryBM) {
                // San Jose to San Jose is not a priced route, will go to default
                case CATEGORY_MALOLOS_MAYSANTOL:
                    baseRegularFarePerPassenger = FARE_BM_B_MAYSANTOL;
                    break; // 11
                case CATEGORY_MALOLOS_SANNICOLAS:
                    baseRegularFarePerPassenger = FARE_BM_B_SANNICOLAS;
                    break; // 12
                case CATEGORY_MALOLOS_PITPITAN:
                    baseRegularFarePerPassenger = FARE_BM_B_PITPITAN;
                    break;   // 13
                case CATEGORY_MALOLOS_MAMBOG:
                    baseRegularFarePerPassenger = FARE_BM_B_MAMBOG;
                    break;     // 14
                case CATEGORY_MALOLOS_MATIMBO:
                    baseRegularFarePerPassenger = FARE_BM_B_MATIMBO;
                    break;    // 15
                case CATEGORY_MALOLOS_PANASAHAN:
                    baseRegularFarePerPassenger = FARE_BM_B_PANASAHAN;
                    break;  // 16
                case CATEGORY_MALOLOS_BAGNA:
                    baseRegularFarePerPassenger = FARE_BM_B_BAGNA;
                    break;      // 17
                case CATEGORY_MALOLOS_ATLAG:
                    baseRegularFarePerPassenger = FARE_BM_B_ATLAG;
                    break;      // 18
                case CATEGORY_MALOLOS_SANJUAN_STOROSARIO:
                    baseRegularFarePerPassenger = FARE_BM_B_SANJUAN;
                    break; // 19
                default:
                    Log.d(TAG, "B(SJ)->M: Dest Cat " + selectedDestinationCategoryBM + " not in San Jose's B->M matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_MAYSANTOL) { // Cat 11 (Maysantol)
            Log.d(TAG, "Pickup: Maysantol (B->M direction)");
            switch (selectedDestinationCategoryBM) {
                // Maysantol to San Jose (Bulakan) is not in this fare group (fare_bm_...)
                // It would be a fare_mb_my_sj type fare if defined
                case CATEGORY_MALOLOS_SANNICOLAS:
                    baseRegularFarePerPassenger = FARE_BM_MY_SN;
                    break;
                case CATEGORY_MALOLOS_PITPITAN:
                    baseRegularFarePerPassenger = FARE_BM_MY_P;
                    break;
                case CATEGORY_MALOLOS_MAMBOG:
                    baseRegularFarePerPassenger = FARE_BM_MY_MM;
                    break;
                case CATEGORY_MALOLOS_MATIMBO:
                    baseRegularFarePerPassenger = FARE_BM_MY_MT;
                    break;
                case CATEGORY_MALOLOS_PANASAHAN:
                    baseRegularFarePerPassenger = FARE_BM_MY_PS;
                    break;
                case CATEGORY_MALOLOS_BAGNA:
                    baseRegularFarePerPassenger = FARE_BM_MY_B;
                    break;
                case CATEGORY_MALOLOS_ATLAG:
                    baseRegularFarePerPassenger = FARE_BM_MY_A;
                    break;
                case CATEGORY_MALOLOS_SANJUAN_STOROSARIO:
                    baseRegularFarePerPassenger = FARE_BM_MY_SJ_SANJUAN;
                    break;
                default:
                    Log.d(TAG, "B(MY)->M: Dest Cat " + selectedDestinationCategoryBM + " not in Maysantol's B->M matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_SANNICOLAS) { // Cat 12 (San Nicolas)
            Log.d(TAG, "Pickup: San Nicolas (B->M direction)");
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_PITPITAN:
                    baseRegularFarePerPassenger = FARE_BM_SN_P;
                    break;
                case CATEGORY_MALOLOS_MAMBOG:
                    baseRegularFarePerPassenger = FARE_BM_SN_MM;
                    break;
                case CATEGORY_MALOLOS_MATIMBO:
                    baseRegularFarePerPassenger = FARE_BM_SN_MT;
                    break;
                case CATEGORY_MALOLOS_PANASAHAN:
                    baseRegularFarePerPassenger = FARE_BM_SN_PS;
                    break;
                case CATEGORY_MALOLOS_BAGNA:
                    baseRegularFarePerPassenger = FARE_BM_SN_B;
                    break;
                case CATEGORY_MALOLOS_ATLAG:
                    baseRegularFarePerPassenger = FARE_BM_SN_A;
                    break;
                case CATEGORY_MALOLOS_SANJUAN_STOROSARIO:
                    baseRegularFarePerPassenger = FARE_BM_SN_SJ_SANJUAN;
                    break;
                default:
                    Log.d(TAG, "B(SN)->M: Dest Cat " + selectedDestinationCategoryBM + " not in San Nicolas' B->M matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_PITPITAN) { // Cat 13 (Pitpitan)
            Log.d(TAG, "Pickup: Pitpitan (B->M direction)");
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_MAMBOG:
                    baseRegularFarePerPassenger = FARE_BM_P_MM;
                    break;
                case CATEGORY_MALOLOS_MATIMBO:
                    baseRegularFarePerPassenger = FARE_BM_P_MT;
                    break;
                case CATEGORY_MALOLOS_PANASAHAN:
                    baseRegularFarePerPassenger = FARE_BM_P_PS;
                    break;
                case CATEGORY_MALOLOS_BAGNA:
                    baseRegularFarePerPassenger = FARE_BM_P_B;
                    break;
                case CATEGORY_MALOLOS_ATLAG:
                    baseRegularFarePerPassenger = FARE_BM_P_A;
                    break;
                case CATEGORY_MALOLOS_SANJUAN_STOROSARIO:
                    baseRegularFarePerPassenger = FARE_BM_P_SJ_SANJUAN;
                    break;
                default:
                    Log.d(TAG, "B(P)->M: Dest Cat " + selectedDestinationCategoryBM + " not in Pitpitan's B->M matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_MAMBOG) { // Cat 14 (Mambog)
            Log.d(TAG, "Pickup: Mambog (B->M direction)");
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_MATIMBO:
                    baseRegularFarePerPassenger = FARE_BM_MM_MT;
                    break;
                case CATEGORY_MALOLOS_PANASAHAN:
                    baseRegularFarePerPassenger = FARE_BM_MM_PS;
                    break;
                case CATEGORY_MALOLOS_BAGNA:
                    baseRegularFarePerPassenger = FARE_BM_MM_B;
                    break;
                case CATEGORY_MALOLOS_ATLAG:
                    baseRegularFarePerPassenger = FARE_BM_MM_A;
                    break;
                case CATEGORY_MALOLOS_SANJUAN_STOROSARIO:
                    baseRegularFarePerPassenger = FARE_BM_MM_SJ_SANJUAN;
                    break;
                default:
                    Log.d(TAG, "B(MM)->M: Dest Cat " + selectedDestinationCategoryBM + " not in Mambog's B->M matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_MATIMBO) { // Cat 15 (Matimbo)
            Log.d(TAG, "Pickup: Matimbo (B->M direction)");
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_PANASAHAN:
                    baseRegularFarePerPassenger = FARE_BM_MT_PS;
                    break;
                case CATEGORY_MALOLOS_BAGNA:
                    baseRegularFarePerPassenger = FARE_BM_MT_B;
                    break;
                case CATEGORY_MALOLOS_ATLAG:
                    baseRegularFarePerPassenger = FARE_BM_MT_A;
                    break;
                case CATEGORY_MALOLOS_SANJUAN_STOROSARIO:
                    baseRegularFarePerPassenger = FARE_BM_MT_SJ_SANJUAN;
                    break;
                default:
                    Log.d(TAG, "B(MT)->M: Dest Cat " + selectedDestinationCategoryBM + " not in Matimbo's B->M matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_PANASAHAN) { // Cat 16 (Panasahan)
            Log.d(TAG, "Pickup: Panasahan (B->M direction)");
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_BAGNA:
                    baseRegularFarePerPassenger = FARE_BM_PS_B;
                    break;
                case CATEGORY_MALOLOS_ATLAG:
                    baseRegularFarePerPassenger = FARE_BM_PS_A;
                    break;
                case CATEGORY_MALOLOS_SANJUAN_STOROSARIO:
                    baseRegularFarePerPassenger = FARE_BM_PS_SJ_SANJUAN;
                    break;
                default:
                    Log.d(TAG, "B(PS)->M: Dest Cat " + selectedDestinationCategoryBM + " not in Panasahan's B->M matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_BAGNA) { // Cat 17 (Bagna)
            Log.d(TAG, "Pickup: Bagna (B->M direction)");
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_ATLAG:
                    baseRegularFarePerPassenger = FARE_BM_B_A;
                    break;
                case CATEGORY_MALOLOS_SANJUAN_STOROSARIO:
                    baseRegularFarePerPassenger = FARE_BM_B_SJ_SANJUAN;
                    break;
                default:
                    Log.d(TAG, "B(BG)->M: Dest Cat " + selectedDestinationCategoryBM + " not in Bagna's B->M matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_ATLAG) { // Cat 18 (Atlag)
            Log.d(TAG, "Pickup: Atlag (B->M direction)");
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_SANJUAN_STOROSARIO:
                    baseRegularFarePerPassenger = FARE_BM_A_SJ_SANJUAN;
                    break;
                default:
                    Log.d(TAG, "B(AT)->M: Dest Cat " + selectedDestinationCategoryBM + " not in Atlag's B->M matrix.");
                    break;
            }
        }

        // --- Malolos-Bulakan Direction Fares ---
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_SANJUAN_STOROSARIO) { // Cat 19 (San Juan/Sto. Rosario)
            Log.d(TAG, "Pickup: San Juan/Sto. Rosario (M->B)");
            switch (selectedDestinationCategoryBM) {
                // San Juan to San Juan not priced, goes to default
                case CATEGORY_MALOLOS_ATLAG:
                    baseRegularFarePerPassenger = FARE_MB_SJ_ATLAG;
                    break; // 18
                case CATEGORY_MALOLOS_BAGNA:
                    baseRegularFarePerPassenger = FARE_MB_SJ_BAGNA;
                    break; // 17
                case CATEGORY_MALOLOS_PANASAHAN:
                    baseRegularFarePerPassenger = FARE_MB_SJ_PANASAHAN;
                    break; // 16
                // Note: Your XML for fare_mb_sj_sanjuan, _atlag, _bagna, _panasahan are all 13.

                // The XML has fare_mb_sj_sanjuan = 13. This conflicts with "same location = error".
                // For now, I'll assume it's for SJ to other listed locations that share that 13 fare.

                case CATEGORY_MALOLOS_MATIMBO:
                    baseRegularFarePerPassenger = FARE_MB_MATIMBO;
                    break;  // 15
                case CATEGORY_MALOLOS_MAMBOG:
                    baseRegularFarePerPassenger = FARE_MB_MAMBOG;
                    break;   // 14
                case CATEGORY_MALOLOS_PITPITAN:
                    baseRegularFarePerPassenger = FARE_MB_PITPITAN;
                    break; // 13
                case CATEGORY_MALOLOS_SANNICOLAS:
                    baseRegularFarePerPassenger = FARE_MB_SANNICOLAS;
                    break; // 12
                case CATEGORY_MALOLOS_MAYSANTOL:
                    baseRegularFarePerPassenger = FARE_MB_MAYSANTOL;
                    break;  // 11
                case CATEGORY_BULAKAN_SAN_JOSE:
                    baseRegularFarePerPassenger = FARE_MB_SANJOSE;
                    break;    // 10 (San Jose, Bulakan)
                default:
                    Log.d(TAG, "M(SJ)->B: Dest Cat " + selectedDestinationCategoryBM + " not in San Juan's M->B matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_ATLAG) { // Cat 18 (Atlag)
            Log.d(TAG, "Pickup: Atlag (M->B)");
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_BAGNA:
                    baseRegularFarePerPassenger = FARE_MB_AT_B;
                    break;
                case CATEGORY_MALOLOS_PANASAHAN:
                    baseRegularFarePerPassenger = FARE_MB_AT_PS;
                    break;
                case CATEGORY_MALOLOS_MATIMBO:
                    baseRegularFarePerPassenger = FARE_MB_AT_MT;
                    break;
                case CATEGORY_MALOLOS_MAMBOG:
                    baseRegularFarePerPassenger = FARE_MB_AT_MM;
                    break;
                case CATEGORY_MALOLOS_PITPITAN:
                    baseRegularFarePerPassenger = FARE_MB_AT_P;
                    break;
                case CATEGORY_MALOLOS_SANNICOLAS:
                    baseRegularFarePerPassenger = FARE_MB_AT_SN;
                    break;
                case CATEGORY_MALOLOS_MAYSANTOL:
                    baseRegularFarePerPassenger = FARE_MB_AT_MY;
                    break;
                case CATEGORY_BULAKAN_SAN_JOSE:
                    baseRegularFarePerPassenger = FARE_MB_AT_SJ_SANJOSE;
                    break;
                default:
                    Log.d(TAG, "M(AT)->B: Dest Cat " + selectedDestinationCategoryBM + " not in Atlag's M->B matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_BAGNA) { // Cat 17 (Bagna)
            Log.d(TAG, "Pickup: Bagna (M->B)");
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_PANASAHAN:
                    baseRegularFarePerPassenger = FARE_MB_BG_PS;
                    break;
                case CATEGORY_MALOLOS_MATIMBO:
                    baseRegularFarePerPassenger = FARE_MB_BG_MT;
                    break;
                case CATEGORY_MALOLOS_MAMBOG:
                    baseRegularFarePerPassenger = FARE_MB_BG_MM;
                    break;
                case CATEGORY_MALOLOS_PITPITAN:
                    baseRegularFarePerPassenger = FARE_MB_BG_P;
                    break;
                case CATEGORY_MALOLOS_SANNICOLAS:
                    baseRegularFarePerPassenger = FARE_MB_BG_SN;
                    break;
                case CATEGORY_MALOLOS_MAYSANTOL:
                    baseRegularFarePerPassenger = FARE_MB_BG_MY;
                    break;
                case CATEGORY_BULAKAN_SAN_JOSE:
                    baseRegularFarePerPassenger = FARE_MB_BG_SJ_SANJOSE;
                    break;
                default:
                    Log.d(TAG, "M(BG)->B: Dest Cat " + selectedDestinationCategoryBM + " not in Bagna's M->B matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_PANASAHAN) { // Cat 16 (Panasahan)
            Log.d(TAG, "Pickup: Panasahan (M->B)");
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_MATIMBO:
                    baseRegularFarePerPassenger = FARE_MB_PS_MT;
                    break;
                case CATEGORY_MALOLOS_MAMBOG:
                    baseRegularFarePerPassenger = FARE_MB_PS_MM;
                    break;
                case CATEGORY_MALOLOS_PITPITAN:
                    baseRegularFarePerPassenger = FARE_MB_PS_P;
                    break;
                case CATEGORY_MALOLOS_SANNICOLAS:
                    baseRegularFarePerPassenger = FARE_MB_PS_SN;
                    break;
                case CATEGORY_MALOLOS_MAYSANTOL:
                    baseRegularFarePerPassenger = FARE_MB_PS_MY;
                    break;
                case CATEGORY_BULAKAN_SAN_JOSE:
                    baseRegularFarePerPassenger = FARE_MB_PS_SJ_SANJOSE;
                    break;
                default:
                    Log.d(TAG, "M(PS)->B: Dest Cat " + selectedDestinationCategoryBM + " not in Panasahan's M->B matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_MATIMBO) { // Cat 15 (Matimbo)
            Log.d(TAG, "Pickup: Matimbo (M->B)");
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_MAMBOG:
                    baseRegularFarePerPassenger = FARE_MB_MT_MM;
                    break;
                case CATEGORY_MALOLOS_PITPITAN:
                    baseRegularFarePerPassenger = FARE_MB_MT_P;
                    break;
                case CATEGORY_MALOLOS_SANNICOLAS:
                    baseRegularFarePerPassenger = FARE_MB_MT_SN;
                    break;
                case CATEGORY_MALOLOS_MAYSANTOL:
                    baseRegularFarePerPassenger = FARE_MB_MT_MY;
                    break;
                case CATEGORY_BULAKAN_SAN_JOSE:
                    baseRegularFarePerPassenger = FARE_MB_MT_SJ_SANJOSE;
                    break;
                default:
                    Log.d(TAG, "M(MT)->B: Dest Cat " + selectedDestinationCategoryBM + " not in Matimbo's M->B matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_MAMBOG) { // Cat 14 (Mambog)
            Log.d(TAG, "Pickup: Mambog (M->B)");
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_PITPITAN:
                    baseRegularFarePerPassenger = FARE_MB_MM_P;
                    break;
                case CATEGORY_MALOLOS_SANNICOLAS:
                    baseRegularFarePerPassenger = FARE_MB_MM_SN;
                    break;
                case CATEGORY_MALOLOS_MAYSANTOL:
                    baseRegularFarePerPassenger = FARE_MB_MM_MY;
                    break;
                case CATEGORY_BULAKAN_SAN_JOSE:
                    baseRegularFarePerPassenger = FARE_MB_MM_SJ_SANJOSE;
                    break;
                default:
                    Log.d(TAG, "M(MM)->B: Dest Cat " + selectedDestinationCategoryBM + " not in Mambog's M->B matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_PITPITAN) { // Cat 13 (Pitpitan)
            Log.d(TAG, "Pickup: Pitpitan (M->B)");
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_SANNICOLAS:
                    baseRegularFarePerPassenger = FARE_MB_P_SN;
                    break;
                case CATEGORY_MALOLOS_MAYSANTOL:
                    baseRegularFarePerPassenger = FARE_MB_P_MY;
                    break;
                case CATEGORY_BULAKAN_SAN_JOSE:
                    baseRegularFarePerPassenger = FARE_MB_P_SJ_SANJOSE;
                    break;
                default:
                    Log.d(TAG, "M(P)->B: Dest Cat " + selectedDestinationCategoryBM + " not in Pitpitan's M->B matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_SANNICOLAS) { // Cat 12 (San Nicolas)
            Log.d(TAG, "Pickup: San Nicolas (M->B)");
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_MAYSANTOL:
                    baseRegularFarePerPassenger = FARE_MB_SN_MY;
                    break;
                case CATEGORY_MALOLOS_SAN_JOSE:
                    baseRegularFarePerPassenger = FARE_MB_SN_SJ_SANJOSE;
                    break;
                default:
                    Log.d(TAG, "M(SN)->B: Dest Cat " + selectedDestinationCategoryBM + " not in San Nicolas' M->B matrix.");
                    break;
            }
        }
        else if (selectedPickupCategoryBM == CATEGORY_MALOLOS_MAYSANTOL) { // Cat 11 (Maysantol)
            Log.d(TAG, "Pickup: Maysantol (M->B)");
            switch (selectedDestinationCategoryBM) {
                case CATEGORY_MALOLOS_SAN_JOSE:
                    baseRegularFarePerPassenger = FARE_MB_MY_SJ_SANJOSE;
                    break;
                default:
                    Log.d(TAG, "M(MY)->B: Dest Cat " + selectedDestinationCategoryBM + " not in Maysantol's M->B matrix.");
                    break;
            }
        }
            Log.d(TAG, "BaseRegularFarePerPassenger after rules: " + baseRegularFarePerPassenger);

            if (baseRegularFarePerPassenger == -1) {
                // This will be true if same pickup/dest and not explicitly priced,
                // or if the specific route combination isn't in any of the switch cases.
                Toast.makeText(this, "Fare rule not found for this route.", Toast.LENGTH_LONG).show();
                displayChangeTextViewBM.setText("0");
                return;
            }

            int actualFarePerPassenger = baseRegularFarePerPassenger;
            if (isDiscounted && baseRegularFarePerPassenger > 0) {
                actualFarePerPassenger -= DISCOUNT_AMOUNT;
                if (actualFarePerPassenger < 0) actualFarePerPassenger = 0;
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
    private void loadFareConstants() {
        try {
            // Bulakan-Malolos Fares (Pickup from San Jose - Cat 10)
            FARE_BM_B_SANJOSE = getResources().getInteger(R.integer.fare_bm_b_sanjose);
            FARE_BM_B_MAYSANTOL = getResources().getInteger(R.integer.fare_bm_b_maysantol);
            FARE_BM_B_SANNICOLAS = getResources().getInteger(R.integer.fare_bm_b_sannicolas);
            FARE_BM_B_PITPITAN = getResources().getInteger(R.integer.fare_bm_b_pitpitan);
            FARE_BM_B_MAMBOG = getResources().getInteger(R.integer.fare_bm_b_mambog);
            FARE_BM_B_MATIMBO = getResources().getInteger(R.integer.fare_bm_b_matimbo);
            FARE_BM_B_PANASAHAN = getResources().getInteger(R.integer.fare_bm_b_panasahan);
            FARE_BM_B_BAGNA = getResources().getInteger(R.integer.fare_bm_b_bagna);
            FARE_BM_B_ATLAG = getResources().getInteger(R.integer.fare_bm_b_atlag);
            FARE_BM_B_SANJUAN = getResources().getInteger(R.integer.fare_bm_b_sanjuan);

            // Pickup from Maysantol (Cat 11)
            FARE_BM_MY_SN = getResources().getInteger(R.integer.fare_bm_my_sn);
            FARE_BM_MY_P = getResources().getInteger(R.integer.fare_bm_my_p);
            FARE_BM_MY_MM = getResources().getInteger(R.integer.fare_bm_my_mm);
            FARE_BM_MY_MT = getResources().getInteger(R.integer.fare_bm_my_mt);
            FARE_BM_MY_PS = getResources().getInteger(R.integer.fare_bm_my_ps);
            FARE_BM_MY_B = getResources().getInteger(R.integer.fare_bm_my_b);
            FARE_BM_MY_A = getResources().getInteger(R.integer.fare_bm_my_a);
            FARE_BM_MY_SJ_SANJUAN = getResources().getInteger(R.integer.fare_bm_my_sj_sanjuan);

            // Pickup from San Nicolas (Cat 12)
            FARE_BM_SN_P = getResources().getInteger(R.integer.fare_bm_sn_p);
            FARE_BM_SN_MM = getResources().getInteger(R.integer.fare_bm_sn_mm);
            FARE_BM_SN_MT = getResources().getInteger(R.integer.fare_bm_sn_mt);
            FARE_BM_SN_PS = getResources().getInteger(R.integer.fare_bm_sn_ps);
            FARE_BM_SN_B = getResources().getInteger(R.integer.fare_bm_sn_b);
            FARE_BM_SN_A = getResources().getInteger(R.integer.fare_bm_sn_a);
            FARE_BM_SN_SJ_SANJUAN = getResources().getInteger(R.integer.fare_bm_sn_sj_sanjuan);

            // Pickup from Pitpitan (Cat 13)
            FARE_BM_P_MM = getResources().getInteger(R.integer.fare_bm_p_mm);
            FARE_BM_P_MT = getResources().getInteger(R.integer.fare_bm_p_mt);
            FARE_BM_P_PS = getResources().getInteger(R.integer.fare_bm_p_ps);
            FARE_BM_P_B = getResources().getInteger(R.integer.fare_bm_p_b);
            FARE_BM_P_A = getResources().getInteger(R.integer.fare_bm_p_a);
            FARE_BM_P_SJ_SANJUAN = getResources().getInteger(R.integer.fare_bm_p_sj_sanjuan);

            // Pickup from Mambog (Cat 14)
            FARE_BM_MM_MT = getResources().getInteger(R.integer.fare_bm_mm_mt);
            FARE_BM_MM_PS = getResources().getInteger(R.integer.fare_bm_mm_ps);
            FARE_BM_MM_B = getResources().getInteger(R.integer.fare_bm_mm_b);
            FARE_BM_MM_A = getResources().getInteger(R.integer.fare_bm_mm_a);
            FARE_BM_MM_SJ_SANJUAN = getResources().getInteger(R.integer.fare_bm_mm_sj_sanjuan);

            // Pickup from Matimbo (Cat 15)
            FARE_BM_MT_PS = getResources().getInteger(R.integer.fare_bm_mt_ps);
            FARE_BM_MT_B = getResources().getInteger(R.integer.fare_bm_mt_b);
            FARE_BM_MT_A = getResources().getInteger(R.integer.fare_bm_mt_a);
            FARE_BM_MT_SJ_SANJUAN = getResources().getInteger(R.integer.fare_bm_mt_sj_sanjuan);

            // Pickup from Panasahan (Cat 16)
            FARE_BM_PS_B = getResources().getInteger(R.integer.fare_bm_ps_b);
            FARE_BM_PS_A = getResources().getInteger(R.integer.fare_bm_ps_a);
            FARE_BM_PS_SJ_SANJUAN = getResources().getInteger(R.integer.fare_bm_ps_sj_sanjuan);

            // pickup from Bagna (Cat 17)
            FARE_BM_B_A = getResources().getInteger(R.integer.fare_bm_b_a);
            FARE_BM_B_SJ_SANJUAN = getResources().getInteger(R.integer.fare_bm_b_sj_sanjuan);

            // pickup from Atlag (Cat 18)
            FARE_BM_A_SJ_SANJUAN = getResources().getInteger(R.integer.fare_bm_a_sj_sanjuan);


            // Malolos-Bulakan Fares (Pickup from San Juan/Sto. Rosario - Cat 19)
            //FARE_MB_SJ_SANJUAN = getResources().getInteger(R.integer.fare_mb_sj_sanjuan);
            FARE_MB_SJ_ATLAG = getResources().getInteger(R.integer.fare_mb_sj_atlag);
            FARE_MB_SJ_BAGNA = getResources().getInteger(R.integer.fare_mb_sj_bagna);
            FARE_MB_SJ_PANASAHAN = getResources().getInteger(R.integer.fare_mb_sj_panasahan);
            FARE_MB_MATIMBO = getResources().getInteger(R.integer.fare_mb_matimbo);
            FARE_MB_MAMBOG = getResources().getInteger(R.integer.fare_mb_mambog);
            FARE_MB_PITPITAN = getResources().getInteger(R.integer.fare_mb_pitpitan);
            FARE_MB_SANNICOLAS = getResources().getInteger(R.integer.fare_mb_sannicolas);
            FARE_MB_MAYSANTOL = getResources().getInteger(R.integer.fare_mb_maysantol);
            FARE_MB_SANJOSE = getResources().getInteger(R.integer.fare_mb_sanjose);

            // Pickup from Atlag (Cat 18) - M->B
            FARE_MB_AT_B = getResources().getInteger(R.integer.fare_mb_at_b);
            FARE_MB_AT_PS = getResources().getInteger(R.integer.fare_mb_at_ps);
            FARE_MB_AT_MT = getResources().getInteger(R.integer.fare_mb_at_mt);
            FARE_MB_AT_MM = getResources().getInteger(R.integer.fare_mb_at_mm);
            FARE_MB_AT_P = getResources().getInteger(R.integer.fare_mb_at_p);
            FARE_MB_AT_SN = getResources().getInteger(R.integer.fare_mb_at_sn);
            FARE_MB_AT_MY = getResources().getInteger(R.integer.fare_mb_at_my);
            FARE_MB_AT_SJ_SANJOSE = getResources().getInteger(R.integer.fare_mb_at_sj_sanjose);

            // Pickup from Bagna (Cat 17) - M->B
            FARE_MB_BG_PS = getResources().getInteger(R.integer.fare_mb_bg_ps);
            FARE_MB_BG_MT = getResources().getInteger(R.integer.fare_mb_bg_mt);
            FARE_MB_BG_MM = getResources().getInteger(R.integer.fare_mb_bg_mm);
            FARE_MB_BG_P = getResources().getInteger(R.integer.fare_mb_bg_p);
            FARE_MB_BG_SN = getResources().getInteger(R.integer.fare_mb_bg_sn);
            FARE_MB_BG_MY = getResources().getInteger(R.integer.fare_mb_bg_my);
            FARE_MB_BG_SJ_SANJOSE = getResources().getInteger(R.integer.fare_mb_bg_sj_sanjose);

            // Pickup from Panasahan (Cat 16) - M->B
            FARE_MB_PS_MT = getResources().getInteger(R.integer.fare_mb_ps_mt);
            FARE_MB_PS_MM = getResources().getInteger(R.integer.fare_mb_ps_mm);
            FARE_MB_PS_P = getResources().getInteger(R.integer.fare_mb_ps_p);
            FARE_MB_PS_SN = getResources().getInteger(R.integer.fare_mb_ps_sn);
            FARE_MB_PS_MY = getResources().getInteger(R.integer.fare_mb_ps_my);
            FARE_MB_PS_SJ_SANJOSE = getResources().getInteger(R.integer.fare_mb_ps_sj_sanjose);

            // Pickup from Matimbo (Cat 15) - M->B
            FARE_MB_MT_MM = getResources().getInteger(R.integer.fare_mb_mt_mm);
            FARE_MB_MT_P = getResources().getInteger(R.integer.fare_mb_mt_p);
            FARE_MB_MT_SN = getResources().getInteger(R.integer.fare_mb_mt_sn);
            FARE_MB_MT_MY = getResources().getInteger(R.integer.fare_mb_mt_my);
            FARE_MB_MT_SJ_SANJOSE = getResources().getInteger(R.integer.fare_mb_mt_sj_sanjose);

            // Pickup from Mambog (Cat 14) - M->B
            FARE_MB_MM_P = getResources().getInteger(R.integer.fare_mb_mm_p);
            FARE_MB_MM_SN = getResources().getInteger(R.integer.fare_mb_mm_sn);
            FARE_MB_MM_MY = getResources().getInteger(R.integer.fare_mb_mm_my);
            FARE_MB_MM_SJ_SANJOSE = getResources().getInteger(R.integer.fare_mb_mm_sj_sanjose);

            // Pickup from Pitpitan (Cat 13) - M->B
            FARE_MB_P_SN = getResources().getInteger(R.integer.fare_mb_p_sn);
            FARE_MB_P_MY = getResources().getInteger(R.integer.fare_mb_p_my);
            FARE_MB_P_SJ_SANJOSE = getResources().getInteger(R.integer.fare_mb_p_sj_sanjose);

            // pickup from Maysantol (Cat 11) - M->B
            FARE_MB_MY_SJ_SANJOSE = getResources().getInteger(R.integer.fare_mb_my_sj_sanjose);

        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "A fare constant was not found in XML during loadFareConstants: " + e.getMessage());
            Toast.makeText(this, "Critical fare data missing! Please check XML.", Toast.LENGTH_LONG).show();
            // Consider disabling calculation buttons if this happens
            calculateRegularButtonBM.setEnabled(false);
            calculateDiscountedButtonBM.setEnabled(false);
        }
        Log.d(TAG, "loadFareConstants: Loaded all fare constants");
    }
}
