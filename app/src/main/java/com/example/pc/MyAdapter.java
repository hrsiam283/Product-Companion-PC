package com.example.pc;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    List<Product> list;
    private MyViewHolder.OnItemClickListener listener;

    // Constructor to initialize context, list, and listener
    public MyAdapter(Context context, List<Product> list, MyViewHolder.OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;  // Initialize the listener for edit/delete
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create and return a new MyViewHolder with the item layout
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Get the product at the current position
        Product product = list.get(position);

        // Set text views with product data
        holder.t1.setText(product.getProduct_type());  // Set product type
        int quantity = product.getQuantity();          // Get the quantity
        holder.t2.setText(String.valueOf(quantity));   // Set the quantity
        holder.t3.setText(product.getDate());          // Set the date
        holder.t4.setText(product.getInOutStatus());   // Set the in/out status

        // Set background color based on "out" status
        if ("out".equalsIgnoreCase(product.getInOutStatus())) {
            // Change the background color to red
            holder.t1.setBackgroundColor(Color.RED);
            holder.t2.setBackgroundColor(Color.RED);
            holder.t3.setBackgroundColor(Color.RED);
            holder.t4.setBackgroundColor(Color.RED);
        } else {
            holder.t1.setBackgroundColor(Color.GREEN);
            holder.t2.setBackgroundColor(Color.GREEN);
            holder.t3.setBackgroundColor(Color.GREEN);
            holder.t4.setBackgroundColor(Color.GREEN);
        }

        // Set the listener on the ViewHolder for long-clicks
        holder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
