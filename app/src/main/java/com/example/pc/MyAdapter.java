package com.example.pc;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    List<Product> list;

    public MyAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        Log.d("siammondol", "onBindViewHolder: "+list.get(position).getProduct_type());
//        holder.t1.setText(list.get(2).getProduct_type());
//        holder.t2.setText("hafiz");
//        holder.t3.setText("hafiz");

//        holder.t1.setText(list.get(position).getProduct_type());
//        int quantity = list.get(position).getQuantity();
//        holder.t2.setText(String.valueOf(quantity));
//
//        holder.t3.setText(list.get(position).getDate());
//        holder.t4.setText(list.get(position).getInOutStatus());
        Product product = list.get(position);
        holder.t1.setText(product.getProduct_type());  // Set product type
        int quantity = product.getQuantity();          // Get the quantity
        holder.t2.setText(String.valueOf(quantity));   // Set the quantity
        holder.t3.setText(product.getDate());          // Set the date
        holder.t4.setText(product.getInOutStatus());   // Set the in/out status

        // Check if the status is "out" and set the background color accordingly
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


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
