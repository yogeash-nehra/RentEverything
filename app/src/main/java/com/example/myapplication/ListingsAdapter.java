package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.ItemViewHolder> {

    private final Context context;
    private final List<Listing> itemList;
    private final OnItemActionListener onItemActionListener;
    private final boolean showUpdateDelete;

    public interface OnItemActionListener {
        void onItemClick(Listing item);
        void onUpdateClick(Listing item);
        void onDeleteClick(Listing item);
        void onBookNowClick(Listing listing);
    }

    public ListingsAdapter(Context context, List<Listing> itemList, OnItemActionListener listener, boolean showUpdateDelete) {
        this.context = context;
        this.itemList = itemList;
        this.onItemActionListener = listener;
        this.showUpdateDelete = showUpdateDelete;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listing, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Listing item = itemList.get(position);
        holder.itemTitle.setText(item.getTitle());
        holder.itemCategory.setText("Category: " + item.getCategory());
        holder.itemPrice.setText("Price per day: $" + item.getPrice());

        // Load image from Firestore URL or use placeholder
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.background) // Fallback placeholder if loading fails
                    .error(R.drawable.background) // Displayed on error
                    .into(holder.itemImage);
        } else {
            holder.itemImage.setImageResource(R.drawable.background);
        }

        // Show/hide update and delete buttons based on showUpdateDelete flag
        if (showUpdateDelete) {
            holder.updateButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
//            //holder.updateButton.setOnClickListener(v -> onItemActionListener.onUpdateClick(item));
            holder.updateButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, UpdateListingActivity.class);
                intent.putExtra("listing", item); // Pass the Listing object
                context.startActivity(intent);
            });

            holder.deleteButton.setOnClickListener(v -> onItemActionListener.onDeleteClick(item));
        } else {
            holder.updateButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }

        // Show Book Now button only if item is available
        if ("available".equals(item.getStatus())) {
            holder.bookNowButton.setVisibility(View.VISIBLE);
            holder.bookNowButton.setOnClickListener(v -> onItemActionListener.onBookNowClick(item));
        } else {
            holder.bookNowButton.setVisibility(View.GONE);
            holder.itemView.setAlpha(0.5f); // Dim view for unavailable items
        }

        // Set item click listener
        holder.itemView.setOnClickListener(v -> onItemActionListener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemCategory, itemPrice;
        ImageView itemImage;
        Button updateButton, deleteButton, bookNowButton;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitleTextView);
            itemCategory = itemView.findViewById(R.id.itemCategoryTextView);
            itemPrice = itemView.findViewById(R.id.itemPriceTextView);
            itemImage = itemView.findViewById(R.id.itemImage);
            updateButton = itemView.findViewById(R.id.btnUpdate);
            deleteButton = itemView.findViewById(R.id.btnDelete);
            bookNowButton = itemView.findViewById(R.id.btnBookNow); // Assuming this button is in the layout
        }
    }
}
