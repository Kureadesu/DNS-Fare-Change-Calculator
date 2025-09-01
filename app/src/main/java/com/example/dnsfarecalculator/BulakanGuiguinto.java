package com.example.dnsfarecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
// It's good practice to import specific classes you use
// import android.widget.Spinner; // Not used in the provided snippet
// import android.widget.TextView; // Not used in the provided snippet

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BulakanGuiguinto extends AppCompatActivity {

    private final int REGULAR_FARE_AMOUNT = 13; // Use constants for fare values
    private final int DISCOUNTED_FARE_DIFFERENCE = 2; // Discount amount

    // Member variable to store the calculated change
    private int calculatedChange; // Renamed for clarity

    private Button discountedButton, // Renamed for clarity
            regularButton,
            backButton;    // Renamed for clarity
    private EditText amountEditText,    // Renamed for clarity
            passengersEditText; // Renamed for clarity
    private TextView displayChangeEditText;


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

        // Instantiate variables
        discountedButton = findViewById(R.id.discounted); // Make sure these IDs match your XML
        regularButton = findViewById(R.id.regular);     // Make sure these IDs match your XML
        amountEditText = findViewById(R.id.amount);
        passengersEditText = findViewById(R.id.pass);
        displayChangeEditText = findViewById(R.id.displayChange);
        backButton = findViewById(R.id.Back);


        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        discountedButton.setOnClickListener(v -> {
            try {
                int paidAmount = Integer.parseInt(amountEditText.getText().toString());
                int numPassengers = Integer.parseInt(passengersEditText.getText().toString());

                calculatedChange = computeFare(paidAmount, numPassengers, true); // true for discounted
                displayChangeEditText.setText(String.valueOf(calculatedChange));

            } catch (NumberFormatException e) {
                displayChangeEditText.setText("Invalid input");
                // Optionally show a Toast message to the user
            }
        });

        regularButton.setOnClickListener(v -> {
            try {
                int paidAmount = Integer.parseInt(amountEditText.getText().toString());
                int numPassengers = Integer.parseInt(passengersEditText.getText().toString());

                calculatedChange = computeFare(paidAmount, numPassengers, false); // false for regular
                displayChangeEditText.setText(String.valueOf(calculatedChange));

            } catch (NumberFormatException e) {
                displayChangeEditText.setText("Invalid input");
                // Optionally show a Toast message to the user
            }
        });
    }


    // Refactored fare logic
    // This method now returns the calculated change
    public int computeFare(int paidAmount, int numPassengers, boolean isDiscounted) {
        if (numPassengers <= 0) {
            return 0; // Or handle as an error, e.g., by throwing an IllegalArgumentException
        }

        int totalFare = REGULAR_FARE_AMOUNT * numPassengers;

        if (isDiscounted) {
            // Ensure discount doesn't make fare negative, though with 13 base and 2 discount, it's unlikely
            totalFare = Math.max(0, totalFare - (DISCOUNTED_FARE_DIFFERENCE * numPassengers)); // Apply discount per passenger
            // Or if discount is a flat 2 pesos off the total:
            // totalFare = Math.max(0, totalFare - DISCOUNTED_FARE_DIFFERENCE);
        }

        return paidAmount - totalFare;
    }
}
