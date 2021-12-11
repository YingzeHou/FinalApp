package com.example.finalapp.Reminder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.finalapp.Calendar.CalendarFrag;
import com.example.finalapp.Calendar.dao.Event;
import com.example.finalapp.MainActivity;
import com.example.finalapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EditTodoFrag extends Fragment {

    public EditTodoFrag() {
        // Required empty public constructor
    }

    public String locationLat;
    public String locationLon;
    TextView selectDate;
    TextView selectTime;
    TextView editLocation;
    EditText content;
    TextView saveTodo;
    TextView deleteTodo;
    int hour, minute;
    int year, month, dayOfMonth;
    Calendar c;
    int todoId;
    Todo todo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_todo, container, false);
        ImageButton goToReminder = (ImageButton) view.findViewById(R.id.goToReminder2);

        Bundle bundle = this.getArguments();
        todoId = bundle.getInt("todoId");

        Context context = this.getContext().getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("todos", Context.MODE_PRIVATE,null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        ArrayList<Todo> todos = dbHelper.readTodos();
        todo = todos.get(todoId);

        content = (EditText) view.findViewById(R.id.editContent2);
        content.setText(todo.getContent());
        saveTodo = (TextView) view.findViewById(R.id.saveTodo2);
        deleteTodo = (TextView) view.findViewById(R.id.deleteTodo);
        selectDate = (TextView) view.findViewById(R.id.selectDate2);
        selectDate.setText(todo.getDate());
        selectTime = (TextView) view.findViewById(R.id.selectTime2);
        selectTime.setText(todo.getTime());
        editLocation = (TextView) view.findViewById(R.id.editLocation);
        if (todo.getLocation().contains(","))
            editLocation.setText(todo.getLocation().split(",")[0]);
        else
            editLocation.setText(todo.getLocation());

        if (todo.getIsEvent().compareTo("true") == 0) {
            TextView editTodoPageTitle = (TextView) view.findViewById(R.id.editTodoPageTitle);
            editTodoPageTitle.setText("View This Event");
            ((ViewManager) saveTodo.getParent()).removeView(saveTodo);
            ((ViewManager) deleteTodo.getParent()).removeView(deleteTodo);
            for (int i = 0; i < todos.size(); i++) {
                if (todos.get(i).getContent().compareTo(todo.getContent()) == 0 && todos.get(i).getDate().compareTo(todo.getDate()) > 0)
                    selectDate.setText(todos.get(i).getDate());
            }
        }

        goToReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.clearFocus();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
                Fragment fragment = new ReminderFrag();
                fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
            }
        });


        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSelectDate(view);
            }
        });
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSelectTime(view);
            }
        });
        saveTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateTodo(view);
            }
        });
        deleteTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder deleteConfirmation = new AlertDialog.Builder(getContext());
                deleteConfirmation.setTitle("Do you want to delete this todo?")
                        .setMessage("Delete Confirmation")
                        .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onClickDeleteTodo(view);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                deleteConfirmation.create().show();
            }
        });
        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChooseLocation(view);
            }
        });
        return view;
    }

    public void onClickChooseLocation(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("type", "edit");
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
        fragmentTransaction.addToBackStack(null);
        Fragment fragment = new com.example.finalapp.Reminder.ChooseLocFrag();
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.nav_fragment,fragment).commit();
    }

    public void onClickSelectDate(View view) {
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                year = selectedYear;
                month = selectedMonth + 1;
                dayOfMonth = selectedDayOfMonth;
                selectDate.setText(String.format("%02d/%02d/%02d", year, month, dayOfMonth));
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), style, onDateSetListener, year, month, dayOfMonth);
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
    }

    public void onClickSelectTime(View view) {
        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                selectTime.setText(String.format("%02d:%02d", hour, minute));
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), style, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void onClickUpdateTodo(View view) {
        Context context = this.getContext().getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("todos", Context.MODE_PRIVATE,null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        if(TextUtils.isEmpty(content.getText())) {
            content.setError("Please Input Content of Todo");
            return;
        }
        dbHelper.updateTodo(content.getText().toString(), selectDate.getText().toString(), selectTime.getText().toString(),
                editLocation.getText().toString() + "," + locationLat + "," + locationLon, todo.getId());
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
        Fragment fragment = new ReminderFrag();
        fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
    }

    public void onClickDeleteTodo(View view) {
        Context context = this.getContext().getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("todos", Context.MODE_PRIVATE,null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        dbHelper.deleteTodo(todo.getId());
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
        Fragment fragment = new ReminderFrag();
        fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
    }
}