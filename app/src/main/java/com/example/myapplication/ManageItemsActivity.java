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

public class ManageItemsActivity extends AppCompatActivity {

    private RecyclerView itemsRecyclerView;
    private ItemsAdapter itemsAdapter; // You will create this adapter
    private List<Listing> itemList; // Assuming Listing class is already defined

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_items);

        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        itemList = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(this, itemList);

        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsRecyclerView.setAdapter(itemsAdapter);

        fetchItems();
    }

    private void fetchItems() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("listings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Listing item = document.toObject(Listing.class);
                        itemList.add(item);
                    }
                    itemsAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
                } else {
                    // Handle error
                }
            }
        });
    }
}
