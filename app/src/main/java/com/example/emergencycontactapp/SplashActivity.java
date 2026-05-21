package com.example.emergencycontactapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Add a fade-in animation to the whole layout
        android.view.View rootView = findViewById(android.R.id.content);
        rootView.setAlpha(0f);
        rootView.animate().alpha(1f).setDuration(1500).start();

        Button btnGetStarted = findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close SplashActivity so user can't go back to it
        });
    }
}
