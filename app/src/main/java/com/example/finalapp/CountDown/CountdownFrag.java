package com.example.finalapp.CountDown;

import static android.provider.Settings.System.DATE_FORMAT;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalapp.R;
import com.example.finalapp.utils.CountdownDBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CountdownFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CountdownFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageButton addEventButton;
    //private List<CountdownEvent> eventList = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] myDataset;
    private MyAdapter.MyViewHolder[] viewHolders;
    private Date eventDate,todayDate;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CountdownFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Countdown.
     */
    // TODO: Rename and change types and number of parameters
    public static CountdownFrag newInstance(String param1, String param2) {
        CountdownFrag fragment = new CountdownFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countdown, container, false);
        addEventButton = view.findViewById(R.id.addEvent);
        //ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);
        //List<CardView> cardViewList = setEventCard(viewGroup);

        Context context = this.getContext().getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("events", Context.MODE_PRIVATE,null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        Context context1 = getActivity();
        SharedPreferences settings = context1.getSharedPreferences("my_first_time", Context.MODE_PRIVATE);

        if (settings.getBoolean("my_first_time", true)) {
            dbHelper.saveTodos("school started","09/05/2021");
            dbHelper.saveTodos("semester ends", "12/15/2021");
            settings.edit().putBoolean("my_first_time", false).commit();
        }
        todos = dbHelper.readTodos();



        addEventButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("info","Click");
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
                CountdownAddEventFrag fragment = new CountdownAddEventFrag();
                fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.listView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        myDataset = new String[todos.size()];

        viewHolders = new MyAdapter.MyViewHolder[todos.size()];

        for (int i = 0; i < todos.size(); i++){
            myDataset[i] = String.format("%d. %s", i + 1, todos.get(i).getContent());
        }

        mAdapter = new MyAdapter(myDataset);

        for (int i = 0; i < todos.size(); i++) {
            viewHolders[i] = (MyAdapter.MyViewHolder)mAdapter.onCreateViewHolder(recyclerView, 1);
            mAdapter.onBindViewHolder(viewHolders[i], i);
        }

        recyclerView.setAdapter(mAdapter);

        timeLineModelList = new ArrayList<>();
//        int size = name.length;
        int size = todos.size();
        timeLineModel = new TimeLineModel[size];
        context = this.getContext();
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        String curDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

        for (int i = size-1; i >= 0; i--) {
            timeLineModel[i] = new TimeLineModel();
            Todo todo = todos.get(i);
            timeLineModel[i].setStatus("active");
            timeLineModel[i].setDate(todo.getDate());
            timeLineModelList.add(timeLineModel[i]);

            String date = timeLineModel[i].getDate();
            if (curDate.compareTo(date) < 0){   //future events
                timeLineModel[i].setPast("false");
                long diff = getDaysBetweenDates(curDate,date);
                timeLineModel[i].setDescription(diff + " days until " + todo.getContent());
            }
            else {          //past events
                timeLineModel[i].setPast("true");
                long diff = getDaysBetweenDates(date, curDate);
                timeLineModel[i].setDescription(todo.getContent() + " has been " + diff + " days");
            }
        }
        timeLineRecyclerView = (RecyclerView) view.findViewById(R.id.listView);
        timeLineRecyclerView.setLayoutManager(linearLayoutManager);
        timeLineRecyclerView.setAdapter(new TimeLineAdapter(context, timeLineModelList));

        return view;
    }

    public static final String DATE_FORMAT = "MM/dd/yyyy";

    public static long getDaysBetweenDates(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

}