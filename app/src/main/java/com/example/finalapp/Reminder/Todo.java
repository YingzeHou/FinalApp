package com.example.finalapp.Reminder;

public class Todo {

    private String content;
    private String date;
    private String time;
    private String location;
    private String id;
    private String isEvent;

    public Todo(String content, String date, String time, String location, String isEvent, String id) {
        this.date = date;
        this.time = time;
        this.content = content;
        this.id = id;
        this.location = location;
        this.isEvent = isEvent;
    }

    public String getIsEvent() { return isEvent; }

    public String getId() { return id; }

    public String getDate() { return date; }

    public String getTime() { return time; }

    public String getContent() { return content; }

    public String getLocation() { return location; }
}
