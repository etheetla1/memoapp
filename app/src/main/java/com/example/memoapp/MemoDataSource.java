package com.example.memoapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class MemoDataSource {
    private SQLiteDatabase database;
    private MemoDBHelper dbHelper;

    public MemoDataSource(Context context) {
        dbHelper = new MemoDBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addMemo(String text, String priority) {
        ContentValues values = new ContentValues();
        values.put("memo_text", text);
        values.put("priority", priority);
        database.insert("memo", null, values);
    }

    public ArrayList<Memo> getMemosByPriority() {
        ArrayList<Memo> memos = new ArrayList<>();
        String[] columns = {"_id", "memo_text", "priority"};
        Cursor cursor = database.query("memo", columns, null, null, null, null, "priority ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
//            Memo memo = new Memo(cursor.getString(1), cursor.getString(2));
//            memos.add(memo);
//            cursor.moveToNext();
        }
        cursor.close();
        return memos;
    }
}
