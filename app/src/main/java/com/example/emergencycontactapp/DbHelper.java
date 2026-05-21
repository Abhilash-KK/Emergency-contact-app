package com.example.emergencycontactapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "contacts_db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_CONTACTS = "tbl_contacts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phoneno";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_CONTACTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    // INSERT
    public boolean insertContact(String name, String phoneno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phoneno);
        long result = db.insert(TABLE_CONTACTS, null, values);
        return result != -1;
    }

    // VIEW ALL
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                contactList.add(new Contact(id, name, phone));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

    // UPDATE
    public boolean updateContact(int id, String name, String phoneno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phoneno);
        int result = db.update(TABLE_CONTACTS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // DELETE
    public boolean deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CONTACTS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}
