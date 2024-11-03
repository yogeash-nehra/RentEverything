package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button manageUsersButton, manageListingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        manageUsersButton = findViewById(R.id.manageUsersButton);
        manageListingsButton = findViewById(R.id.manageListingsButton);

        manageUsersButton.setOnClickListener(v -> startActivity(new Intent(this, ManageUsersActivity.class)));

        manageListingsButton.setOnClickListener(v -> startActivity(new Intent(this, ManageItemsActivity.class)));
    }

    public void approveRequest(String userId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("lenderRequests").document(userId)
                .update("status", "approved")
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Request approved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to approve request", Toast.LENGTH_SHORT).show());
    }

    public void rejectRequest(String userId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("lenderRequests").document(userId)
                .update("status", "rejected")
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Request rejected", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to reject request", Toast.LENGTH_SHORT).show());
    }
}
