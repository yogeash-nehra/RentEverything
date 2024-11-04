package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddListingActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText itemTitleEditText, itemDescriptionEditText, itemCategoryEditText, itemPriceEditText;
    private ImageView itemImageView;
    private Button uploadImageButton, addListingButton;

    private Uri imageUri;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);

        // Initialize Firebase instances
        storageReference = FirebaseStorage.getInstance().getReference().child("item_images"); // Images will be saved in this new folder
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        itemTitleEditText = findViewById(R.id.itemTitleEditText);
        itemDescriptionEditText = findViewById(R.id.itemDescriptionEditText);
        itemCategoryEditText = findViewById(R.id.itemCategoryEditText);
        itemPriceEditText = findViewById(R.id.itemPriceEditText);
        itemImageView = findViewById(R.id.itemImageView);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        addListingButton = findViewById(R.id.addListingButton);

        // Set up upload button to open the image picker
        uploadImageButton.setOnClickListener(v -> openImageChooser());

        // Set up add listing button to save the listing
        addListingButton.setOnClickListener(v -> saveListing());
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
            imageUri = data.getData();
            itemImageView.setImageURI(imageUri); // Preview selected image
        }
    }

    private void saveListing() {
        String title = itemTitleEditText.getText().toString();
        String description = itemDescriptionEditText.getText().toString();
        String category = itemCategoryEditText.getText().toString();
        String priceText = itemPriceEditText.getText().toString();

        if (title.isEmpty() || description.isEmpty() || category.isEmpty() || priceText.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceText);
        uploadImageAndSaveListing(title, description, category, price);
    }

    private void uploadImageAndSaveListing(String title, String description, String category, double price) {
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique ID for the image file
        String imageId = UUID.randomUUID().toString();
        StorageReference fileReference = storageReference.child(imageId);

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    Log.d("ImageUpload", "Image URL: " + imageUrl);
                    saveListingToFirestore(title, description, category, price, imageUrl); // Save listing with image URL
                }))
                .addOnFailureListener(e -> {
                    Log.e("ImageUpload", "Upload failed", e);
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveListingToFirestore(String title, String description, String category, double price, String imageUrl) {
        String listingId = firestore.collection("listings").document().getId();
        String userId = auth.getCurrentUser().getUid();

        Map<String, Object> listingData = new HashMap<>();
        listingData.put("id", listingId);
        listingData.put("title", title);
        listingData.put("description", description);
        listingData.put("category", category);
        listingData.put("price", price);
        listingData.put("imageUrl", imageUrl);
        listingData.put("available", true);
        listingData.put("lenderId", userId);

        firestore.collection("listings").document(listingId)
                .set(listingData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Listing added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add listing", Toast.LENGTH_SHORT).show());
    }
}
