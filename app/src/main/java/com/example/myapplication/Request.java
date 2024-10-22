package com.example.myapplication;

public class Request {
    private String id; // To hold the Firestore document ID
    private String userId; // User who made the request
    private String itemId; // ID of the item being requested
    private String details; // Any other details about the request

    // Default constructor required for calls to DataSnapshot.getValue(Request.class)
    public Request() {
    }

    public Request(String userId, String itemId, String details) {
        this.userId = userId;
        this.itemId = itemId;
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
