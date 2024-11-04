package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserDashboardActivity extends AppCompatActivity {

    private Button viewBookingsButton, requestLenderButton;
    private EditText searchBar;

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
        searchBar = findViewById(R.id.searchBar);

    }

    private void performSearch() {
        String searchText = searchBar.getText().toString().trim();

        if (TextUtils.isEmpty(searchText)) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firestore query to search listings based on the input text
        firestore.collection("listings")
                .whereEqualTo("itemName", searchText)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Process the search results here
                            Toast.makeText(this, "Found: " + document.getString("itemName"), Toast.LENGTH_SHORT).show();
                            // Here, you can launch an activity or update UI with the search results
                        }
                    } else {
                        Toast.makeText(this, "No results found for: " + searchText, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Search failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
