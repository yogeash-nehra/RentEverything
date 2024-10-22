package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RentalsAdapter extends RecyclerView.Adapter<RentalsAdapter.RentalViewHolder> {

    private final Context context;
    private final List<Rental> rentalList; // Assuming Rental is a model class

    public RentalsAdapter(Context context, List<Rental> rentalList) {
        this.context = context;
        this.rentalList = rentalList;
    }

    @NonNull
    @Override
    public RentalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rental, parent, false);
        return new RentalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentalViewHolder holder, int position) {
        Rental rental = rentalList.get(position);
        holder.rentalDetails.setText(rental.getRentalDetails()); // Assuming Rental class has a getDetails method
    }

    @Override
    public int getItemCount() {
        return rentalList.size();
    }

    static class RentalViewHolder extends RecyclerView.ViewHolder {
        TextView rentalDetails;

        public RentalViewHolder(View itemView) {
            super(itemView);
            rentalDetails = itemView.findViewById(R.id.rentalDetailsTextView); // Ensure this ID matches your item layout
        }
    }
}
