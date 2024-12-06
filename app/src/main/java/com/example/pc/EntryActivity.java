package com.example.pc;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EntryActivity extends AppCompatActivity {

    private Spinner spinnerProductName;
    private Spinner spinnerInOut; // New Spinner for IN/OUT
    private EditText editTextQuantity;
    private TextView textViewDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        // Initialize Firebase (Ensure Firebase is initialized)
        // FirebaseApp.initializeApp(this); // Uncomment if needed in case Firebase is not auto-initialized

        // Initialize views
        spinnerProductName = findViewById(R.id.spinner_product_name);
        spinnerInOut = findViewById(R.id.spinner_in_out); // Initialize IN/OUT Spinner
        editTextQuantity = findViewById(R.id.edit_text_quantity);
        textViewDateTime = findViewById(R.id.text_view_date_time);

        // Set current date and time
        setDateTime();

        // Product name spinner setup
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductName.setAdapter(adapter);

        // IN/OUT spinner setup
        ArrayList<String> inOutOptions = new ArrayList<>();
        inOutOptions.add("IN");
        inOutOptions.add("OUT");
        ArrayAdapter<String> inOutAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, inOutOptions);
        inOutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInOut.setAdapter(inOutAdapter);

        // Spinner item selection listener for product names
        spinnerProductName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String item = parentView.getItemAtPosition(position).toString();
                Toast.makeText(EntryActivity.this, "Selected: " + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Optionally handle if nothing is selected
            }
        });

        // Submit button setup
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> handleFormSubmit());

        // Handle window insets for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.entry), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Method to set current date and time
    private void setDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        textViewDateTime.setText(currentDateAndTime);
    }

    // Method to handle form submission
//    private void handleFormSubmit() {
//        String productName = spinnerProductName.getSelectedItem().toString();
//        String inOutSelection = spinnerInOut.getSelectedItem().toString(); // Get IN/OUT selection
//        String quantity = editTextQuantity.getText().toString();
//        String dateTime = textViewDateTime.getText().toString();
//
//        // Validate form fields
//        if (productName.isEmpty()) {
//            Toast.makeText(this, "Please select a product.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (quantity.isEmpty()) {
//            Toast.makeText(this, "Please enter a quantity.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Push data to Firebase
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference("products");
//        String productKey = database.push().getKey();
//
//        Product product = new Product(productName, Integer.parseInt(quantity), inOutSelection);
//        assert productKey != null;
//        database.child(productKey).setValue(product);
//
//        // Display a message with the form data
//        String message = "Product: " + productName + "\nQuantity: " + quantity + "\nIN/OUT: " + inOutSelection + "\nDate & Time: " + dateTime;
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//
//        // Reset the form
//        editTextQuantity.setText("");
//        spinnerProductName.setSelection(0);
//        spinnerInOut.setSelection(0);
//    }
    private void handleFormSubmit() {
        String productName = spinnerProductName.getSelectedItem().toString();
        String inOutSelection = spinnerInOut.getSelectedItem().toString(); // Get IN/OUT selection
        String quantity = editTextQuantity.getText().toString();

        // Validate form fields
        if (productName.isEmpty()) {
            Toast.makeText(this, "Please select a product.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (quantity.isEmpty()) {
            Toast.makeText(this, "Please enter a quantity.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get Firebase Database reference
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("products");

        // Create Product object
        Product product = new Product(productName, Integer.parseInt(quantity), inOutSelection,"");

        // Add timestamp using Firebase's ServerValue.TIMESTAMP
        String productKey = database.push().getKey();
        assert productKey != null;
        database.child(productKey).setValue(product);

        // Save timestamp separately (if needed)
        database.child(productKey).child("timestamp").setValue(ServerValue.TIMESTAMP);

        // Display a message with the form data
        String message = "Product: " + productName + "\nQuantity: " + quantity + "\nIN/OUT: " + inOutSelection;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // Reset the form
        editTextQuantity.setText("");
        spinnerProductName.setSelection(0);
        spinnerInOut.setSelection(0);
    }

}
