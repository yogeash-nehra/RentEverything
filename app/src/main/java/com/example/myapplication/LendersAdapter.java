package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LendersAdapter extends RecyclerView.Adapter<LendersAdapter.LenderViewHolder> {

    private final Context context;
    private final List<LenderRequest> lenderRequestList; // Assuming LenderRequest is a model class

    public LendersAdapter(Context context, List<LenderRequest> lenderRequestList) {
        this.context = context;
        this.lenderRequestList = lenderRequestList;
    }

    @NonNull
    @Override
    public LenderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lender_request, parent, false);
        return new LenderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LenderViewHolder holder, int position) {
        LenderRequest request = lenderRequestList.get(position);
        holder.lenderDetails.setText(request.getDetails()); // Assuming LenderRequest class has a getDetails method
    }

    @Override
    public int getItemCount() {
        return lenderRequestList.size();
    }

    static class LenderViewHolder extends RecyclerView.ViewHolder {
        TextView lenderDetails;

        public LenderViewHolder(View itemView) {
            super(itemView);
            lenderDetails = itemView.findViewById(R.id.lenderDetailsTextView); // Ensure this ID matches your item layout
        }
    }
}
