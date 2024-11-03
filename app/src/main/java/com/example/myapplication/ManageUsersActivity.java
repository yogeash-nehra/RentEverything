package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {

    private RecyclerView usersRecyclerView;
    private UsersAdapter usersAdapter; // Adapter to display the users in a RecyclerView
    private List<User> userList;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        firestore = FirebaseFirestore.getInstance();
        userList = new ArrayList<>();

        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersAdapter = new UsersAdapter(this, userList, new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUpdateRoleClick(User user) {
                updateRole(user);
            }

            @Override
            public void onDeleteUserClick(User user) {
                deleteUser(user);
            }
        });

        usersRecyclerView.setAdapter(usersAdapter);

        fetchUsers();
    }

    private void fetchUsers() {
        // Fetch users who have not booked any listings
        firestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            userList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                if (user != null) {
                                    user.setId(document.getId());
                                    userList.add(user);
                                }
                            }
                            usersAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ManageUsersActivity.this, "Failed to load users.", Toast.LENGTH_SHORT).show();
                            Log.d("ManageUsersActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void updateRole(User user) {
        // Toggle the role between "user" and "lender" for simplicity
        String newRole = user.getRole().equals("user") ? "lender" : "user";

        firestore.collection("users").document(user.getId())
                .update("role", newRole)
                .addOnSuccessListener(aVoid -> {
                    user.setRole(newRole); // Update role in the local list as well
                    usersAdapter.notifyDataSetChanged();
                    Toast.makeText(ManageUsersActivity.this, "User role updated to " + newRole, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(ManageUsersActivity.this, "Failed to update role.", Toast.LENGTH_SHORT).show());
    }

    private void deleteUser(User user) {
        firestore.collection("bookings")
                .whereEqualTo("userId", user.getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().isEmpty()) {
                        // Delete user only if they have no bookings
                        firestore.collection("users").document(user.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    userList.remove(user);
                                    usersAdapter.notifyDataSetChanged();
                                    Toast.makeText(ManageUsersActivity.this, "User deleted successfully.", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(ManageUsersActivity.this, "Failed to delete user.", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(ManageUsersActivity.this, "Cannot delete user with active bookings.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
