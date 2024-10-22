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

public class ViewRequestsActivity extends AppCompatActivity {

    private RecyclerView requestsRecyclerView;
    private RequestsAdapter requestsAdapter; // You will create this adapter
    private List<Request> requestList; // Create a Request class as needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        requestsRecyclerView = findViewById(R.id.requestsRecyclerView);
        requestList = new ArrayList<>();
        requestsAdapter = new RequestsAdapter(this, requestList);

        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestsRecyclerView.setAdapter(requestsAdapter);

        fetchRequests();
    }

    private void fetchRequests() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("requests").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Request request = document.toObject(Request.class); // Assume Request is a model class
                        requestList.add(request);
                    }
                    requestsAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
                } else {
                    // Handle error
                }
            }
        });
    }
}
