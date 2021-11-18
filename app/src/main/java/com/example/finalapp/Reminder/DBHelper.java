package com.example.finalapp.Reminder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBHelper {

    SQLiteDatabase sqLiteDatabase;

    public DBHelper(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public void createTable() {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS todos " +
                "(id INTEGER PRIMARY KEY, content TEXT, date TEXT, time TEXT)");
    }

    public ArrayList<Todo> readTodos() {
        createTable();
        Cursor c = sqLiteDatabase.rawQuery(String.format("SELECT * FROM todos"), null);
        int contentIndex = c.getColumnIndex("content");
        int dateIndex = c.getColumnIndex("date");
        int timeIndex = c.getColumnIndex("time");
        c.moveToFirst();
        ArrayList<Todo> todosList = new ArrayList<>();
        while (!c.isAfterLast()) {
            String time = c.getString(timeIndex);
            String date = c.getString(dateIndex);
            String content = c.getString(contentIndex);
            Todo todo = new Todo(content, date, time);
            todosList.add(todo);
            c.moveToNext();
        }
        c.close();
        sqLiteDatabase.close();
        return todosList;
    }

    public void saveTodos(String content, String date, String time) {
        createTable();
        sqLiteDatabase.execSQL(String.format("INSERT INTO todos (content, date, time) " +
                " VALUES ('%s', '%s', '%s')", content, date, time));
    }

//    public void updateTodo(String content, String date, String time) {
//        createTable();
//        sqLiteDatabase.execSQL(String.format("UPDATE todos SET content = '%s', date = '%s', " +
//                        "time = '%s' WHERE content = '%s' and username = '%s'",
//                 date, content, title, username));
//    }

    public void clearDatabase() {
        createTable();
        sqLiteDatabase.execSQL(String.format("DELETE FROM todos"));
    }
}
