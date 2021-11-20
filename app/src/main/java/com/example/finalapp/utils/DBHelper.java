package com.example.finalapp.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finalapp.Calendar.dao.Event;

import java.util.ArrayList;
import java.util.List;

public class DBHelper {

    SQLiteDatabase sqLiteDatabase;
    public DBHelper(SQLiteDatabase sqLiteDatabase){
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public void createTable(){
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS events"+
                "(id INTEGER PRIMARY KEY, eventName TEXT, colorCode TEXT, weekDay INTEGER, note TEXT, " +
                "startTime TEXT, endTime TEXT, participant TEXT, location TEXT, room TEXT, alarmId INTEGER)");
    }

    public ArrayList<Event> readEvents(){
        createTable();
        Cursor c = sqLiteDatabase.rawQuery(String.format("SELECT * FROM events"),null);

        int nameInd = c.getColumnIndex("eventName");
        int colorInd = c.getColumnIndex("colorCode");
        int dayInd = c.getColumnIndex("weekDay");
        int noteInd = c.getColumnIndex("note");
        int startInd = c.getColumnIndex("startTime");
        int endInd = c.getColumnIndex("endTime");
        int partInd = c.getColumnIndex("participant");
        int locInd = c.getColumnIndex("location");
        int roomInd = c.getColumnIndex("room");
        int alarmInd = c.getColumnIndex("alarmId");

        c.moveToFirst();
        ArrayList<Event> eventList = new ArrayList<>();

        while(!c.isAfterLast()){
            String eName = c.getString(nameInd);
            String eColor = c.getString(colorInd);
            int eDay = c.getInt(dayInd);
            String eNote = c.getString(noteInd);
            String eStart = c.getString(startInd);
            String eEnd = c.getString(endInd);
            String ePart = c.getString(partInd);
            String eLoc = c.getString(locInd);
            String eRoom = c.getString(roomInd);
            int eAlarm = c.getInt(alarmInd);

            Event event = new Event(eName,eColor,eDay,eNote,eStart,eEnd,ePart,eLoc, eRoom, eAlarm);
            eventList.add(event);
            c.moveToNext();
        }
        c.close();
        sqLiteDatabase.close();
        return eventList;
    }

    public void saveEvent(String name, String color, String note, int day, String start, String end, String part, String loc, String room, int alarmId){
        createTable();
        sqLiteDatabase.execSQL(String.format("INSERT INTO events (eventName, colorCode, weekDay, note, startTime, endTime, participant, location, room, alarmId)"+
                "VALUES('%s', '%s', '%d', '%s', '%s', '%s', '%s', '%s', '%s', '%d')", name,color,day,note,start,end,part,loc, room, alarmId));
    }

    public void deleteEvent(String name, String start, String end){
        createTable();
        sqLiteDatabase.execSQL(String.format("DELETE FROM events WHERE eventName='%s' AND startTime='%s' AND endTime='%s'",
                name, start, end));
    }

    public List<Event> selectEvent(String name, String start, String end){
        createTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM events WHERE eventName=? AND startTime=? AND endTime=?",new String[]{name,start,end});
        c.moveToFirst();
        List<Event> eventList = new ArrayList<>();
        while(!c.isAfterLast()) {
            int colorInd = c.getColumnIndex("colorCode");
            int dayInd = c.getColumnIndex("weekDay");
            int noteInd = c.getColumnIndex("note");
            int partInd = c.getColumnIndex("participant");
            int locInd = c.getColumnIndex("location");
            int roomInd = c.getColumnIndex("room");
            int alarmInd = c.getColumnIndex("alarmId");

            String colorCode = c.getString(colorInd);
            int weekDay = c.getInt(dayInd);
            String note = c.getString(noteInd);
            String participant = c.getString(partInd);
            String location = c.getString(locInd);
            String room = c.getString(roomInd);
            int alarm = c.getInt(alarmInd);

            eventList.add(new Event(name,colorCode,weekDay,note,start,end,participant,location, room, alarm));
            c.moveToNext();
        }

        return eventList;
    }
}
