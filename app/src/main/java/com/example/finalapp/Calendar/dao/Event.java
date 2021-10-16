package com.example.finalapp.Calendar.dao;

public class Event {

    private String eventName;
    private String colorCode;
    private String note;
    private String startTime;
    private String endTime;
    private String participant;
    private String location;

    public Event(String eventName, String colorCode, String note, String startTime, String endTime, String participant, String location) {
        this.eventName = eventName;
        this.colorCode = colorCode;
        this.note = note;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participant = participant;
        this.location = location;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", note='" + note + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", participant='" + participant + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
