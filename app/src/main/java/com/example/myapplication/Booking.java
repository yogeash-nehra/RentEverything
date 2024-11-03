package com.example.myapplication;

import java.io.Serializable;
import java.util.Date;

public class Booking implements Serializable {
    private String bookingId; // Unique ID for each booking
    private String listingId; // The ID of the listing being booked
    private String userId; // The ID of the user who made the booking
    private String lenderId; // The ID of the lender of the item
    private Date startDate; // The start date of the booking
    private Date endDate; // The end date of the booking
    private double totalPrice; // Total price for the booking duration
    private String status; // Booking status: "confirmed", "pending", "cancelled"

    // Default constructor required for Firestore
    public Booking() {
    }

    // Constructor with all fields
    public Booking(String bookingId, String listingId, String userId, String lenderId, Date startDate, Date endDate, double totalPrice, String status) {
        this.bookingId = bookingId;
        this.listingId = listingId;
        this.userId = userId;
        this.lenderId = lenderId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Getters and setters
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLenderId() {
        return lenderId;
    }

    public void setLenderId(String lenderId) {
        this.lenderId = lenderId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Method to calculate the total price based on the number of days
    public void calculateTotalPrice(double pricePerDay) {
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diffInDays = (diffInMillies / (1000 * 60 * 60 * 24)) + 1; // Include both start and end dates
        this.totalPrice = diffInDays * pricePerDay;
    }
}
