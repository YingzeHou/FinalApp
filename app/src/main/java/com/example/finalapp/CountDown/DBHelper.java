package com.example.finalapp.CountDown;

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
        c.moveToFirst();
        ArrayList<Todo> todosList = new ArrayList<>();
        while (!c.isAfterLast()) {
            String date = c.getString(dateIndex);
            String content = c.getString(contentIndex);
            String past = "true";
            Todo todo = new Todo(content, date, past);
            todosList.add(todo);
            c.moveToNext();
        }
        c.close();
        sqLiteDatabase.close();
        return todosList;
    }

    public void saveTodos(String content, String date) {
        createTable();
        sqLiteDatabase.execSQL(String.format("INSERT INTO todos (content, date) " +
                " VALUES ('%s', '%s')", content, date));
    }

    public void clearDatabase() {
        createTable();
        sqLiteDatabase.execSQL(String.format("DELETE FROM todos"));
    }
}
