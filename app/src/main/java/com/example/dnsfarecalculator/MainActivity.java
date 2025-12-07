package com.example.dnsfarecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button bulakanGuiguinto,
            bulakanBalagtas,
            bulakanMalolos,
            exit;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bulakanGuiguinto = findViewById(R.id.bulakanGuiguinto);
        bulakanBalagtas = findViewById(R.id.bulakanBalagtas);
        bulakanMalolos = findViewById(R.id.bulakanMalolos);
        exit = findViewById(R.id.exit);

        // select from routes
        bulakanGuiguinto.setOnClickListener(v -> {
            intent = new Intent(this, BulakanGuiguinto.class);
            startActivity(intent);
        });
        bulakanBalagtas.setOnClickListener(v -> {
            intent = new Intent(this, BulakanBalagtas.class);
            startActivity(intent);
        });
        bulakanMalolos.setOnClickListener(v -> {
            intent = new Intent(this, BulakanMalolos.class);
            startActivity(intent);
        });

        // exit app
        exit.setOnClickListener(v -> finishAffinity());

    }
}