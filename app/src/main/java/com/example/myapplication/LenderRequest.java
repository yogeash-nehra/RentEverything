package com.example.myapplication;

public class LenderRequest {
    private String id; // To hold the Firestore document ID
    private String userId; // User who wants to be a lender
    private String details; // Any other details about the request

    // Default constructor required for calls to DataSnapshot.getValue(LenderRequest.class)
    public LenderRequest() {
    }

    public LenderRequest(String userId, String details) {
        this.userId = userId;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
