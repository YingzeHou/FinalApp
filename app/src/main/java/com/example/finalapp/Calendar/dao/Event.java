package com.example.finalapp.Calendar.dao;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event implements Comparable<Event> {

    private String eventName;
    private String colorCode;
    private int weekDay;
    private String note;
    private String startTime;
    private String endTime;
    private String participant;
    private String location;
    private String room;
    private int alarmId;

    public Event(String eventName, String colorCode, int weekDay, String note, String startTime,
                 String endTime, String participant, String location, String room, int alarmId) {
        this.eventName = eventName;
        this.colorCode = colorCode;
        this.weekDay = weekDay;
        this.note = note;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participant = participant;
        this.location = location;
        this.room = room;
        this.alarmId = alarmId;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", weekDay=" + weekDay +
                ", note='" + note + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", participant='" + participant + '\'' +
                ", location='" + location + '\'' +
                ", room='" + room + '\'' +
                '}';
    }

    @Override
    public int compareTo(Event o) {
        if(Integer.parseInt(this.getStartTime().split(":")[0])>=Integer.parseInt(o.getStartTime().split(":")[0])){
            return 1;
        }
        return -1;
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//        Date d1 = null;
//        try {
//            d1 = sdf.parse(this.getStartTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Date d2 = null;
//        try {
//            d2 = sdf.parse(this.getEndTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        int elapsed = (int) d2.getTime() - (int)d1.getTime();
//
//        return elapsed;

    }
}
