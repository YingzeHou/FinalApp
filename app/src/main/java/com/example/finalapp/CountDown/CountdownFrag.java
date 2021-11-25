package com.example.finalapp.CountDown;

import android.content.Context;
import android.content.Intent;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static ArrayList<Todo> todos = new ArrayList<>();

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

//        for(CardView c:cardViewList){
//            registerForContextMenu(c);
//        }

        return view;
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public List<CardView> setEventCard(ViewGroup viewGroup){
//        List<CardView> cardViewList = new ArrayList<>();
//        Context context = getContext();
//        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("events", Context.MODE_PRIVATE,null);
//
//        CountdownDBHelper dbHelper = new CountdownDBHelper(sqLiteDatabase);
//        ArrayList<CountdownEvent> eventList = dbHelper.readCountdownEvents();
//
//        Collections.sort(eventList);
//
//        for(CountdownEvent event:eventList){
//            CountdownEvent prevEvent = getPrevEvent(eventList,event);
//            CardView cardView = setEventCardHelper(viewGroup,event, prevEvent);
//            cardViewList.add(cardView);
//        }
//        return cardViewList;
//
//    }
//
//    private CountdownEvent getPrevEvent(List<CountdownEvent> eventList, CountdownEvent event){
//        CountdownEvent eventPrev = null;
//        for(CountdownEvent event1:eventList) {
//            if (event1.equals(event)) {
//                return eventPrev;
//            }
//            else if(event1.getWeekDay()==event.getWeekDay()){
//                eventPrev=event1;
//            }
//        }
//        return eventPrev;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private CardView setEventCardHelper(ViewGroup viewGroup, CountdownEvent event, CountdownEvent prevEvent){
//        // Get Week Panel to draw
//        int past = event.getPast();
//        int day = event.getDays();
//        int weekDay = event.getWeekDay();
////        int weekPanelId = DayOfWeek.valueOf((weekDay+1)%7).getWeekPanelId();
////        LinearLayout weekDayCol = (LinearLayout) viewGroup.findViewById(listView);
//
//        // Instantiate event card
//        CardView eventCard = new CardView(getContext());
//        //eventCard.setMinimumWidth(150);
//
//
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(150,100);
//        layoutParams.setMargins(20, 10,10,0);
//
//        TextView eventText = new TextView(getContext());
//        eventText.setText(String.format("%s%s%d%s",event.getEventName()," has been ",event.getDays()));
//        eventText.setTextColor(getResources().getColor(R.color.black));
//        eventText.setTypeface(null, Typeface.BOLD);
//        eventText.setPadding(5,5,5,0);
//        eventText.setTextSize(10);
//        eventText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//        eventCard.addView(eventText);
//        if (past == 1) {
//            eventCard.setCardBackgroundColor(17170456);
//        }
//        else {
//            eventCard.setCardBackgroundColor(17170459);
//        }
//        eventCard.setRadius(30);
//        eventCard.setAlpha(0.75F);
//        eventCard.setLayoutParams(layoutParams);
//
//        eventCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "something happens, need more implementation",Toast.LENGTH_SHORT).show();
//            }
//        });
//        recyclerView.addView(eventCard);
//        return eventCard;
//    }


}