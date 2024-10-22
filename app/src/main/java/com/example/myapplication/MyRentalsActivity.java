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

public class MyRentalsActivity extends AppCompatActivity {

    private RecyclerView rentalsRecyclerView;
    private RentalsAdapter rentalsAdapter;
    private List<Rental> rentalList; // Create a Rental class as needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rentals);

        rentalsRecyclerView = findViewById(R.id.rentalsRecyclerView);
        rentalList = new ArrayList<>();
        rentalsAdapter = new RentalsAdapter(this, rentalList);

        rentalsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rentalsRecyclerView.setAdapter(rentalsAdapter);

        fetchRentals();
    }

    private void fetchRentals() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("rentals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Rental rental = document.toObject(Rental.class); // Assume Rental is a model class
                        rentalList.add(rental);
                    }
                    rentalsAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
                } else {
                    // Handle error
                }
            }
        });
    }
}
