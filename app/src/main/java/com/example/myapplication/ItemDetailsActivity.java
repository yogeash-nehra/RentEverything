package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailsActivity extends AppCompatActivity {

    private ImageView itemImageView;
    private TextView itemTitle, itemCategory, itemPrice, itemLender;
    private Button bookNowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        itemImageView = findViewById(R.id.itemImageView);
        itemTitle = findViewById(R.id.itemTitle);
        itemCategory = findViewById(R.id.itemCategory);
        itemPrice = findViewById(R.id.itemPrice);
        itemLender = findViewById(R.id.itemLender);
        bookNowButton = findViewById(R.id.bookNowButton);

        // Get the Intent that started this activity and extract the data
        Intent intent = getIntent();
        Listing listing = (Listing) intent.getSerializableExtra("listing");

        // Set the views with the listing data
        if (listing != null) {
            itemTitle.setText(listing.getTitle());
            itemCategory.setText(listing.getCategory());  // Add a category field to your Listing class if needed
            itemPrice.setText("Price: " + listing.getPrice());
            itemLender.setText("Lender: " + listing.getLenderId()); // Add a lender field to your Listing class if needed
            // Load image into itemImageView using a library like Glide or Picasso
            // Glide.with(this).load(listing.getImageUrl()).into(itemImageView);
        }

        bookNowButton.setOnClickListener(v -> {
            // Handle booking functionality here
        });
    }
}
