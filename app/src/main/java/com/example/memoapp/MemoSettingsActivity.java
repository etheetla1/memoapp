package com.example.memoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class MemoSettingsActivity extends AppCompatActivity {

    private ImageButton imageButtonList;
    private ImageButton imageButtonSettings;
    private RadioGroup radioGroupSortBy;
    private RadioButton radioPriority;
    private RadioButton radioDate;
    private RadioButton radioSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_settings);

        imageButtonList = findViewById(R.id.imageButtonList);
        imageButtonSettings = findViewById(R.id.imageButtonSettings);
        radioGroupSortBy = findViewById(R.id.radioGroupSortBy);
        radioPriority = findViewById(R.id.radioPriority);
        radioDate = findViewById(R.id.radioDate);
        radioSubject = findViewById(R.id.radioSubject);

        // ImageButton click events
        imageButtonList.setOnClickListener(v -> showMemoList());
        imageButtonSettings.setOnClickListener(v -> showSettings());

        // RadioGroup change event
        radioGroupSortBy.setOnCheckedChangeListener((group, checkedId) -> {
            String criteria = "Priority"; // Default to "Priority"
            if (checkedId == R.id.radioPriority) {
                criteria = "Priority";
            } else if (checkedId == R.id.radioDate) {
                criteria = "Due Date";
            } else if (checkedId == R.id.radioSubject) {
                criteria = "Subject";
            }
            saveSortingCriteria(criteria);
            sortMemosBy(criteria);
        });

        // Load saved sorting preference
        loadSortingCriteria();
    }

    private void showMemoList() {
        Intent intent = new Intent(MemoSettingsActivity.this, MemoListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showSettings() {
        Toast.makeText(this, "Showing settings...", Toast.LENGTH_SHORT).show();
    }

    private void saveSortingCriteria(String criteria) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString("SORTING_CRITERIA", criteria)
                .apply();
    }

    private void loadSortingCriteria() {
        String criteria = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("SORTING_CRITERIA", "Priority"); // Default to "Priority"
        if ("Priority".equals(criteria)) {
            radioPriority.setChecked(true);
        } else if ("Due Date".equals(criteria)) {
            radioDate.setChecked(true);
        } else if ("Subject".equals(criteria)) {
            radioSubject.setChecked(true);
        }
    }

    private void sortMemosBy(String criteria) {
        Toast.makeText(this, "Sorting memos by: " + criteria, Toast.LENGTH_SHORT).show();
        // No need to implement actual sorting logic here, as it will be handled in MemoListActivity
    }
}