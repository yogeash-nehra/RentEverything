package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private final Context context;
    private final List<Listing> itemList; // Assuming Listing is a model class

    public ItemsAdapter(Context context, List<Listing> itemList) {
        this.context = context;
        this.itemList = itemList;
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
        holder.itemName.setText(item.getTitle()); // Assuming Listing class has a getName method
        holder.itemDescription.setText(item.getDescription()); // Assuming Listing class has a getDescription method
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemDescription;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemNameTextView); // Ensure this ID matches your item layout
            itemDescription = itemView.findViewById(R.id.itemDescriptionTextView); // Ensure this ID matches your item layout
        }
    }
}
