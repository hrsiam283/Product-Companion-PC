package com.example.pc;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Product> productList = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("products");

        // Retrieve all products without limiting
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if the data exists
                if (snapshot.exists()) {
                    // Iterate over the child nodes (all products in this case)
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        String productType = productSnapshot.child("product_type").getValue(String.class);
                        Integer quantity = productSnapshot.child("quantity").getValue(Integer.class);
                        String inOutStatus = productSnapshot.child("inOutStatus").getValue(String.class);
                        String timestamp = productSnapshot.child("date").getValue(String.class); // Get the timestamp from Firebase
                        Log.d("TimestampD", "Timestamp from Firebase: " + timestamp);

                        // Convert the timestamp to Date

                        productList.add(new Product(productType, quantity, inOutStatus, timestamp));
                    }

                    // Log the size of the product list after adding products
                    Log.d("ProductListSize", "Size of productList: " + productList.size());

                    // Set the RecyclerView adapter after the product list is populated
                    recyclerView.setAdapter(new MyAdapter(getApplicationContext(), productList));

                } else {
                    // Show a message if no data is found
                    Toast.makeText(ShowActivity.this, "No products found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
                Toast.makeText(ShowActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
