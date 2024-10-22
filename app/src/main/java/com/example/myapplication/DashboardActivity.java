package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private LinearLayout userDashboard;
    private LinearLayout lenderDashboard;
    private LinearLayout adminDashboard;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize the layouts
        userDashboard = findViewById(R.id.user_dashboard);
        lenderDashboard = findViewById(R.id.lender_dashboard);
        adminDashboard = findViewById(R.id.admin_dashboard);

        // Get the user role from the intent
        String userRole = getIntent().getStringExtra("userRole");

        // Show the corresponding dashboard based on user role
        switch ("admin") {
            case "user":
                userDashboard.setVisibility(View.VISIBLE);
                setupUserDashboard();
                break;
            case "lender":
                lenderDashboard.setVisibility(View.VISIBLE);
                setupLenderDashboard();
                break;
            case "admin":
                adminDashboard.setVisibility(View.VISIBLE);
                setupAdminDashboard();
                break;
            default:
                // Optionally handle unknown roles
                break;
        }
    }

    private void setupUserDashboard() {
        searchBar = findViewById(R.id.searchBar);
        Button searchButton = findViewById(R.id.searchButton);
        Button myRentalsButton = findViewById(R.id.myRentalsButton);
        Button requestLenderButton = findViewById(R.id.requestLenderButton);

        searchButton.setOnClickListener(v -> {
            String query = searchBar.getText().toString();
            // Implement search functionality
        });

        myRentalsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MyRentalsActivity.class));
        });

        requestLenderButton.setOnClickListener(v -> {
            // Implement request to become a lender functionality
        });
    }

    private void setupLenderDashboard() {
        Button addItemButton = findViewById(R.id.addItemButton);
        Button myListingsButton = findViewById(R.id.myListingsButton);
        Button viewRequestsButton = findViewById(R.id.viewRequestsButton);

        addItemButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AddItemActivity.class));
        });

        myListingsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MyListingsActivity.class));
        });

        viewRequestsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewRequestsActivity.class));
        });
    }

    private void setupAdminDashboard() {
        Button approveLendersButton = findViewById(R.id.approveLendersButton);
        Button manageUsersButton = findViewById(R.id.manageUsersButton);
        Button manageItemsButton = findViewById(R.id.manageItemsButton);

        approveLendersButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ApproveLendersActivity.class));
        });

        manageUsersButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ManageUsersActivity.class));
        });

        manageItemsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ManageItemsActivity.class));
        });
    }
}
