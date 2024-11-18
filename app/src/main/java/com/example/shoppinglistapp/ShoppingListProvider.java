package com.example.shoppinglistapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.content.UriMatcher;

public class ShoppingListProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://com.example.shoppinglistapp.provider/shopping_items");
    private static final int ITEMS = 100;
    private static final int ITEM_ID = 101;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // Define URI pattern matching
        uriMatcher.addURI("com.example.shoppinglistapp.provider", "shopping_items", ITEMS);  // for all items
        uriMatcher.addURI("com.example.shoppinglistapp.provider", "shopping_items/#", ITEM_ID); // for single item
    }

    private ShoppingListDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new ShoppingListDbHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(ShoppingListDbHelper.TABLE_NAME, null, values);
        if (id > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, id);
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);

        switch (match) {
            case ITEMS:
                return db.query(ShoppingListDbHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            case ITEM_ID:
                selection = ShoppingListDbHelper.COLUMN_ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()}; // Get the item ID from the URI
                return db.query(ShoppingListDbHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowsUpdated = 0;

        switch (match) {
            case ITEMS:
                rowsUpdated = db.update(ShoppingListDbHelper.TABLE_NAME, values, selection, selectionArgs);
                break;
            case ITEM_ID:
                selection = ShoppingListDbHelper.COLUMN_ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                rowsUpdated = db.update(ShoppingListDbHelper.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowsDeleted = 0;

        switch (match) {
            case ITEMS:
                rowsDeleted = db.delete(ShoppingListDbHelper.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM_ID:
                selection = ShoppingListDbHelper.COLUMN_ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                rowsDeleted = db.delete(ShoppingListDbHelper.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ITEMS:
                return "vnd.android.cursor.dir/vnd.com.example.shoppinglistapp.shopping_items"; // Directory MIME type
            case ITEM_ID:
                return "vnd.android.cursor.item/vnd.com.example.shoppinglistapp.shopping_items"; // Single item MIME type
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
