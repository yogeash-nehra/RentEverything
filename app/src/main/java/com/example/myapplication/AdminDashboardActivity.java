package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private RecyclerView usersRecyclerView;
    private UsersAdapter usersAdapter;
    private List<User> usersList;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        firestore = FirebaseFirestore.getInstance();
        usersList = new ArrayList<>();

        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersAdapter = new UsersAdapter(this, usersList, new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUpdateRoleClick(User user) {
                updateUserRole(user); // Call function to update user role
            }

            @Override
            public void onDeleteUserClick(User user) {
                deleteUser(user); // Call function to delete user
            }
        });
        usersRecyclerView.setAdapter(usersAdapter);

        fetchUsers();
    }

    private void fetchUsers() {
        firestore.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        usersList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            user.setId(document.getId()); // Set the document ID as user ID
                            usersList.add(user);
                        }
                        usersAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AdminDashboardActivity.this, "Error fetching users", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserRole(User user) {
        // Implement logic to update user role, e.g., show a dialog to select the new role
        Toast.makeText(this, "Update role for user: " + user.getName(), Toast.LENGTH_SHORT).show();
    }

    private void deleteUser(User user) {
        firestore.collection("users").document(user.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    usersList.remove(user);
                    usersAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "User deleted successfully.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete user.", Toast.LENGTH_SHORT).show());
    }
}
