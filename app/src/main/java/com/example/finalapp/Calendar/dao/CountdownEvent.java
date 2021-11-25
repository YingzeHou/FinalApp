package com.example.finalapp.Calendar.dao;

public class CountdownEvent implements Comparable<CountdownEvent> {

    private String name;
    private int days;
    private int weekDay;
    private int past;   //true if the event is in the past, false if the event is in the future

    public CountdownEvent(String eventName, int days, int weekDay, int past) {
        this.name = eventName;
        this.days = days;
        this.weekDay = weekDay;
        this.past = past;
    }

    public String getEventName() {
        return name;
    }

    public void setEventName(String eventName) {
        this.name = eventName;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getPast() {
        return past;
    }

    public void setPast(int past){
        this.past = past;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + name + '\'' +
                ", days='" + days + '\'' +
                ", past='" + past + '\'' +
                '}';
    }

    @Override
    public int compareTo(CountdownEvent countdownEvent) {
        return 0;
    }
}
