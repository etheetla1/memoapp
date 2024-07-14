package com.example.memoapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MemoListActivity extends AppCompatActivity {

    private Switch switchDelete;
    private Button buttonAddMemo;
    private RecyclerView rvMemos;
    private ImageButton imageButtonList;
    private ImageButton imageButtonSettings;
    private MemoDBHelper dbHelper;
    private MemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);

        switchDelete = findViewById(R.id.switchDelete);
        buttonAddMemo = findViewById(R.id.buttonAddMemo);
        rvMemos = findViewById(R.id.allMemos);
        imageButtonList = findViewById(R.id.imageButtonList);
        imageButtonSettings = findViewById(R.id.imageButtonSettings);

        dbHelper = new MemoDBHelper(this);
        adapter = new MemoAdapter(this, new ArrayList<>());

        rvMemos.setLayoutManager(new LinearLayoutManager(this));
        rvMemos.setAdapter(adapter);

        switchDelete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            adapter.setDeleteMode(isChecked);
            Toast.makeText(this, "Delete mode: " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
        });

        buttonAddMemo.setOnClickListener(v -> {
            Intent intent = new Intent(MemoListActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        imageButtonList.setOnClickListener(v -> {
            Toast.makeText(this, "Showing memo list...", Toast.LENGTH_SHORT).show();
        });

        imageButtonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MemoListActivity.this, MemoSettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        loadMemos();
    }

    private void loadMemos() {
        String criteria = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("SORTING_CRITERIA", "Priority"); // Default to "Priority"

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Memo> memoList = new ArrayList<>();

        Cursor cursor = db.query("memo", new String[]{"_id", "memo_text", "priority", "date"}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Memo memo = new Memo(
                        cursor.getInt(cursor.getColumnIndexOrThrow("_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("memo_text")),
                        cursor.getString(cursor.getColumnIndexOrThrow("priority")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date"))
                );
                memoList.add(memo);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Sort the memos based on the selected criteria
        if ("Priority".equals(criteria)) {
            Collections.sort(memoList, new Comparator<Memo>() {
                @Override
                public int compare(Memo m1, Memo m2) {
                    return getPriorityValue(m1.getPriority()) - getPriorityValue(m2.getPriority());
                }

                private int getPriorityValue(String priority) {
                    switch (priority) {
                        case "High":
                            return 1;
                        case "Medium":
                            return 2;
                        case "Low":
                            return 3;
                        default:
                            return 4;
                    }
                }
            });
        } else if ("Due Date".equals(criteria)) {
            Collections.sort(memoList, new Comparator<Memo>() {
                @Override
                public int compare(Memo m1, Memo m2) {
                    return m1.getDate().compareTo(m2.getDate());
                }
            });
        } else if ("Subject".equals(criteria)) {
            Collections.sort(memoList, Comparator.comparing(Memo::getText));
        }

        adapter.setMemoList(memoList);
    }
}

