package com.example.myapplication;

public class Rental {
    private String id; // To hold the Firestore document ID
    private String itemId; // ID of the item being rented
    private String userId; // User who rented the item
    private String rentalDetails; // Any other details about the rental

    // Default constructor required for calls to DataSnapshot.getValue(Rental.class)
    public Rental() {
    }

    public Rental(String itemId, String userId, String rentalDetails) {
        this.itemId = itemId;
        this.userId = userId;
        this.rentalDetails = rentalDetails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRentalDetails() {
        return rentalDetails;
    }

    public void setRentalDetails(String rentalDetails) {
        this.rentalDetails = rentalDetails;
    }
}
