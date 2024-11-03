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

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final Context context;
    private final List<User> userList;
    private final OnUserClickListener listener;

    // Interface to handle update and delete actions
    public interface OnUserClickListener {
        void onUpdateRoleClick(User user);
        void onDeleteUserClick(User user);
    }

    public UsersAdapter(Context context, List<User> userList, OnUserClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText(user.getName());
        holder.userEmail.setText(user.getEmail());

        // Set click listeners for update and delete buttons
        holder.updateRoleButton.setOnClickListener(v -> listener.onUpdateRoleClick(user));
        holder.deleteUserButton.setOnClickListener(v -> listener.onDeleteUserClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView userEmail;
        Button updateRoleButton;
        Button deleteUserButton;

        public UserViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userNameTextView);
            userEmail = itemView.findViewById(R.id.userEmailTextView);
            updateRoleButton = itemView.findViewById(R.id.updateRoleButton);
            deleteUserButton = itemView.findViewById(R.id.deleteUserButton);
        }
    }
}
