package com.example.shoppinglistapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ShoppingListAdapter extends CursorAdapter {

    public ShoppingListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Retrieve values from the cursor
        String itemName = cursor.getString(cursor.getColumnIndexOrThrow(ShoppingListDbHelper.COLUMN_NAME));
        String quantity = cursor.getString(cursor.getColumnIndexOrThrow(ShoppingListDbHelper.COLUMN_QUANTITY));
        String notes = cursor.getString(cursor.getColumnIndexOrThrow(ShoppingListDbHelper.COLUMN_NOTES));
        long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(ShoppingListDbHelper.COLUMN_ID));

        // Bind data to the views
        TextView itemNameTextView = view.findViewById(R.id.itemName);
        itemNameTextView.setText(itemName);

        TextView itemQuantityTextView = view.findViewById(R.id.itemQuantity);
        itemQuantityTextView.setText("Quantity: " + quantity);

        // Display "NA" if no notes are available
        TextView itemNotesTextView = view.findViewById(R.id.itemNotes);
        if (notes != null && !notes.isEmpty()) {
            itemNotesTextView.setText("Notes: " + notes);
        } else {
            itemNotesTextView.setText("Notes: NA");
        }

        // Edit button logic
        Button editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(v -> {
            // Create intent to open the AddEditItemActivity with the itemId
            Intent editIntent = new Intent(context, AddEditItemActivity.class);
            editIntent.putExtra("item_id", itemId);
            context.startActivity(editIntent);
        });

        // Delete button logic
        Button deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            Uri uri = Uri.withAppendedPath(Uri.parse("content://com.example.shoppinglistapp.provider/shopping_items"), String.valueOf(itemId));
            int deletedRows = context.getContentResolver().delete(uri, null, null);

            // If the item is deleted, update the cursor
            if (deletedRows > 0) {
                Toast.makeText(view.getContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show();

                // Reload the list by querying the content provider again
                Cursor updatedCursor = context.getContentResolver().query(
                        Uri.parse("content://com.example.shoppinglistapp.provider/shopping_items"),
                        null, null, null, null);

                // Swap the old cursor with the new one to refresh the list
                if (updatedCursor != null) {
                    swapCursor(updatedCursor);
                }
            } else {
                // If deletion fails
                Toast.makeText(view.getContext(), "Failed to delete item", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
