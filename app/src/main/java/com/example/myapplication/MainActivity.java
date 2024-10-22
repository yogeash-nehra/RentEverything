package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListingsAdapter listingAdapter;
    private List<Listing> listingList;
    private FirebaseFirestore firestore;
    private Button logoutBtn, dashboardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        recyclerView = findViewById(R.id.recycler_view);
        logoutBtn = findViewById(R.id.logout_btn);
        dashboardBtn = findViewById(R.id.dashboardBtn);

        firestore = FirebaseFirestore.getInstance();
        listingList = new ArrayList<>();

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listingAdapter = new ListingsAdapter(this, listingList);
        recyclerView.setAdapter(listingAdapter);

        // Check if it's the first launch
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean("isFirstLaunch", true);

        if (isFirstLaunch) {
            addDefaultListings();
            prefs.edit().putBoolean("isFirstLaunch", false).apply(); // Update the flag
        }

        // Fetch listings from Firestore
        fetchListings();

        // Logout button functionality
        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });

        // Dashboard button functionality
        dashboardBtn.setOnClickListener(v -> {
            String userRole = getUserRole(); // Replace with actual logic to get user role

            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            intent.putExtra("userRole", userRole); // Pass user role to DashboardActivity
            startActivity(intent);
        });
    }

    private String getUserRole() {
        // Placeholder for actual logic to determine user role
        // This should be replaced with actual logic to retrieve the user's role from Firestore or SharedPreferences
        return "user"; // Example role
    }

    private void fetchListings() {
        firestore.collection("listings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Listing listing = document.toObject(Listing.class);
                        listingList.add(listing);
                    }
                    listingAdapter.notifyDataSetChanged(); // Notify adapter of data changes
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                    Toast.makeText(MainActivity.this, "Error fetching listings", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addDefaultListings() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Example default listings with all required parameters
        Listing listing1 = new Listing("Hammer", "A sturdy hammer for all your needs.", 10, "url_to_image", "Tools", "dummyLenderId");
        Listing listing2 = new Listing("Drill", "Powerful drill for various tasks.", 25, "url_to_image", "Tools", "dummyLenderId");

        firestore.collection("listings").add(listing1)
                .addOnSuccessListener(documentReference -> Log.d("TAG", "Listing added: " + documentReference.getId()))
                .addOnFailureListener(e -> {
                    Log.w("TAG", "Error adding listing", e);
                    Toast.makeText(MainActivity.this, "Error adding Hammer listing", Toast.LENGTH_SHORT).show();
                });

        firestore.collection("listings").add(listing2)
                .addOnSuccessListener(documentReference -> Log.d("TAG", "Listing added: " + documentReference.getId()))
                .addOnFailureListener(e -> {
                    Log.w("TAG", "Error adding listing", e);
                    Toast.makeText(MainActivity.this, "Error adding Drill listing", Toast.LENGTH_SHORT).show();
                });
    }

}
