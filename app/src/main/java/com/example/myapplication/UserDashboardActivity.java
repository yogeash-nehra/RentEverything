package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserDashboardActivity extends AppCompatActivity {

    private Button viewBookingsButton, requestLenderButton;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        viewBookingsButton = findViewById(R.id.viewBookingsButton);
        requestLenderButton = findViewById(R.id.requestLenderButton);

        viewBookingsButton.setOnClickListener(v -> startActivity(new Intent(this, MyRentalsActivity.class)));

        requestLenderButton.setOnClickListener(v -> requestToBecomeLender());
    }

    private void requestToBecomeLender() {
        String userId = auth.getCurrentUser().getUid();

        Map<String, Object> request = new HashMap<>();
        request.put("userId", userId);
        request.put("status", "pending");

        firestore.collection("lenderRequests").document(userId)
                .set(request)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Lender request submitted successfully.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to submit request.", Toast.LENGTH_SHORT).show());
    }
}
