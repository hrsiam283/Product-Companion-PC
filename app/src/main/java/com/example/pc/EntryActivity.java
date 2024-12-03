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

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EntryActivity extends AppCompatActivity {

    private Spinner spinnerProductName;
    private EditText editTextQuantity;
    private TextView textViewDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        // Initialize views
        spinnerProductName = findViewById(R.id.spinner_product_name);
        editTextQuantity = findViewById(R.id.edit_text_quantity);
        textViewDateTime = findViewById(R.id.text_view_date_time);

        // Databasereference


        // Set current date and time
        setDateTime();

        // Spinner setup
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, productNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductName.setAdapter(adapter);

        // Spinner item selection listener
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
    private void handleFormSubmit() {
        String productName = spinnerProductName.getSelectedItem().toString();
        String quantity = editTextQuantity.getText().toString();
        String dateTime = textViewDateTime.getText().toString();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("products");
        String productKey = database.push().getKey();


        // Validate quantity
        if (quantity.isEmpty()) {
            Toast.makeText(this, "Please enter a quantity.", Toast.LENGTH_SHORT).show();
            return;
        }
        Product one = new Product(productName, Integer.parseInt(quantity));
        assert productKey != null;
        database.child(productKey).setValue(one);


        // Display a message with the form data

        String message = "Product: " + productName + "\nQuantity: " + quantity + "\nDate & Time: " + dateTime;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // Reset the form
        editTextQuantity.setText("");
        spinnerProductName.setSelection(0);
    }
}
