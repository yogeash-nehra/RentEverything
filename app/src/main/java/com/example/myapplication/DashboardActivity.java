package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        String userRole = getIntent().getStringExtra("userRole");
        if (userRole == null) {
            userRole = "admin";
        }

        switch (userRole) {
            case "user":
                startActivity(new Intent(this, UserDashboardActivity.class));
                finish();
                break;
            case "lender":
                startActivity(new Intent(this, LenderDashboardActivity.class));
                finish();
                break;
            case "admin":
                startActivity(new Intent(this, AdminDashboardActivity.class));
                finish();
                break;
            default:
                Toast.makeText(this, "Invalid user role", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
