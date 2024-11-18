package com.example.shoppinglistapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditItemActivity extends AppCompatActivity {

    private EditText itemNameEditText, itemQuantityEditText, itemNotesEditText;
    private Button saveButton;
    private TextView headingTextView; // Add a reference to the heading TextView
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
        headingTextView = findViewById(R.id.headingTextView); // Initialize heading TextView

        // Check if we're editing an item (via item ID passed in intent)
        itemId = getIntent().getLongExtra("item_id", -1);

        if (itemId != -1) {
            // Load item data if it's an edit
            loadItemData(itemId);
            headingTextView.setText("Edit Item");  // Change header text to "Edit Item"
            saveButton.setText("Save Changes");   // Set button text for editing
        } else {
            // It's a new item
            headingTextView.setText("Add New Item");  // Default header text for adding an item
            saveButton.setText("Add Item");           // Set button text for adding
        }

        // Save button logic
        saveButton.setOnClickListener(v -> saveItem());
    }

    // Load data for the item that is being edited
    private void loadItemData(long itemId) {
        Uri itemUri = Uri.withAppendedPath(ShoppingListProvider.CONTENT_URI, String.valueOf(itemId));
        Cursor cursor = getContentResolver().query(itemUri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int itemNameIndex = cursor.getColumnIndex(ShoppingListDbHelper.COLUMN_NAME);
            int itemQuantityIndex = cursor.getColumnIndex(ShoppingListDbHelper.COLUMN_QUANTITY);
            int itemNotesIndex = cursor.getColumnIndex(ShoppingListDbHelper.COLUMN_NOTES);

            // Check if the column indices are valid (greater than or equal to 0)
            if (itemNameIndex != -1 && itemQuantityIndex != -1 && itemNotesIndex != -1) {
                String itemName = cursor.getString(itemNameIndex);
                String itemQuantity = cursor.getString(itemQuantityIndex);
                String itemNotes = cursor.getString(itemNotesIndex);

                itemNameEditText.setText(itemName);
                itemQuantityEditText.setText(itemQuantity);
                itemNotesEditText.setText(itemNotes);
            } else {
                Toast.makeText(this, "Error: Columns not found in the database", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        } else {
            Toast.makeText(this, "Error loading item data or no data found", Toast.LENGTH_SHORT).show();
        }
    }

    // Save or update item in the database
    private void saveItem() {
        String itemName = itemNameEditText.getText().toString().trim();
        String itemQuantity = itemQuantityEditText.getText().toString().trim();
        String itemNotes = itemNotesEditText.getText().toString().trim();

        if (itemName.isEmpty() || itemQuantity.isEmpty()) {
            Toast.makeText(this, "Item name and quantity are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ShoppingListDbHelper.COLUMN_NAME, itemName);
        values.put(ShoppingListDbHelper.COLUMN_QUANTITY, itemQuantity);
        values.put(ShoppingListDbHelper.COLUMN_NOTES, itemNotes);

        if (itemId == -1) {
            // Insert new item
            Uri newItemUri = getContentResolver().insert(ShoppingListProvider.CONTENT_URI, values);
            if (newItemUri != null) {
                Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
                finish();  // Go back to the list
            } else {
                Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update existing item
            Uri updateUri = Uri.withAppendedPath(ShoppingListProvider.CONTENT_URI, String.valueOf(itemId));
            int rowsUpdated = getContentResolver().update(updateUri, values, null, null);
            if (rowsUpdated > 0) {
                Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                finish();  // Go back to the list
            } else {
                Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
