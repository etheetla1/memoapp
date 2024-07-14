package com.example.memoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {

    private Context context;
    private List<Memo> memoList;
    private boolean deleteMode = false;

    public MemoAdapter(Context context, List<Memo> memoList) {
        this.context = context;
        this.memoList = memoList;
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.memo_item, parent, false);
        return new MemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {
        Memo memo = memoList.get(position);
        holder.memoTextView.setText(memo.getText());
        holder.priorityTextView.setText(memo.getPriority());
        holder.dateTextView.setText(memo.getDate());

        holder.itemView.setOnClickListener(v -> {
            if (deleteMode) {
                deleteMemo(position);
            } else {
                Toast.makeText(context, "Memo clicked: " + memo.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    public void setMemoList(List<Memo> memoList) {
        this.memoList = memoList;
        notifyDataSetChanged();
    }

    public void setDeleteMode(boolean deleteMode) {
        this.deleteMode = deleteMode;
    }

    private void deleteMemo(int position) {
        Memo memo = memoList.get(position);
        MemoDBHelper dbHelper = new MemoDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = db.delete("memo", "_id = ?", new String[]{String.valueOf(memo.getId())});
        db.close();

        if (deletedRows > 0) {
            memoList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Memo deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error deleting memo", Toast.LENGTH_SHORT).show();
        }
    }

    public static class MemoViewHolder extends RecyclerView.ViewHolder {
        TextView memoTextView, priorityTextView, dateTextView;

        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            memoTextView = itemView.findViewById(R.id.memoTextView);
            priorityTextView = itemView.findViewById(R.id.priorityTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}