package com.example.finalapp.CountDown;

public class Todo {

    private String content;
    private String date;

    public Todo(String content, String date, String time) {
        this.date = date;
        this.content = content;
    }

    public String getDate() { return date; }

    public String getContent() { return content; }
}
