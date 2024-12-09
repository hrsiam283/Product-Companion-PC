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
                        String date = productSnapshot.child("date").getValue(String.class); // Get the timestamp from Firebase
                        String productKey = productSnapshot.child("productKey").getValue(String.class);
                        Log.d("TimestampD", "Timestamp from Firebase: " + date);

                        // Convert the timestamp string to a Date object and format it

                        // Add the new Product to the list
                        productList.add(new Product(productType, quantity, inOutStatus, date,productKey));
                    }

                    // Log the size of the product list after adding products
                    Log.d("ProductListSize", "Size of productList: " + productList.size());

                    // Check if the list is empty
                    if (productList.isEmpty()) {
                        Toast.makeText(ShowActivity.this, "No products available", Toast.LENGTH_SHORT).show();
                    }

                    // Set the RecyclerView adapter after the product list is populated
                    recyclerView.setAdapter(new MyAdapter(ShowActivity.this, productList, new MyViewHolder.OnItemClickListener() {
                        @Override
                        public void onEditClick(int position) {
                            // Get the product at the selected position
                            Product product = productList.get(position);

                            // Find the ViewHolder for the clicked item in the RecyclerView
                            MyViewHolder holder = (MyViewHolder) recyclerView.findViewHolderForAdapterPosition(position);

                            if (holder != null) {
                                // Make the fields editable by toggling visibility between TextView and EditText
                                holder.setEditableMode(true);  // This will make the EditText fields visible and TextViews hidden

                                // Optionally, pre-fill the EditText fields with the current values
                                holder.et1.setText(product.getProduct_type());
                                holder.et2.setText(String.valueOf(product.getQuantity()));
                                holder.et3.setText(product.getDate());
                                holder.et4.setText(product.getInOutStatus());

                                // You can also set an OnClickListener for the Save button if you need to handle saving changes
                                holder.savebutton.setOnClickListener(v -> {
                                    // Retrieve the updated values from the EditText fields
                                    String updatedProductType = holder.et1.getText().toString();
                                    String updatedQuantity = holder.et2.getText().toString();
                                    String updatedAdditionalInfo1 = holder.et3.getText().toString();
                                    String updatedAdditionalInfo2 = holder.et4.getText().toString();
                                    String productKey = product.getProductKey();

                                    // Create a new Product object with updated values
                                    Product updatedProduct = new Product(updatedProductType, Integer.parseInt(updatedQuantity), updatedAdditionalInfo2, updatedAdditionalInfo1,productKey);

                                    // Update the product in the list
                                    productList.set(position, updatedProduct);

                                    // Notify the adapter that the item has been updated
                                    recyclerView.getAdapter().notifyItemChanged(position);

                                    // Optionally, save to Firebase (or any data source you're using)
                                    saveProductToDatabase(updatedProduct);

                                    // After saving, switch back to non-editable mode (show TextView and hide EditText)
                                    holder.setEditableMode(false);
                                });
                            }
                        }

                        private void saveProductToDatabase(Product updatedProduct) {
                            // Get the product ID (assuming the Product has a productId field)
                            String productId = updatedProduct.getProductKey();

                            // Get the Firebase reference to the "products" node and the specific product by productId
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference("products");

                            // Set the updated product data under the specific product ID
                            database.child(productId).setValue(updatedProduct)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            // Successfully saved the product to Firebase
                                            Log.d("Firebase", "Product updated successfully");
                                            Toast.makeText(ShowActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Handle errors if saving fails
                                            Log.e("Firebase", "Failed to update product", task.getException());
                                            Toast.makeText(ShowActivity.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }



                        @Override
                        public void onDeleteClick(int position) {
                            // Get the product to delete
                            Product productToDelete = productList.get(position);

                            // Get the Firebase reference for the specific product
                            DatabaseReference productRef = FirebaseDatabase.getInstance()
                                    .getReference("products")
                                    .child(productToDelete.getProductKey());  // Assuming you have a unique ID for each product

                            // Remove the product from Firebase
                            productRef.removeValue()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            // If the deletion from Firebase was successful, remove the product from the local list
                                            productList.remove(position);
                                            recyclerView.getAdapter().notifyItemRemoved(position);
                                            Toast.makeText(ShowActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Handle failure (e.g., show a toast or log the error)
                                            Toast.makeText(ShowActivity.this, "Failed to delete product from Firebase", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                    }));

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

    // Helper method to format the timestamp into a readable date format
    private String formatDate(String timestamp) {
        try {
            // Assuming the timestamp is in a format like "yyyy-MM-dd'T'HH:mm:ss'Z'"
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            inputFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(timestamp);

            // Now format the date into a more readable format
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown Date";  // In case of parsing error, return a default value
        }
    }
}
