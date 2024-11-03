package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyListingsActivity extends AppCompatActivity {

    private RecyclerView listingsRecyclerView;
    private ListingsAdapter listingsAdapter;
    private List<Listing> listingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        listingsRecyclerView = findViewById(R.id.listingsRecyclerView);
        listingList = new ArrayList<>();

        // Initialize adapter with click listener for both item click and book now click
        listingsAdapter = new ListingsAdapter(this, listingList, new ListingsAdapter.OnItemActionListener() {
            @Override
            public void onItemClick(Listing listing) {
                // Navigate to ItemDetailsActivity with the selected listing details
                Intent intent = new Intent(MyListingsActivity.this, ItemDetailsActivity.class);
                intent.putExtra("listing", listing); // Pass the listing as a serializable extra
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
                // Navigate to BookingActivity with the selected listing details for booking
                Intent intent = new Intent(MyListingsActivity.this, BookingActivity.class);
                intent.putExtra("listing", listing); // Pass the listing as a serializable extra
                startActivity(intent);
            }
        },false);

        listingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listingsRecyclerView.setAdapter(listingsAdapter);

        fetchListings();
    }

    private void fetchListings() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("listings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listingList.clear(); // Clear the list before adding new items
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Listing listing = document.toObject(Listing.class);
                        listingList.add(listing);
                    }
                    listingsAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
                } else {
                    Toast.makeText(MyListingsActivity.this, "Error fetching listings", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
