package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    private EditText itemNameEditText;
    private EditText itemDescriptionEditText;
    private EditText itemPriceEditText;
    private EditText itemCategoryEditText; // Field for category
    private Button addItemButton;
    private RecyclerView itemsRecyclerView;
    private ItemsAdapter itemsAdapter; // Adapter for displaying added items
    private List<Listing> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        itemNameEditText = findViewById(R.id.itemNameEditText);
        itemDescriptionEditText = findViewById(R.id.itemDescriptionEditText);
        itemPriceEditText = findViewById(R.id.itemPriceEditText);
        itemCategoryEditText = findViewById(R.id.itemCategoryEditText); // Initialize category EditText
        addItemButton = findViewById(R.id.addItemButton);
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);

        itemList = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(this, itemList);

        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsRecyclerView.setAdapter(itemsAdapter);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToFirestore();
            }
        });
    }

    private void addItemToFirestore() {
        String name = itemNameEditText.getText().toString();
        String description = itemDescriptionEditText.getText().toString();
        String priceString = itemPriceEditText.getText().toString();
        String category = itemCategoryEditText.getText().toString(); // Get the category

        if (name.isEmpty() || description.isEmpty() || priceString.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceString); // Parse the price as a double
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        // Create a new Listing object with all required fields
        Listing newItem = new Listing(name, description, price, "url_to_image", category, "dummyLenderId");

        firestore.collection("listings").add(newItem)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                    itemList.add(newItem); // Add to the list for displaying
                    itemsAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
                    clearFields(); // Clear input fields
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error adding item", Toast.LENGTH_SHORT).show();
                });
    }

    private void clearFields() {
        itemNameEditText.setText("");
        itemDescriptionEditText.setText("");
        itemPriceEditText.setText("");
        itemCategoryEditText.setText(""); // Clear category field
    }
}
