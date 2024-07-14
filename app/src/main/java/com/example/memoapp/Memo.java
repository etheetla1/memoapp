package com.example.memoapp;

public class Memo {
    private int id;
    private String title;
    private String text;
    private String priority;
    private String date;

    public Memo(int id, String title, String priority, String date) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return title;
    }

    public String getPriority() {
        return priority;
    }

    public String getDate() {
        return date;
    }
}