package com.example.finalapp;

public class Todo {

    private String content;
    private String date;
    private String time;

    public Todo(String content, String date, String time) {
        this.date = date;
        this.time = time;
        this.content = content;
    }

    public String getDate() { return date; }

    public String getTime() { return time; }

    public String getContent() { return content; }
}
