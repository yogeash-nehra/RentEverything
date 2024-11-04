package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListingsAdapter listingAdapter;
    private List<Listing> listingList;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private Button logoutBtn, dashboardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("FirebaseInit", "Firebase initialized: " + (FirebaseApp.getInstance() != null)); // Verify Firebase initialization

        // Initialize Firebase components
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        listingList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listingAdapter = new ListingsAdapter(this, listingList, new ListingsAdapter.OnItemActionListener() {
            @Override
            public void onItemClick(Listing listing) {
                Intent intent = new Intent(MainActivity.this, ItemDetailsActivity.class);
                intent.putExtra("listing", listing);
                startActivity(intent);
            }

            @Override
            public void onUpdateClick(Listing item) {
                // Leave empty or implement if needed
            }

            @Override
            public void onDeleteClick(Listing item) {
                // Leave empty or implement if needed
            }

            @Override
            public void onBookNowClick(Listing listing) {
                Intent intent = new Intent(MainActivity.this, BookingActivity.class);
                intent.putExtra("listingId", listing.getId());
                intent.putExtra("itemName", listing.getTitle());
                intent.putExtra("pricePerDay", listing.getPrice());
                intent.putExtra("lenderId", listing.getLenderId());
                startActivity(intent);
            }
        }, false);

        recyclerView.setAdapter(listingAdapter);

        logoutBtn = findViewById(R.id.logout_btn);
        dashboardBtn = findViewById(R.id.dashboardBtn);

        // Fetch listings from Firestore
        fetchListings();
    onResume();

        // Logout button
        logoutBtn.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });

        // Dashboard button
        dashboardBtn.setOnClickListener(v -> getUserRoleFromFirebase());
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchListings();
    }

    private void getUserRoleFromFirebase() {
        String userId = auth.getCurrentUser().getUid();

        firestore.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String role = document.getString("role");
                            navigateToDashboard(role != null ? role : "user");
                        } else {
                            navigateToDashboard("user");
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Error fetching role. Defaulting to User role.", Toast.LENGTH_SHORT).show();
                        navigateToDashboard("user");
                    }
                });
    }

    private void navigateToDashboard(String userRole) {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        intent.putExtra("userRole", userRole);
        startActivity(intent);
    }

    private void fetchListings() {
        firestore.collection("listings")
                .whereEqualTo("available", true) // Filter to only fetch available listings
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listingList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Listing listing = document.toObject(Listing.class);
                            listing.setId(document.getId());
                            listingList.add(listing);
                        }
                        listingAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "Error fetching available listings", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
