package com.example.finalapp.Calendar.dao;

public class CountdownEvent implements Comparable<CountdownEvent> {

    private String eventName;
    private String date;
    private boolean past;   //true if the event is in the past, false if the event is in the future

    public CountdownEvent(String eventName, String date, boolean past) {
        this.eventName = eventName;
        this.date = date;
        this.past = past;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getPast() {
        return past;
    }

    public void setPast(boolean past){
        this.past = past;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", date='" + date + '\'' +
                ", past='" + past + '\'' +
                '}';
    }

    @Override
    public int compareTo(CountdownEvent o) {
        if(o.past == true){
            return 1;
        }
        else{
            return 0;
        }
    }
}
