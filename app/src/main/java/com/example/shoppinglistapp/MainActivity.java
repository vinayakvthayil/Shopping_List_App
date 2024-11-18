package com.example.shoppinglistapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ShoppingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.shoppingListView);

        // Initialize the adapter to prevent null pointer issues
        adapter = new ShoppingListAdapter(this, null);
        listView.setAdapter(adapter);

        loadShoppingItems();

        findViewById(R.id.addItemButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
            startActivity(intent);
        });
    }

    private void loadShoppingItems() {
        Cursor cursor = getContentResolver().query(
                Uri.parse("content://com.example.shoppinglistapp.provider/shopping_items"),
                null, null, null, null);

        if (cursor != null) {
            // Update the adapter with new cursor data
            adapter.swapCursor(cursor);
        } else {
            Toast.makeText(this, "No items to display", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload items whenever the activity is resumed
        loadShoppingItems();
    }
}
