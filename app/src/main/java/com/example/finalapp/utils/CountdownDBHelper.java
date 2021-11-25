package com.example.finalapp.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finalapp.Calendar.dao.CountdownEvent;
import com.example.finalapp.Calendar.dao.Event;

import java.util.ArrayList;
import java.util.List;

public class CountdownDBHelper {

    SQLiteDatabase sqLiteDatabase;
    public CountdownDBHelper(SQLiteDatabase sqLiteDatabase){
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public void createCountdownTable(){
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS countdown"+
                "(id INTEGER PRIMARY KEY, name TEXT, days INTEGER, weekDay INTEGER, past INTEGER)");
    }

    public ArrayList<CountdownEvent> readCountdownEvents(){
        createCountdownTable();

        Cursor c = sqLiteDatabase.rawQuery(String.format("SELECT * FROM countdown"),null);

        int nameInd = c.getColumnIndex("eventName");
        int daysInd = c.getColumnIndex("days");
        int weekDayInd = c.getColumnIndex("weekDay");
        int pastInd = c.getColumnIndex("past");

        c.moveToFirst();
        ArrayList<CountdownEvent> eventList = new ArrayList<>();

        while(!c.isAfterLast()){
            String eName = c.getString(nameInd);
            int eDays = c.getInt(daysInd);
            int eWeekDay = c.getInt(weekDayInd);
            int ePast = c.getInt(pastInd);

            CountdownEvent event = new CountdownEvent(eName,eDays,eWeekDay,ePast);
            eventList.add(event);
            c.moveToNext();
        }
        c.close();
        sqLiteDatabase.close();
        return eventList;
    }

    public void saveCountdownEvent(String name, int days, int weekDay, int past){
        createCountdownTable();
        sqLiteDatabase.execSQL(String.format("INSERT INTO countdown (eventName, days, weekDay, past)"+
                "VALUES('%s', '%d', '%d', '%d')", name,days,weekDay,past));
    }

//    public void deleteCountdownEvent(String name, int days, int weekDay, int past){
//        createCountdownTable();
//        sqLiteDatabase.execSQL(String.format("DELETE FROM events WHERE );
//    }

//    public List<Event> selectCountdownEvent(String name, String start, String end){
//        createCountdownTable();
//        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM events WHERE eventName=? AND startTime=? AND endTime=?",new String[]{name,start,end});
//        c.moveToFirst();
//        List<Event> eventList = new ArrayList<>();
//        while(!c.isAfterLast()) {
//            int colorInd = c.getColumnIndex("colorCode");
//            int dayInd = c.getColumnIndex("weekDay");
//            int noteInd = c.getColumnIndex("note");
//            int partInd = c.getColumnIndex("participant");
//            int locInd = c.getColumnIndex("location");
//            int roomInd = c.getColumnIndex("room");
//
//            String colorCode = c.getString(colorInd);
//            int weekDay = c.getInt(dayInd);
//            String note = c.getString(noteInd);
//            String participant = c.getString(partInd);
//            String location = c.getString(locInd);
//            String room = c.getString(roomInd);
//
//            eventList.add(new Event(name,colorCode,weekDay,note,start,end,participant,location, room));
//            c.moveToNext();
//        }
//
//        return eventList;
//    }
}
