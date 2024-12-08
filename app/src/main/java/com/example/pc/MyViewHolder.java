package com.example.pc;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.PopupMenu;
import android.view.MenuInflater;


public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView t1, t2, t3, t4;
    EditText et1,et2,et3,et4;
    Button savebutton;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        t1 = itemView.findViewById(R.id.textView1);
        t2 = itemView.findViewById(R.id.textView2);
        t3 = itemView.findViewById(R.id.textView3);
        t4 = itemView.findViewById(R.id.textView4);
        et1 = itemView.findViewById(R.id.edit_t1);
        et2 = itemView.findViewById(R.id.edit_t2);
        et3 = itemView.findViewById(R.id.edit_t3);
        et4 = itemView.findViewById(R.id.edit_t4);


        savebutton = itemView.findViewById(R.id.save_button);
        setEditableMode(false);


        // Set a long click listener on the entire itemView
        itemView.setOnLongClickListener(v -> {
            // Show the popup menu when the item is long pressed
            showPopupMenu(v);
            return true;  // Return true to indicate the event was handled
        });
    }

    public void setEditableMode(boolean isEditable) {
        if (isEditable) {
            // Hide TextViews and show EditTexts
            t1.setVisibility(View.GONE);
            t2.setVisibility(View.GONE);
            t3.setVisibility(View.GONE);
            t4.setVisibility(View.GONE);

            et1.setVisibility(View.VISIBLE);
            et2.setVisibility(View.VISIBLE);
            et3.setVisibility(View.VISIBLE);
            et4.setVisibility(View.VISIBLE);

            // Set the values in EditText
            et1.setText(t1.getText().toString());
            et2.setText(t2.getText().toString());
            et3.setText(t3.getText().toString());
            et4.setText(t4.getText().toString());

            // Make the save button visible (if you're using one)
            savebutton.setVisibility(View.VISIBLE);

        } else {
            // Hide EditTexts and show TextViews
            et1.setVisibility(View.GONE);
            et2.setVisibility(View.GONE);
            et3.setVisibility(View.GONE);
            et4.setVisibility(View.GONE);

            t1.setVisibility(View.VISIBLE);
            t2.setVisibility(View.VISIBLE);
            t3.setVisibility(View.VISIBLE);
            t4.setVisibility(View.VISIBLE);

            // Hide the save button after the user has finished editing
            savebutton.setVisibility(View.GONE);
        }
    }

    // Method to show the PopupMenu
    private void showPopupMenu(View view) {
        // Create the PopupMenu
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();

        // Inflate the custom menu (Edit and Delete options)
        inflater.inflate(R.menu.item_options_menu, popupMenu.getMenu());

        // Set a listener for menu item clicks
        popupMenu.setOnMenuItemClickListener(item -> {
            // Use if-else instead of switch to avoid constant expression error
            if (item.getItemId() == R.id.menu_edit) {
                // Handle Edit action
                if (listener != null) {
                    listener.onEditClick(getAdapterPosition());  // Trigger the edit action
                }
                return true;  // Indicate that the event was handled
            } else if (item.getItemId() == R.id.menu_delete) {
                // Handle Delete action
                if (listener != null) {
                    listener.onDeleteClick(getAdapterPosition());  // Trigger the delete action
                }
                return true;  // Indicate that the event was handled
            }
            return false;  // Default return value when no valid item was clicked
        });

        // Show the popup menu
        popupMenu.show();
    }


    // Interface to handle Edit/Delete actions in the Adapter or Activity
    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    private OnItemClickListener listener;

    // Set the listener from the adapter
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
