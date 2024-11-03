package com.example.myapplication;

import java.util.List;

public class User {
    private String id; // To hold the Firestore document ID
    private String name;
    private String email;
    private String role; // User, Lender, Admin
    private List<Booking> bookings; // List to track the user's booked items

    // Default constructor required for Firestore
    public User() {
    }

    // Constructor for creating a user with name, email, and default role as "User"
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.role = "User"; // Default role set to User
    }

    // Getters and setters for id, name, email, role, and bookings
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    // Method to add a booking to the user's list
    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }
}
