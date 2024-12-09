package com.example.pc;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AvailableProducts extends AppCompatActivity {

    private TextView types, avail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_available_products);

        types = findViewById(R.id.text3);  // Assuming you have TextViews with these IDs
        avail = findViewById(R.id.text4);  // Assuming you have TextViews with these IDs

        // List of product names
        ArrayList<String> productNames = new ArrayList<>();
        productNames.add("WN");
        productNames.add("WL");
        productNames.add("W");
        productNames.add("DC");
        productNames.add("D");
        productNames.add("DBLT");
        productNames.add("C");
        productNames.add("Wm");
        productNames.add("FL");
        productNames.add("R");
        productNames.add("PD");
        productNames.add("BW");

        // Map to store product names with their corresponding availability count
        Map<String, Integer> productMap = new HashMap<>();

        // Initialize productMap with 0 availability for each product
        for (String product : productNames) {
            productMap.put(product.toLowerCase(), 0);
        }

        // Reference to Firebase database
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("products");

        // Fetch data from Firebase
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Loop through the products in the Firebase database
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String productType = dataSnapshot.child("product_type").getValue(String.class);
                        Integer quantity = dataSnapshot.child("quantity").getValue(Integer.class);
                        String inOutStatus = dataSnapshot.child("inOutStatus").getValue(String.class);

                        if (productType != null && quantity != null && inOutStatus != null) {
                            // Adjust availability based on "in" or "out" status
                            if (inOutStatus.toLowerCase().equals("in")) {
                                productMap.put(productType.toLowerCase(), productMap.get(productType.toLowerCase()) + quantity);
                            } else {
                                productMap.put(productType.toLowerCase(), productMap.get(productType.toLowerCase()) - quantity);
                            }
                        }
                    }
                }
                // Update the UI after data is fetched and processed
                updateProductAvailability(productNames, productMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        // Window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.available), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Method to update the UI with product names and availability
    private void updateProductAvailability(ArrayList<String> productNames, Map<String, Integer> productMap) {
        StringBuilder typesText = new StringBuilder();
        StringBuilder availText = new StringBuilder();

        // Loop through each product and append to the text fields
        for (String product : productNames) {
            typesText.append(product).append("\n");
            availText.append(productMap.get(product.toLowerCase())).append("\n");
        }

        // Update the TextViews
        types.setText(typesText.toString());
        avail.setText(availText.toString());
    }
}
