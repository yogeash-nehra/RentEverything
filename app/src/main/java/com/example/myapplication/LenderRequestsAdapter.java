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

public class LenderRequestsAdapter extends RecyclerView.Adapter<LenderRequestsAdapter.ViewHolder> {

    private final Context context;
    private final List<LenderRequest> lenderRequests;
    private final AdminDashboardActivity adminActivity;

    public LenderRequestsAdapter(Context context, List<LenderRequest> lenderRequests) {
        this.context = context;
        this.lenderRequests = lenderRequests;
        this.adminActivity = (AdminDashboardActivity) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lender_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LenderRequest request = lenderRequests.get(position);

        // Set the text fields with appropriate data
        holder.userIdTextView.setText("User ID: " + request.getUserId());
        holder.statusTextView.setText("Status: " + request.getStatus());
        holder.lenderDetailsTextView.setText(request.getDetails());

        // Approve button functionality
        /*holder.approveButton.setOnClickListener(v -> {
            adminActivity.approveRequest(request.getUserId());
            lenderRequests.remove(position);
            notifyItemRemoved(position);
        });*/

        // Reject button functionality (optional)
/*        holder.rejectButton.setOnClickListener(v -> {
            adminActivity.rejectRequest(request.getUserId());
            lenderRequests.remove(position);
            notifyItemRemoved(position);
        });*/
    }

    @Override
    public int getItemCount() {
        return lenderRequests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lenderDetailsTextView;
        TextView userIdTextView;
        TextView statusTextView;
        Button approveButton;
        Button rejectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lenderDetailsTextView = itemView.findViewById(R.id.lenderDetailsTextView);
            userIdTextView = itemView.findViewById(R.id.userIdTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            approveButton = itemView.findViewById(R.id.approveButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }
}
