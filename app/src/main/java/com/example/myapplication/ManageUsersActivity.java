package com.example.myapplication;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class ManageUsersActivity extends AppCompatActivity {

    private RecyclerView usersRecyclerView;
    private UsersAdapter usersAdapter; // You will create this adapter
    private ArrayList<User> userList; // Ensure this is your custom User class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        userList = new ArrayList<>();
        usersAdapter = new UsersAdapter(this, userList); // Use your custom User class

        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersRecyclerView.setAdapter(usersAdapter);

        fetchUsers();
    }

    private void fetchUsers() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Use your custom User class instead of Firestore's User class
                        User user = new User();
                        user.setId(document.getId()); // Set Firestore document ID
                        user.setName(document.getString("name")); // Assuming you have a name field
                        user.setEmail(document.getString("email")); // Assuming you have an email field
                        userList.add(user);
                    }
                    usersAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
                } else {
                    // Handle error
                }
            }
        });
    }
}
