package com.example.emergencycontactapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ServiceDetailActivity extends AppCompatActivity {

    private TextView tvServiceTitle, tvServiceDescription, tvServiceNumber;
    private Button btnCallService, btnFindNearby, btnSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        tvServiceTitle = findViewById(R.id.tvServiceTitle);
        tvServiceDescription = findViewById(R.id.tvServiceDescription);
        tvServiceNumber = findViewById(R.id.tvServiceNumber);
        btnCallService = findViewById(R.id.btnCallService);

        // Get data from Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String phoneNumber = intent.getStringExtra("phoneNumber");

        if (title != null) tvServiceTitle.setText(title);
        if (description != null) tvServiceDescription.setText(description);
        if (phoneNumber != null) tvServiceNumber.setText("Phone: " + phoneNumber);

        btnCallService.setOnClickListener(v -> {
            if (phoneNumber != null) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(dialIntent);
            }
        });

        btnSendMessage = findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(v -> {
            if (phoneNumber != null) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.setData(Uri.parse("smsto:" + phoneNumber));
                smsIntent.putExtra("sms_body", "Emergency! I need immediate assistance at my location.");
                startActivity(smsIntent);
            }
        });

        btnFindNearby = findViewById(R.id.btnFindNearby);
        btnFindNearby.setOnClickListener(v -> {
            if (title != null) {
                // Determine search query based on title
                String query = title + " near me";
                if (title.contains("Police")) query = "Police Stations near me";
                else if (title.contains("Ambulance") || title.contains("Doctor")) query = "Hospitals near me";
                else if (title.contains("Fire")) query = "Fire Stations near me";

                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(query));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    // Fallback if Google Maps is not installed
                    Intent genericMapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    startActivity(genericMapIntent);
                }
            }
        });
    }
}
