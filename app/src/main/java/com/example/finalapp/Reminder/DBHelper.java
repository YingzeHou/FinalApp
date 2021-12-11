package com.example.finalapp.Reminder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.finalapp.Calendar.dao.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class DBHelper {

    SQLiteDatabase sqLiteDatabase;

    public DBHelper(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public void createTable() {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS todos " +
                "(id INTEGER PRIMARY KEY, content TEXT, date TEXT, time TEXT, location TEXT, isEvent TEXT, todoId TEXT)");
    }

    public ArrayList<Todo> readTodos() {
//        clearDatabase();
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS todos");
        createTable();
        Cursor c = sqLiteDatabase.rawQuery(String.format("SELECT * FROM todos"), null);
        int contentIndex = c.getColumnIndex("content");
        int dateIndex = c.getColumnIndex("date");
        int timeIndex = c.getColumnIndex("time");
        int locationIndex = c.getColumnIndex("location");
        int idIndex = c.getColumnIndex("todoId");
        int isEventIndex = c.getColumnIndex("isEvent");
        c.moveToFirst();
        ArrayList<Todo> todosList = new ArrayList<>();
        LinkedList<Todo> sortedTodos = new LinkedList<>();
        while (!c.isAfterLast()) {
            String time = c.getString(timeIndex);
            String date = c.getString(dateIndex);
            String content = c.getString(contentIndex);
            String location = c.getString(locationIndex);
            String isEvent = c.getString(isEventIndex);
            if (time.compareTo("blank") == 0 && date.compareTo("blank") == 0 && content.compareTo("blank") == 0) {
                c.moveToNext();
                continue;
            }
            String id = c.getString(idIndex);
            Todo todo = new Todo(content, date, time, location, isEvent, id);
            if (sortedTodos.size() == 0)
                sortedTodos.add(todo);
            else {
                String toAddDateAndTime = todo.getDate() + todo.getTime();
                int listSize = sortedTodos.size();
                for (int i = 0; i < listSize; i++) {
                    String curDateAndTime = sortedTodos.get(i).getDate() + sortedTodos.get(i).getTime();
                    if (toAddDateAndTime.compareTo(curDateAndTime) < 0) {
                        sortedTodos.add(i, todo);
                        break;
                    }
                    if (i == listSize - 1)
                        sortedTodos.add(todo);
                }
            }
            c.moveToNext();
        }
        c.close();
//        sqLiteDatabase.close();
        for (int i = 0; i < sortedTodos.size(); i++) {
            todosList.add(sortedTodos.get(i));
        }
        return todosList;
    }

    public boolean findEventInTodoDatabase(Event event, String[] days) {
        createTable();
        Cursor c = sqLiteDatabase.rawQuery(String.format("SELECT * FROM todos WHERE isEvent = 'true'"), null);
        int contentIndex = c.getColumnIndex("content");
        int dateIndex = c.getColumnIndex("date");
        int timeIndex = c.getColumnIndex("time");
        int locationIndex = c.getColumnIndex("location");
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String time = c.getString(timeIndex);
            String date = c.getString(dateIndex);
            String content = c.getString(contentIndex);
            String location = c.getString(locationIndex);
            if (event.getEventName().compareTo(content) == 0 && event.getLocation().compareTo(location) == 0
                && event.getStartTime().compareTo(time) == 0 && String.valueOf(days[event.getWeekDay() - 1]).compareTo(date) == 0) {
                c.close();
                return true;
            }
            c.moveToNext();
        }
        c.close();
        return false;
    }

    public void deleteEventsNotExist(ArrayList<Event> events, String[] days) {
        createTable();
        Cursor c = sqLiteDatabase.rawQuery(String.format("SELECT * FROM todos WHERE isEvent = 'true'"), null);
        int contentIndex = c.getColumnIndex("content");
        int dateIndex = c.getColumnIndex("date");
        int timeIndex = c.getColumnIndex("time");
        int locationIndex = c.getColumnIndex("location");
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String time = c.getString(timeIndex);
            String date = c.getString(dateIndex);
            String content = c.getString(contentIndex);
            String location = c.getString(locationIndex);
            boolean toDelete = true;
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                if (event.getEventName().compareTo(content) == 0 && event.getLocation().compareTo(location) == 0
                        && event.getStartTime().compareTo(time) == 0 && String.valueOf(days[event.getWeekDay() - 1]).compareTo(date) == 0) {
                    toDelete = false;
                }
            }
            if (toDelete)
                sqLiteDatabase.execSQL(String.format("UPDATE todos SET content = 'blank', date = 'blank', " +
                        "time = 'blank', location = 'blank' WHERE content = '%s' AND date = '%s' AND time = '%s' AND location = '%s'", content, date, time, location));
            c.moveToNext();
        }
        c.close();
    }

    public void saveTodos(String content, String date, String time, String location, String isEvent, String todoId) {
        createTable();
        sqLiteDatabase.execSQL(String.format("INSERT INTO todos (content, date, time, location, isEvent, todoId) " +
                " VALUES ('%s', '%s', '%s', '%s', '%s', '%s')", content, date, time, location, isEvent, todoId));
    }

    public void updateTodo(String content, String date, String time, String location, String todoId) {
        createTable();
        sqLiteDatabase.execSQL(String.format("UPDATE todos SET content = '%s', date = '%s', " +
                        "time = '%s', location = '%s' WHERE id = '%s'",
                 content, date, time, location, todoId));
    }

    public void deleteTodo(String todoId) {
        createTable();
        sqLiteDatabase.execSQL(String.format("UPDATE todos SET content = 'blank', date = 'blank', " +
                        "time = 'blank', location = 'blank' WHERE id = '%s'", todoId));
    }

    public void clearDatabase() {
        createTable();
        sqLiteDatabase.execSQL(String.format("DELETE FROM todos"));
    }

}
