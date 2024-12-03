package com.example.pc;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        // Find the TextView to display data
        TextView textView = findViewById(R.id.showdata);

        // Set up Firebase reference to "products"
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("products");

        // Retrieve all products without limiting
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if the data exists
                if (snapshot.exists()) {
                    // Iterate over the child nodes (all products in this case)
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        String key = productSnapshot.getKey();
                        Log.d("ProductSnapshot", "Raw data: " + productSnapshot.getValue());
                        String productType = productSnapshot.child("product_type").getValue(String.class);
                        Integer quantity = productSnapshot.child("quantity").getValue(Integer.class);
                        textView.append(productType+"\n");




//                         textView.append(y);
//                        textView.append(product);
//
//                        if (product != null) {
//                            // Display the product data in your TextView
//                            String productData = "Product Type: " + product.getProduct_type() + "\n" +
//                                    "Quantity: " + product.getQuantity() + "\n\n";
//                            // Append product data to the TextView
//                            textView.append(productData);
//                        }
                    }
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
