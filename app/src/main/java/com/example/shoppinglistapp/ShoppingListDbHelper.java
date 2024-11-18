package com.example.shoppinglistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShoppingListDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "shoppinglist.db";
    private static final int DATABASE_VERSION = 3;  // Increment version to update database

    // Shopping list table columns
    public static final String TABLE_NAME = "shopping_items";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_CATEGORY_ID = "category_id";  // Foreign key to categories table

    // Categories table columns
    public static final String CATEGORY_TABLE_NAME = "categories";
    public static final String CATEGORY_COLUMN_ID = "_id";
    public static final String CATEGORY_COLUMN_NAME = "category_name";

    // SQL to create tables
    private static final String CREATE_CATEGORY_TABLE =
            "CREATE TABLE " + CATEGORY_TABLE_NAME + " (" +
                    CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CATEGORY_COLUMN_NAME + " TEXT NOT NULL);";

    private static final String CREATE_SHOPPING_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_QUANTITY + " INTEGER, " +
                    COLUMN_NOTES + " TEXT, " +
                    COLUMN_CATEGORY_ID + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " + CATEGORY_TABLE_NAME + "(" + CATEGORY_COLUMN_ID + "));";

    public ShoppingListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_SHOPPING_TABLE);  // Create shopping items table with category foreign key
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
        onCreate(db);
    }

    // Insert a new category
    public long insertCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_COLUMN_NAME, categoryName);

        return db.insert(CATEGORY_TABLE_NAME, null, values);  // Return the row ID of the inserted category
    }

    // Check if a category already exists
    public boolean categoryExists(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CATEGORY_TABLE_NAME,
                new String[]{CATEGORY_COLUMN_NAME},
                CATEGORY_COLUMN_NAME + " = ?",
                new String[]{categoryName},
                null, null, null);

        boolean exists = cursor != null && cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // Insert a new shopping item
    public long insertItem(String name, int quantity, String notes, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_NOTES, notes);
        values.put(COLUMN_CATEGORY_ID, categoryId);  // Associate item with category

        return db.insert(TABLE_NAME, null, values);  // Return the row ID of the inserted item
    }

    // Retrieve all categories
    // Get all categories for the Spinner
    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(CATEGORY_TABLE_NAME,
                new String[]{CATEGORY_COLUMN_NAME},
                null, null, null, null, null);
    }

    // Get category ID by category name
    public Cursor getCategoryIdByName(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(CATEGORY_TABLE_NAME,
                new String[]{CATEGORY_COLUMN_ID},
                CATEGORY_COLUMN_NAME + " = ?",
                new String[]{categoryName},
                null, null, null);
    }

}
