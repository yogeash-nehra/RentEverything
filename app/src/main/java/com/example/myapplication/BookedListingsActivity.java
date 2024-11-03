package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookedListingsActivity extends AppCompatActivity {

    private RecyclerView bookedListingsRecyclerView;
    private ListingsAdapter listingAdapter;
    private List<Listing> bookedListings;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_listings);

        // Initialize Firebase and UI components
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        bookedListings = new ArrayList<>();

        bookedListingsRecyclerView = findViewById(R.id.bookedListingsRecyclerView);

        // Set up RecyclerView
        bookedListingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter with OnItemClickListener
        listingAdapter = new ListingsAdapter(this, bookedListings, new ListingsAdapter.OnItemActionListener() {
            @Override
            public void onItemClick(Listing listing) {
                // Handle item click here if necessary
                Toast.makeText(BookedListingsActivity.this, "Clicked: " + listing.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpdateClick(Listing item) {

            }

            @Override
            public void onDeleteClick(Listing item) {

            }

            @Override
            public void onBookNowClick(Listing listing) {
                // Empty implementation since "Book Now" is not needed in BookedListingsActivity
            }
        },false);

        bookedListingsRecyclerView.setAdapter(listingAdapter);

        // Fetch booked listings
        fetchBookedListings();
    }

    private void fetchBookedListings() {
        String userId = auth.getCurrentUser().getUid();

        // Query Firestore for listings booked by the current user
        firestore.collection("bookings")
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", "booked")  // Filter by "booked" status
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            bookedListings.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Assuming each booking contains listing details as a reference or embedded
                                Listing listing = document.toObject(Listing.class);
                                bookedListings.add(listing);
                            }
                            listingAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("BookedListingsActivity", "Error getting documents.", task.getException());
                            Toast.makeText(BookedListingsActivity.this, "Error fetching booked listings", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
