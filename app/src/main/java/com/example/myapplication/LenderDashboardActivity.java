package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LenderDashboardActivity extends AppCompatActivity {

    private RecyclerView lenderListingsRecyclerView;
    private ListingsAdapter listingAdapter;
    private List<Listing> lenderListings;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private Button btnAddNewListing, btnViewBookedListings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lender_dashboard);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        lenderListings = new ArrayList<>();

        lenderListingsRecyclerView = findViewById(R.id.lenderListingsRecyclerView);
        btnAddNewListing = findViewById(R.id.btnAddNewListing);
        btnViewBookedListings = findViewById(R.id.btnViewBookedListings);

        lenderListingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        listingAdapter = new ListingsAdapter(this, lenderListings, new ListingsAdapter.OnItemActionListener() {
            public void onItemClick(Listing listing) {
                // Handle item click

            }

            @Override
            public void onUpdateClick(Listing listing) {
                openUpdateListingActivity(listing);
            }

            @Override
            public void onDeleteClick(Listing listing) {
                deleteListing(listing);
            }
            @Override
            public void onBookNowClick(Listing listing) {
                // Empty implementation since "Book Now" is not needed
            }
        }, true);

        lenderListingsRecyclerView.setAdapter(listingAdapter);

        fetchLenderListings();

        btnAddNewListing.setOnClickListener(v -> openAddListingActivity());
        btnViewBookedListings.setOnClickListener(v -> openBookedListingsActivity());
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchLenderListings(); // Refresh listings when returning to this activity
    }

    private void fetchLenderListings() {
        String lenderId = auth.getCurrentUser().getUid();
        firestore.collection("listings")
                .whereEqualTo("lenderId", lenderId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        lenderListings.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Listing listing = document.toObject(Listing.class);
                            listing.setId(document.getId());
                            lenderListings.add(listing);
                        }
                        listingAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(LenderDashboardActivity.this, "Error fetching listings", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openAddListingActivity() {
        Intent intent = new Intent(LenderDashboardActivity.this, AddListingActivity.class);
        startActivity(intent);
    }

    private void openUpdateListingActivity(Listing listing) {
        Intent intent = new Intent(LenderDashboardActivity.this, UpdateListingActivity.class);
        intent.putExtra("listing", listing); // Pass the listing data to the update activity
        startActivity(intent);
    }

    private void deleteListing(Listing listing) {
        firestore.collection("listings").document(listing.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    lenderListings.remove(listing);
                    listingAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Listing deleted successfully.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete listing.", Toast.LENGTH_SHORT).show());
    }

    private void openBookedListingsActivity() {
        Intent intent = new Intent(LenderDashboardActivity.this, BookedListingsActivity.class);
        startActivity(intent);
    }
}

