package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.ListingViewHolder> {

    private final Context context;
    private final List<Listing> listingList; // Assuming Listing is a model class

    public ListingsAdapter(Context context, List<Listing> listingList) {
        this.context = context;
        this.listingList = listingList;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listing, parent, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        Listing listing = listingList.get(position);
        holder.itemName.setText(listing.getTitle()); // Assuming Listing class has a getTitle method
        holder.itemDescription.setText(listing.getDescription()); // Assuming Listing class has a getDescription method
        holder.itemPrice.setText(String.valueOf(listing.getPrice())); // Displaying the price

        // Set an OnClickListener to open the details activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetailsActivity.class);
            intent.putExtra("listing", listing); // Pass the listing object to the details activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listingList.size();
    }

    static class ListingViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemDescription;
        TextView itemPrice; // Added price TextView

        public ListingViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemNameTextView); // Ensure this ID matches your item layout
            itemDescription = itemView.findViewById(R.id.itemDescriptionTextView); // Ensure this ID matches your item layout
            itemPrice = itemView.findViewById(R.id.itemPriceTextView); // Ensure this ID matches your item layout
        }
    }
}
