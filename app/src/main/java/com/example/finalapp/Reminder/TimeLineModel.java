package com.example.finalapp.Reminder;

public class TimeLineModel {
    private String name;
    private String status;
    private String description;
    private String address;
    private String time;
    private String date;
    private String isEvent;

    String getTime() {
        return time;
    }

    void setTime(String time) {
        this.time = time;
    }

    String getDate() {
        return date;
    }

    void setDate(String date) {
        this.date = date;
    }

    String getDescription() { return description; }

    void setDescription(String description) {
        this.description = description;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsEvent() { return isEvent; }

    public void setIsEvent(String isEvent) { this.isEvent = isEvent; }
}
