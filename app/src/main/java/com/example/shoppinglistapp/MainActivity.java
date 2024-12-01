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
        adapter = new ShoppingListAdapter(this, null);
        listView.setAdapter(adapter);

        findViewById(R.id.addItemButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
            startActivity(intent);
        });

        loadShoppingItems();
    }

    private void loadShoppingItems() {
        new Thread(() -> {
            Cursor cursor = getContentResolver().query(
                    ShoppingListProvider.CONTENT_URI, null, null, null, null);
            runOnUiThread(() -> {
                if (cursor != null) {
                    adapter.swapCursor(cursor);
                } else {
                    Toast.makeText(this, "No items to display", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadShoppingItems();
    }
}
