package com.example.myapplication;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private ItemsAdapter itemsAdapter;
    private List<Listing> itemList;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_items);

        firestore = FirebaseFirestore.getInstance();
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        itemList = new ArrayList<>();

        // Initialize adapter with update and delete actions
        itemsAdapter = new ItemsAdapter(this, itemList, new ItemsAdapter.OnItemActionListener() {
            @Override
            public void onUpdateClick(Listing item) {
                updateItem(item);
            }

            @Override
            public void onDeleteClick(Listing item) {
                deleteItem(item);
            }
        });

        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsRecyclerView.setAdapter(itemsAdapter);

        fetchItems();
    }

    private void fetchItems() {
        firestore.collection("listings").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                itemList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Listing item = document.toObject(Listing.class);
                    item.setId(document.getId()); // Ensure the item has its Firestore document ID
                    itemList.add(item);
                }
                itemsAdapter.notifyDataSetChanged();
            } else {
                // Handle error
            }
        });
    }

    private void updateItem(Listing item) {
        // Implement the logic to update the listing item here.
        // You might open a new activity or dialog to edit the item's details
    }

    private void deleteItem(Listing item) {
        firestore.collection("listings").document(item.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    itemList.remove(item);
                    itemsAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                });
    }
}
