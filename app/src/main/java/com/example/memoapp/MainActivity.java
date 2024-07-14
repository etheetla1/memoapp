package com.example.memoapp;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editMemo;
    private EditText editMemoTitleText;
    private EditText editDate;
    private ToggleButton toggleButtonEdit;
    private Button buttonSave;
    private RadioGroup radioGroupPriority;
    private RadioButton radioPriority;
    private RadioButton radioMedium;
    private RadioButton radioLow;
    private ImageButton imageButtonList;
    private ImageButton imageButtonSettings;

    private MemoDBHelper dbHelper;
    private Memo currentMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editMemo = findViewById(R.id.editMemo);
        editMemoTitleText = findViewById(R.id.editMemoTitleText);
        editDate = findViewById(R.id.editDate);
        toggleButtonEdit = findViewById(R.id.toggleButtonEdit);
        buttonSave = findViewById(R.id.buttonSave);
        radioGroupPriority = findViewById(R.id.radioGroupPriority);
        radioPriority = findViewById(R.id.radioButton);
        radioMedium = findViewById(R.id.radioButton2);
        radioLow = findViewById(R.id.radioButton3);
        imageButtonList = findViewById(R.id.imageButtonList);
        imageButtonSettings = findViewById(R.id.imageButtonSettings);

        dbHelper = new MemoDBHelper(this);

        toggleButtonEdit.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editMemo.setEnabled(isChecked);
            editMemoTitleText.setEnabled(isChecked);
            editDate.setEnabled(isChecked);
            radioGroupPriority.setEnabled(isChecked);
            radioPriority.setEnabled(isChecked);
            radioMedium.setEnabled(isChecked);
            radioLow.setEnabled(isChecked);
        });

        buttonSave.setOnClickListener(v -> saveMemo());

        imageButtonList.setOnClickListener(v -> showMemoList());
        imageButtonSettings.setOnClickListener(v -> showSettings());

        editDate.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    calendar.set(year1, month1, dayOfMonth);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    editDate.setText(format.format(calendar.getTime()));
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void saveMemo() {
        String memoText = editMemo.getText().toString();
        String memoTitle = editMemoTitleText.getText().toString();
        String priority = getSelectedPriority();
        String date = editDate.getText().toString();

        if (memoText.isEmpty() || memoTitle.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please enter a memo title, memo text, and date.", Toast.LENGTH_SHORT).show();
        } else {
            boolean wasSuccessful;
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("memo_text", memoTitle);
            values.put("priority", priority);
            values.put("date", date);

            try {
                if (currentMemo == null) {
                    long newRowId = db.insert("memo", null, values);
                    wasSuccessful = newRowId != -1;
                    if (wasSuccessful) {
                        currentMemo = new Memo((int) newRowId, memoTitle, priority, date);
                    }
                } else {
                    int rowsAffected = db.update("memo", values, "_id = ?", new String[]{String.valueOf(currentMemo.getId())});
                    wasSuccessful = rowsAffected > 0;
                }
            } catch (Exception e) {
                wasSuccessful = false;
            }

            if (wasSuccessful) {
                toggleButtonEdit.toggle();
                setForEditing(false);
                if (currentMemo == null) {
                    Toast.makeText(this, "New memo saved with priority: " + priority, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Memo updated with priority: " + priority, Toast.LENGTH_SHORT).show();
                }
                loadMemos();
            }
        }
    }

    private String getSelectedPriority() {
        int selectedId = radioGroupPriority.getCheckedRadioButtonId();
        if (selectedId == radioPriority.getId()) {
            return "High";
        } else if (selectedId == radioMedium.getId()) {
            return "Medium";
        } else if (selectedId == radioLow.getId()) {
            return "Low";
        } else {
            return "None";
        }
    }

    private void showMemoList() {
        Intent intent = new Intent(MainActivity.this, MemoListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showSettings() {
        Intent intent = new Intent(MainActivity.this, MemoSettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void loadMemos() {
        // Load and sort memos based on priority, or other criteria as needed
    }

    private void setForEditing(boolean enabled) {
        editMemo.setEnabled(enabled);
        editMemoTitleText.setEnabled(enabled);
        editDate.setEnabled(enabled);
        radioGroupPriority.setEnabled(enabled);
        radioPriority.setEnabled(enabled);
        radioMedium.setEnabled(enabled);
        radioLow.setEnabled(enabled);
    }
}