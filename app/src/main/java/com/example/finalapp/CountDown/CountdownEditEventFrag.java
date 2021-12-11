package com.example.finalapp.CountDown;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalapp.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CountdownEditEventFrag#} factory method to
 * create an instance of this fragment.
 */
public class CountdownEditEventFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Calendar c;
    Button selectDate;
    int year, month, dayOfMonth;
    private int mDefaultColor;
    private boolean update=false;
    EditText content;
    EditText past;
    String eventName;
    String eventDate;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CountdownEditEventFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            eventName = bundle.getString("event", null);
            eventDate = bundle.getString("date", null);
            SQLiteDatabase sqLiteDatabase = getContext().openOrCreateDatabase("events", Context.MODE_PRIVATE,null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);
            dbHelper.deleteTodos(eventName);
        }

        View view = inflater.inflate(R.layout.fragment_countdown_edit_event, container, false);
        ImageButton goBackBtn = view.findViewById(R.id.goBackBtn);
        mDefaultColor=0;
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
                Fragment fragment = new CountdownFrag();
                fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
            }
        });

        content = (EditText) view.findViewById(R.id.editContent);
        selectDate = (Button) view.findViewById(R.id.selectDate);
        content.setText(eventName);
        selectDate.setText(eventDate);

        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSaveEvent(v);
            }
        });

        Button deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDeleteEvent(v);
            }
        });

        c = Calendar.getInstance();
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSelectDate(view);
            }
        });
        return view;
    }


    public void onClickSelectDate(View view) {
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                year = selectedYear;
                month = selectedMonth + 1;
                dayOfMonth = selectedDayOfMonth;
                selectDate.setText(String.format("%02d/%02d/%02d", month, dayOfMonth, year));
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), style, onDateSetListener, year, month, dayOfMonth);
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
    }

    public void onClickSaveEvent(View view) {
        if (!selectDate.getText().toString().equalsIgnoreCase("select date") && !content.getText().toString().equalsIgnoreCase("")){
            Context context = this.getContext().getApplicationContext();
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("events", Context.MODE_PRIVATE,null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);
            dbHelper.saveTodos(content.getText().toString(), selectDate.getText().toString());

            Toast.makeText(content.getContext(), content.getText().toString() + " has been added", Toast.LENGTH_LONG).show();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
            Fragment fragment = new CountdownFrag();
            fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
        }

        else if (content.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(content.getContext(), "Please name the event first", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(content.getContext(), "Please select a date", Toast.LENGTH_LONG).show();
        }

    }

    public void onClickDeleteEvent(View view) {
        Context context = this.getContext().getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("events", Context.MODE_PRIVATE,null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        dbHelper.deleteTodos(content.getText().toString());

        Toast.makeText(content.getContext(), content.getText().toString() + " has been deleted", Toast.LENGTH_LONG).show();
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
        Fragment fragment = new CountdownFrag();
        fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
    }

}