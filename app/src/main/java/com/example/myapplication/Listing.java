package com.example.myapplication;

import java.io.Serializable;

public class Listing implements Serializable {
    private String id;
    private String title;
    private String description;
    private double price;
    private String imageUrl;
    private String category;
    private String lenderId;
    private String status;
    private String renterEmail;
    private String bookedDate;
    private Boolean available;

    public Listing() {
        // Required for Firestore
    }

    public Listing(String id, String title, String description, double price, String imageUrl, String category, String lenderId, String status, Boolean available) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.lenderId = lenderId;
        this.status = status;
        this.available = available;
    }

    // Getters and Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLenderId() { return lenderId; }
    public void setLenderId(String lenderId) { this.lenderId = lenderId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRenterEmail() { return renterEmail; }
    public void setRenterEmail(String renterEmail) { this.renterEmail = renterEmail; }

    public String getBookedDate() { return bookedDate; }
    public void setBookedDate(String bookedDate) { this.bookedDate = bookedDate; }

    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
}
