package com.example.emergencycontactapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.telephony.SmsManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;

public class MainActivity extends AppCompatActivity {

    private Button btnSos, btnPolice, btnAmbulance, btnDoctor, btnAddFamilyContact;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int PERMISSIONS_REQUEST_CODE = 123;
    private Button btnFireForce, btnWomenHelpline, btnChildHelpline, btnCyberCrime;
    private RecyclerView recyclerViewFamily;
    private ContactAdapter contactAdapter;
    private List<Contact> familyContacts;

    private DbHelper dbHelper;

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

        // Initialize DbHelper
        dbHelper = new DbHelper(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize UI components
        btnSos = findViewById(R.id.btnSos);
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        btnSos.startAnimation(pulse);
        
        btnPolice = findViewById(R.id.btnPolice);
        btnAmbulance = findViewById(R.id.btnAmbulance);
        btnDoctor = findViewById(R.id.btnDoctor);
        btnFireForce = findViewById(R.id.btnFireForce);
        btnWomenHelpline = findViewById(R.id.btnWomenHelpline);
        btnChildHelpline = findViewById(R.id.btnChildHelpline);
        btnCyberCrime = findViewById(R.id.btnCyberCrime);
        btnAddFamilyContact = findViewById(R.id.btnAddFamilyContact);
        recyclerViewFamily = findViewById(R.id.recyclerViewFamily);

        // Setup SOS button
        btnSos.setOnClickListener(v -> checkPermissionsAndSendSOS());

        // Setup emergency buttons
        btnPolice.setOnClickListener(v -> showEmergencyServiceDialog("Police", "Emergency service for crime, security, and immediate assistance.", "112"));
        btnAmbulance.setOnClickListener(v -> showEmergencyServiceDialog("Ambulance", "Emergency medical service for health emergencies and hospital transport.", "108"));
        btnDoctor.setOnClickListener(v -> showEmergencyServiceDialog("Doctor", "Contact your primary doctor for non-critical medical consultations.", "9876543210"));
        btnFireForce.setOnClickListener(v -> showEmergencyServiceDialog("Fire Force", "Emergency service for fire and rescue operations.", "101"));
        btnWomenHelpline.setOnClickListener(v -> showEmergencyServiceDialog("Women Helpline", "Emergency service and support for women in distress.", "1091"));
        btnChildHelpline.setOnClickListener(v -> showEmergencyServiceDialog("Child Helpline", "Emergency service for children in need of care and protection.", "1098"));
        btnCyberCrime.setOnClickListener(v -> showEmergencyServiceDialog("Cyber Crime Helpline", "National cyber crime reporting and support portal.", "1930"));

        // Setup family contacts list
        familyContacts = dbHelper.getAllContacts();
        contactAdapter = new ContactAdapter(familyContacts, new ContactAdapter.OnContactClickListener() {
            @Override
            public void onContactClick(Contact contact) {
                dialPhoneNumber(contact.getPhone());
            }

            @Override
            public void onDeleteClick(Contact contact, int position) {
                boolean isDeleted = dbHelper.deleteContact(contact.getId());
                if (isDeleted) {
                    familyContacts.remove(position);
                    contactAdapter.notifyItemRemoved(position);
                }
            }

            @Override
            public void onEditClick(Contact contact, int position) {
                showEditContactDialog(contact, position);
            }
        });
        recyclerViewFamily.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFamily.setAdapter(contactAdapter);

        // Setup add contact button
        btnAddFamilyContact.setOnClickListener(v -> showAddContactDialog());
    }

    private void showEmergencyServiceDialog(String title, String description, String phoneNumber) {
        Intent intent = new Intent(MainActivity.this, ServiceDetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);
    }

    private void showAddContactDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null);
        bottomSheetDialog.setContentView(dialogView);

        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etPhone = dialogView.findViewById(R.id.etPhone);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            if (!name.isEmpty() && !phone.isEmpty()) {
                boolean isInserted = dbHelper.insertContact(name, phone);
                if (isInserted) {
                    familyContacts.clear();
                    familyContacts.addAll(dbHelper.getAllContacts());
                    contactAdapter.notifyDataSetChanged();
                    bottomSheetDialog.dismiss();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void showEditContactDialog(Contact contact, int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null);
        bottomSheetDialog.setContentView(dialogView);

        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etPhone = dialogView.findViewById(R.id.etPhone);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        btnSave.setText("Update");

        etName.setText(contact.getName());
        etPhone.setText(contact.getPhone());

        btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            if (!name.isEmpty() && !phone.isEmpty()) {
                boolean isUpdated = dbHelper.updateContact(contact.getId(), name, phone);
                if (isUpdated) {
                    contact.setName(name);
                    contact.setPhone(phone);
                    contactAdapter.notifyItemChanged(position);
                    bottomSheetDialog.dismiss();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void checkPermissionsAndSendSOS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
        } else {
            sendSOS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSOS();
            } else {
                Toast.makeText(this, "Permissions required for SOS feature.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendSOS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken())
                .addOnSuccessListener(this, location -> {
                    String message = "Emergency! I need help.";
                    if (location != null) {
                        message += " My current location is: https://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
                    } else {
                        message += " (Location unavailable)";
                    }

                    if (familyContacts == null || familyContacts.isEmpty()) {
                        Toast.makeText(MainActivity.this, "No family contacts to send SOS!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SmsManager smsManager = SmsManager.getDefault();
                    for (Contact contact : familyContacts) {
                        try {
                            smsManager.sendTextMessage(contact.getPhone(), null, message, null, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(MainActivity.this, "SOS Sent to " + familyContacts.size() + " contacts!", Toast.LENGTH_LONG).show();
                });
    }
}