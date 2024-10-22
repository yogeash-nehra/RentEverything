package com.example.myapplication;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
        listingsAdapter = new ListingsAdapter(this, listingList);

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
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Listing listing = document.toObject(Listing.class);
                        listingList.add(listing);
                    }
                    listingsAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
                } else {
                    // Handle error
                }
            }
        });
    }
}
