package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ItemDetailsActivity extends AppCompatActivity {

    private ImageView itemImageView;
    private TextView itemTitle, itemCategory, itemPrice, itemDescription;
    private Button bookNowButton;

    private Listing listing;
    private String userId;

    private Date startDate;
    private Date endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Initialize UI components
        itemImageView = findViewById(R.id.itemImageView);
        itemTitle = findViewById(R.id.itemTitle);
        itemCategory = findViewById(R.id.itemCategory);
        itemPrice = findViewById(R.id.itemPrice);
        itemDescription = findViewById(R.id.itemDescription);
        bookNowButton = findViewById(R.id.bookNowButton);

        // Get the Intent that started this activity and extract the listing data
        Intent intent = getIntent();
        listing = (Listing) intent.getSerializableExtra("listing");

        // Get the current user ID
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Set the views with the listing data
        if (listing != null) {
            itemTitle.setText(listing.getTitle());
            itemCategory.setText("Category: " + listing.getCategory());
            itemPrice.setText("Price per day: $" + listing.getPrice());
            itemDescription.setText(listing.getDescription());

            // Load image into itemImageView using Glide
            Glide.with(this)
                    .load(listing.getImageUrl())
                    .placeholder(R.drawable.background)
                    .into(itemImageView);
        }

        // Set OnClickListener for Book Now button
        bookNowButton.setOnClickListener(v -> showDatePicker());
    }

    // Show a date picker to select the start date
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog startDatePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            startDate = calendar.getTime();

            // Show end date picker
            showEndDatePicker();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        startDatePicker.setTitle("Select Start Date");
        startDatePicker.show();
    }

    private void showEndDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog endDatePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            endDate = calendar.getTime();

            if (endDate.before(startDate)) {
                Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
            } else {
                createBooking();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        endDatePicker.setTitle("Select End Date");
        endDatePicker.show();
    }

    private void createBooking() {
        // Generate a unique booking ID
        String bookingId = UUID.randomUUID().toString();

        // Calculate total price for booking based on the number of days
        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        booking.setListingId(listing.getId());
        booking.setUserId(userId);
        booking.setLenderId(listing.getLenderId());
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setStatus("pending");

        // Calculate the total price based on the number of days
        booking.calculateTotalPrice(listing.getPrice());

        // Store booking data in Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("bookings")
                .document(bookingId)
                .set(booking)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Booking created successfully!", Toast.LENGTH_SHORT).show();
                    updateListingAvailability();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to create booking", Toast.LENGTH_SHORT).show());
    }

    // Update listing's availability status in Firestore
    private void updateListingAvailability() {
        FirebaseFirestore.getInstance().collection("listings")
                .document(listing.getId())
                .update("available", false, "status", "unavailable")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Listing updated to unavailable", Toast.LENGTH_SHORT).show();
                    finish(); // Close the ItemDetailsActivity
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update listing availability", Toast.LENGTH_SHORT).show());
    }
}
