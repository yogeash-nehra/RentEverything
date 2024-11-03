package com.example.myapplication;

public class LenderRequest {
    private String id; // To hold the Firestore document ID
    private String userId; // User who wants to be a lender
    private String details; // Any other details about the request
    private String status; // Status of the request, e.g., "pending", "approved", "rejected"

    // Default constructor required for Firestore
    public LenderRequest() {
    }

    // Constructor with all parameters
    public LenderRequest(String userId, String details, String status) {
        this.userId = userId;
        this.details = details;
        this.status = status; // Initialize with the status, e.g., "pending"
    }

    // Getters and Setters
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
