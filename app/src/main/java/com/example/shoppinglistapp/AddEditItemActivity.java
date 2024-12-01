package com.example.shoppinglistapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditItemActivity extends AppCompatActivity {

    private EditText itemNameEditText, itemQuantityEditText, itemNotesEditText;
    private Button saveButton;
    private TextView headingTextView;
    private long itemId = -1;  // -1 means it's a new item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        // Initialize views
        itemNameEditText = findViewById(R.id.itemNameEditText);
        itemQuantityEditText = findViewById(R.id.itemQuantityEditText);
        itemNotesEditText = findViewById(R.id.itemNotesEditText);
        saveButton = findViewById(R.id.saveButton);
        headingTextView = findViewById(R.id.headingTextView);

        // Check if editing an item
        itemId = getIntent().getLongExtra("item_id", -1);
        if (itemId != -1) {
            headingTextView.setText("Edit Item");
            saveButton.setText("Save Changes");
            loadItemData(itemId);
        } else {
            headingTextView.setText("Add New Item");
            saveButton.setText("Add Item");
        }

        saveButton.setOnClickListener(v -> saveItem());
    }

    @SuppressLint("Range")
    private void loadItemData(long itemId) {
        new Thread(() -> {
            Uri itemUri = Uri.withAppendedPath(ShoppingListProvider.CONTENT_URI, String.valueOf(itemId));
            Cursor cursor = getContentResolver().query(itemUri, null, null, null, null);
            runOnUiThread(() -> {
                if (cursor != null && cursor.moveToFirst()) {
                    itemNameEditText.setText(cursor.getString(cursor.getColumnIndex("name")));
                    itemQuantityEditText.setText(cursor.getString(cursor.getColumnIndex("quantity")));
                    itemNotesEditText.setText(cursor.getString(cursor.getColumnIndex("notes")));
                    cursor.close();
                } else {
                    Toast.makeText(this, "Error loading item data", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void saveItem() {
        String itemName = itemNameEditText.getText().toString().trim();
        String itemQuantity = itemQuantityEditText.getText().toString().trim();
        String itemNotes = itemNotesEditText.getText().toString().trim();

        if (itemName.isEmpty() || itemQuantity.isEmpty()) {
            Toast.makeText(this, "Item name and quantity are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("name", itemName);
        values.put("quantity", itemQuantity);
        values.put("notes", itemNotes);

        new Thread(() -> {
            if (itemId == -1) {
                Uri newItemUri = getContentResolver().insert(ShoppingListProvider.CONTENT_URI, values);
                runOnUiThread(() -> {
                    if (newItemUri != null) {
                        Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Uri updateUri = Uri.withAppendedPath(ShoppingListProvider.CONTENT_URI, String.valueOf(itemId));
                int rowsUpdated = getContentResolver().update(updateUri, values, null, null);
                runOnUiThread(() -> {
                    if (rowsUpdated > 0) {
                        Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
}
