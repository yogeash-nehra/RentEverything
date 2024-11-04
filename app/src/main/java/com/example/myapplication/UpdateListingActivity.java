package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class UpdateListingActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText itemTitleEditText, itemDescriptionEditText, itemCategoryEditText, itemPriceEditText;
    private ImageView itemImageView;
    private Button saveButton;
    private Uri newImageUri; // Uri to hold new image
    private Listing listing;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_listing);

        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("item_images"); // Folder for storing images

        itemTitleEditText = findViewById(R.id.itemTitleEditText);
        itemDescriptionEditText = findViewById(R.id.itemDescriptionEditText);
        itemCategoryEditText = findViewById(R.id.itemCategoryEditText);
        itemPriceEditText = findViewById(R.id.itemPriceEditText);
        itemImageView = findViewById(R.id.itemImageView);
        saveButton = findViewById(R.id.saveButton);

        // Retrieve listing data passed from the adapter
        listing = (Listing) getIntent().getSerializableExtra("listing");

        // Populate fields with existing listing data
        if (listing != null) {
            itemTitleEditText.setText(listing.getTitle());
            itemDescriptionEditText.setText(listing.getDescription());
            itemCategoryEditText.setText(listing.getCategory());
            itemPriceEditText.setText(String.valueOf(listing.getPrice()));
            Glide.with(this).load(listing.getImageUrl()).placeholder(R.drawable.background).into(itemImageView);
        }

        // Set onClick listener to open image picker
        itemImageView.setOnClickListener(v -> openImageChooser());

        // Save button to update the listing
        saveButton.setOnClickListener(v -> updateListing());
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            newImageUri = data.getData();
            itemImageView.setImageURI(newImageUri); // Preview the selected image
        }
    }

    private void updateListing() {
        String title = itemTitleEditText.getText().toString().trim();
        String description = itemDescriptionEditText.getText().toString().trim();
        String category = itemCategoryEditText.getText().toString().trim();
        String priceText = itemPriceEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(category) || TextUtils.isEmpty(priceText)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        listing.setTitle(title);
        listing.setDescription(description);
        listing.setCategory(category);
        listing.setPrice(price);

        if (newImageUri != null) {
            uploadImageAndSaveListing();
        } else {
            // No new image selected, update Firestore directly
            saveListingToFirestore(listing.getImageUrl());
        }
    }

    private void uploadImageAndSaveListing() {
        String uniqueId = UUID.randomUUID().toString();
        StorageReference fileReference = storageReference.child(uniqueId);

        fileReference.putFile(newImageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String newImageUrl = uri.toString();
                    saveListingToFirestore(newImageUrl); // Save listing with the new image URL
                }))
                .addOnFailureListener(e -> Toast.makeText(UpdateListingActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveListingToFirestore(String imageUrl) {
        listing.setImageUrl(imageUrl);

        firestore.collection("listings").document(listing.getId())
                .set(listing)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateListingActivity.this, "Listing updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(UpdateListingActivity.this, "Failed to update listing", Toast.LENGTH_SHORT).show());
    }
}
