package com.example.finalapp.CountDown;

public class TimeLineModel {
    private String name;
    private String status;
    private String description;
    private String date;
    private String address;
    private String past;

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

    String getDate(){ return date;}

    void setDate(String date) { this.date = date; }

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

    public String getPast() {
        return past;
    }

    public void setPast(String past) {
        this.past = past;
    }
}
