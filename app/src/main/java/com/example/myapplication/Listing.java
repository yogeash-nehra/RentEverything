package com.example.myapplication;

import java.io.Serializable;

public class Listing implements Serializable {
    private String title;
    private String description;
    private double price; // This represents the price per day
    private String imageUrl;
    private String category; // Field for the item category
    private String lenderId; // Field for the lender's ID

    // Default constructor required for Firestore
    public Listing() {
    }

    // Constructor with all required parameters
    public Listing(String title, String description, double price, String imageUrl, String category, String lenderId) {
        this.title = title;
        this.description = description;
        this.price = price; // Correctly assigning to price
        this.imageUrl = imageUrl;
        this.category = category;
        this.lenderId = lenderId; // Use lenderId for clarity
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price; // Returns price as double
    }

    public void setPrice(double price) {
        this.price = price; // Allows setting price as double
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLenderId() {
        return lenderId; // Correctly named getter
    }

    public void setLenderId(String lenderId) {
        this.lenderId = lenderId; // Correctly named setter
    }
}
