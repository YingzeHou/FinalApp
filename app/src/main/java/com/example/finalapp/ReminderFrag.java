package com.example.finalapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.finalapp.Calendar.CalAddEventFrag;

import java.util.ArrayList;

public class ReminderFrag extends Fragment {

    public ReminderFrag() {
        // Required empty public constructor
    }


    ArrayList<Todo> todos = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        Todo a = new Todo("Write some code", "2021/11/8", "14:00");
        todos.add(a);
        todos.add(a);
        todos.add(a);
        todos.add(a);
        todos.add(a);
        ArrayList<String> displayNotes = new ArrayList<>();
        for (Todo todo : todos) {
            displayNotes.add(String.format("%s\n%s", todo.getTime(), todo.getDate()));
        }

        ArrayAdapter adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_activated_1, displayNotes);
        ListView listView = (ListView) view.findViewById(R.id.notesListView);
        listView.setAdapter(adapter);

        ImageButton addToDoButton = view.findViewById(R.id.addTodoButton);

        addToDoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
                Fragment fragment = new AddToDoFrag();
                fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
            }
        });

        return view;
    }

}