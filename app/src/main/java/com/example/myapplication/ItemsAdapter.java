package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private final Context context;
    private final List<Listing> itemList;
    private final OnItemActionListener onItemActionListener;

    public interface OnItemActionListener {
        void onUpdateClick(Listing item);
        void onDeleteClick(Listing item);
    }

    public ItemsAdapter(Context context, List<Listing> itemList, OnItemActionListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.onItemActionListener = listener;
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

        // Update button action
        holder.updateButton.setOnClickListener(v -> onItemActionListener.onUpdateClick(item));

        // Delete button action
        holder.deleteButton.setOnClickListener(v -> onItemActionListener.onDeleteClick(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemCategory, itemPrice;
        Button updateButton, deleteButton;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitleTextView);
            itemCategory = itemView.findViewById(R.id.itemCategoryTextView);
            itemPrice = itemView.findViewById(R.id.itemPriceTextView);
            updateButton = itemView.findViewById(R.id.btnUpdate);
            deleteButton = itemView.findViewById(R.id.btnDelete);
        }
    }
}
