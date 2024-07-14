package com.example.memoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MemoDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "memo.db";
    private static final int DATABASE_VERSION = 2;  // Incremented version for schema update

    private static final String CREATE_TABLE_MEMO = "CREATE TABLE memo (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "memo_text TEXT NOT NULL, priority TEXT NOT NULL, date TEXT NOT NULL);";

    public MemoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_MEMO);
        } catch (Exception e) {
            Log.e("MemoDBHelper", "Error creating database", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MemoDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS memo");
        onCreate(db);
    }
}