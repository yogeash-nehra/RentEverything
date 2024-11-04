package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    private TextView tvItemName, tvSelectedDate, tvTotalPrice;
    private EditText etLendingDays;
    private CheckBox cbAgreeTerms;
    private Button btnSelectDate, btnCompleteBooking;
    private ProgressBar progressBar;
    private String itemId, itemName, lenderId, userId, selectedDate;
    private double pricePerDay;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Get passed data from intent (item and user details)
        itemId = getIntent().getStringExtra("listingId");
        itemName = getIntent().getStringExtra("itemName");
        lenderId = getIntent().getStringExtra("lenderId");
        userId = firebaseAuth.getCurrentUser().getUid();
        pricePerDay = getIntent().getDoubleExtra("pricePerDay", 0.0);

        // UI references
        tvItemName = findViewById(R.id.tv_item_name);
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        etLendingDays = findViewById(R.id.et_lending_days);
        cbAgreeTerms = findViewById(R.id.cb_agree_terms);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnCompleteBooking = findViewById(R.id.btn_complete_booking);
        progressBar = findViewById(R.id.progressBar);
        tvTotalPrice = findViewById(R.id.tv_total_price);

        tvItemName.setText(itemName);

        // Select Date Button Click
        btnSelectDate.setOnClickListener(v -> showDatePickerDialog());

        // Complete Booking Button Click
        btnCompleteBooking.setOnClickListener(v -> completeBooking());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            tvSelectedDate.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void completeBooking() {
        String lendingDaysStr = etLendingDays.getText().toString();
        boolean agreed = cbAgreeTerms.isChecked();

        if (TextUtils.isEmpty(selectedDate) || lendingDaysStr.isEmpty() || !agreed) {
            Toast.makeText(this, "Please fill all details and agree to terms", Toast.LENGTH_SHORT).show();
            return;
        }

        int lendingDays = Integer.parseInt(lendingDaysStr);
        double totalPrice = lendingDays * pricePerDay;
        tvTotalPrice.setText("Total Price: $" + totalPrice);

        Date startDate;
        try {
            startDate = dateFormat.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid start date format", Toast.LENGTH_SHORT).show();
            return;
        }

        Date endDate = calculateEndDate(startDate, lendingDays);
        String endDateStr = dateFormat.format(endDate);

        progressBar.setVisibility(View.VISIBLE);

        // Create booking data
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("itemId", itemId);
        bookingData.put("itemName", itemName);
        bookingData.put("lenderId", lenderId);
        bookingData.put("userId", userId);
        bookingData.put("startDate", selectedDate);
        bookingData.put("endDate", endDateStr);
        bookingData.put("totalPrice", totalPrice);
        bookingData.put("status", "booked");

        firestore.collection("bookings").add(bookingData)
                .addOnSuccessListener(documentReference -> {
                    markItemAsUnavailable();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(BookingActivity.this, "Failed to complete booking", Toast.LENGTH_SHORT).show();
                });
    }

    private Date calculateEndDate(Date startDate, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_YEAR, days - 1);
        return calendar.getTime();
    }

    private void markItemAsUnavailable() {
        DocumentReference itemRef = firestore.collection("listings").document(itemId);
        itemRef.update("available", false, "status", "unavailable")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(BookingActivity.this, "Booking completed successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(BookingActivity.this, "Failed to update item availability", Toast.LENGTH_SHORT).show();
                });
    }
}
