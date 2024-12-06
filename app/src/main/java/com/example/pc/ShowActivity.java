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
                        Long timestamp = productSnapshot.child("timestamp").getValue(Long.class); // Get the timestamp from Firebase

                        // Convert the timestamp to Date
                        Date date = new Date(timestamp);

                        // Format the date to Bangladesh Time (BST, UTC+6)
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Dhaka"));

                        // Convert the Date to a formatted string
                        String formattedDate = sdf.format(date);
                        Log.d("inOutStatus", "onDataChange: "+quantity );

                        // Create a new Product object and add it to the product list
                        productList.add(new Product(productType, quantity, inOutStatus, formattedDate));
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
