package com.example.finalapp.Reminder;

public class TimeLineModel {
    private String name;
    private String status;
    private String description;
    private String address;

    String getTime() {
        return time;
    }

    void setTime(String time) {
        this.time = time;
    }

    private String time;

    String getDescription() {
        return description;
    }

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
}
