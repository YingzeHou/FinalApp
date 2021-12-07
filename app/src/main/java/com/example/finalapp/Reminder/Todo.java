package com.example.finalapp.Reminder;

public class Todo {

    private String content;
    private String date;
    private String time;
    private String id;

    public Todo(String content, String date, String time, String id) {
        this.date = date;
        this.time = time;
        this.content = content;
        this.id = id;
    }

    public String getId() { return id; }

    public String getDate() { return date; }

    public String getTime() { return time; }

    public String getContent() { return content; }
}
