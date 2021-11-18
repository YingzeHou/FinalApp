package com.example.finalapp.Reminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.finalapp.R;

import java.util.ArrayList;
import java.util.List;

public class ReminderFrag extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] myDataset;
    private MyAdapter.MyViewHolder[] viewHolders;

    public static ArrayList<Todo> todos = new ArrayList<>();

    private RecyclerView timeLineRecyclerView;
    String[] name = {"Event 1", "Event 2", "Event 3"};
    String[] status = {"active", "inactive", "inactive"};
    String[] description = {"Description 1","Description 2","Description 3"};
    String[] time = {"11:00 PM", "10:03 AM", "10:03 PM"};

    List<TimeLineModel> timeLineModelList;
    TimeLineModel[] timeLineModel;
    Context context;
    LinearLayoutManager linearLayoutManager;

    public ReminderFrag() {
        // Required empty public constructor
    }

//    ArrayList<Todo> todos = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
//        Todo a = new Todo("Write some code", "2021/11/8", "14:00");
//        todos.add(a);
//        todos.add(a);
//        todos.add(a);
//        todos.add(a);
//        todos.add(a);
//        ArrayList<String> displayNotes = new ArrayList<>();
//        for (Todo todo : todos) {
//            displayNotes.add(String.format("%s\n%s", todo.getTime(), todo.getDate()));
//        }
//
//        ArrayAdapter adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_activated_1, displayNotes);
//        ListView listView = (ListView) view.findViewById(R.id.notesListView);
//        listView.setAdapter(adapter);


        timeLineModelList = new ArrayList<>();
        int size = name.length;
        timeLineModel = new TimeLineModel[size];
        context = this.getContext();
        linearLayoutManager = new LinearLayoutManager(this.getContext());

        for (int i = 0; i < size; i++) {
            timeLineModel[i] = new TimeLineModel();
            timeLineModel[i].setName(name[i]);
            timeLineModel[i].setStatus(status[i]);
            timeLineModel[i].setDescription(description[i]);
            timeLineModel[i].setTime(time[i]);
            timeLineModel[i].setAddress("CS407\n@CS Building 1240");
            timeLineModelList.add(timeLineModel[i]);
        }
        timeLineRecyclerView = (RecyclerView) view.findViewById(R.id.timeLineView);
        timeLineRecyclerView.setLayoutManager(linearLayoutManager);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(timeLineRecyclerView.getContext(), linearLayoutManager.getOrientation());
//        timeLineRecyclerView.addItemDecoration(dividerItemDecoration);  //for divider
        timeLineRecyclerView.setAdapter(new TimeLineAdapter(context, timeLineModelList));


        Context context = this.getContext().getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("todos", Context.MODE_PRIVATE,null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
//        dbHelper.clearDatabase();
        todos = dbHelper.readTodos();

        recyclerView = (RecyclerView) view.findViewById(R.id.notesListView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        myDataset = new String[todos.size()];

        viewHolders = new MyAdapter.MyViewHolder[todos.size()];

        for (int i = 0; i < todos.size(); i++) {
            myDataset[i] = String.format("%d. %s", i + 1, todos.get(i).getContent());
        }
//        myDataset[0] = "1. Go to Mendota";
//        myDataset[1] = "2. Write 407";
//        myDataset[2] = "3. Review Material";
//        myDataset[3] = "4. Buy some mushroom";
//        myDataset[4] = "5. Continue develop reminder";
        mAdapter = new MyAdapter(myDataset);

        for (int i = 0; i < todos.size(); i++) {
            viewHolders[i] = (MyAdapter.MyViewHolder) mAdapter.onCreateViewHolder(recyclerView, 1);
            mAdapter.onBindViewHolder(viewHolders[i], i);
        }

        recyclerView.setAdapter(mAdapter);

        SearchView searchView = view.findViewById(R.id.searchBar);
        searchView.setBackgroundColor(Color.rgb(163,201,192));
        searchView.setQueryHint("Search Todo");

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