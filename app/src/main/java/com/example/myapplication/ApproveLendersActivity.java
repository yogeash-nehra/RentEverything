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

public class ApproveLendersActivity extends AppCompatActivity {

    private RecyclerView lendersRecyclerView;
    private LendersAdapter lendersAdapter; // You will create this adapter
    private List<LenderRequest> lenderRequestList; // Create a LenderRequest class as needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_lenders);

        lendersRecyclerView = findViewById(R.id.lendersRecyclerView);
        lenderRequestList = new ArrayList<>();
        lendersAdapter = new LendersAdapter(this, lenderRequestList);

        lendersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        lendersRecyclerView.setAdapter(lendersAdapter);

        fetchLenderRequests();
    }

    private void fetchLenderRequests() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("lenderRequests").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        LenderRequest request = document.toObject(LenderRequest.class); // Assume LenderRequest is a model class
                        lenderRequestList.add(request);
                    }
                    lendersAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
                } else {
                    // Handle error
                }
            }
        });
    }
}
