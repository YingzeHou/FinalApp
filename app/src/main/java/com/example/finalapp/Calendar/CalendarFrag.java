package com.example.finalapp.Calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finalapp.Calendar.dao.Event;
import com.example.finalapp.Calendar.enums.DayOfWeek;
import com.example.finalapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFrag extends Fragment {

//    private final String[] DAY_IDS = new String[]{"sun","mon","tue","wed","thur","fri","sat"};
//    private final String[] Day_TEXTS = new String[]{"Sun","Mon","Tue","Wed","Thur","Fri","Sat"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Event> eventList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Calendar.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFrag newInstance(String param1, String param2) {
        CalendarFrag fragment = new CalendarFrag();
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
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);
        setDateAndDay(viewGroup);
        setEventCard(viewGroup);
        return viewGroup;
    }

    public void setDateAndDay(ViewGroup viewGroup){

        // Set Header Date
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String currDate = dateFormat.format(new Date());
        TextView dateView = (TextView)viewGroup.findViewById(R.id.currDateView);
        dateView.setText(currDate);

        // Set Day of Week
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        DayOfWeek day = DayOfWeek.valueOf(dayOfWeek);
        TextView dayView = (TextView) viewGroup.findViewById(day.getViewId());
        dayView.setText(day.getTextContent());
        dayView.setTextColor(getResources().getColor(R.color.brown));
        dayView.setTypeface(null, Typeface.BOLD);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setEventCard(ViewGroup viewGroup){
        Event event1 = new Event("ECE352", "#89007E",1,"EE", "8:00","10:20","YZH","CS Building");
        eventList.add(event1);

        for(Event event:eventList){
            setEventCardHelper(viewGroup,event);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setEventCardHelper(ViewGroup viewGroup, Event event){

        // Get Week Panel to draw
        int weekDay = event.getWeekDay();
        int weekPanelId = DayOfWeek.valueOf((weekDay+1)%7).getWeekPanelId();
        LinearLayout weekDayCol = (LinearLayout) viewGroup.findViewById(weekPanelId);

        // Instantiate event card
        CardView eventCard = new CardView(getContext());
        eventCard.setMinimumWidth(10);

        // Get time slot size needed
        LocalTime startTime = LocalTime.of(Integer.valueOf(event.getStartTime().split(":")[0]),
                Integer.valueOf(event.getStartTime().split(":")[1]));
        LocalTime endTime = LocalTime.of(Integer.valueOf(event.getEndTime().split(":")[0]),
                Integer.valueOf(event.getEndTime().split(":")[1]));
        Double timeSlotRatio = Math.abs(ChronoUnit.HOURS.between(endTime,startTime)+ChronoUnit.MINUTES.between(endTime,startTime)%60/Double.valueOf(60));

        // Get time start size needed
        eventCard.setMinimumHeight((int) (getResources().getDimension(R.dimen.weekItemHeight)*timeSlotRatio));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, (int) (getResources().getDimension(R.dimen.timeHeaderHeight)),10,0);

        TextView eventText = new TextView(getContext());
        eventText.setText(String.format("%s\n%s\n%s\n%s\n%s\n%s",event.getEventName(),event.getNote(),event.getStartTime(),event.getEndTime(),
                event.getParticipant(),event.getLocation()));
        eventText.setTextSize(12);
        eventText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        eventCard.addView(eventText);
        eventCard.setCardBackgroundColor(Color.parseColor(event.getColorCode()));
        eventCard.setRadius(30);
        eventCard.setLayoutParams(layoutParams);
        weekDayCol.addView(eventCard);

//        LinearLayout weekDayCol = (LinearLayout) viewGroup.findViewById(R.id.weekPanel_1);
//        CardView eventCard = new CardView(getContext());
//        eventCard.setMinimumWidth(10);
//        eventCard.setMinimumHeight((int) (getResources().getDimension(R.dimen.weekItemHeight)*1.2));
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(10, (int) (70+1.5*(int) getResources().getDimension(R.dimen.weekItemHeight)),10,0);
//
//        TextView eventText = new TextView(getContext());
//        eventText.setText(" Event1\nNew Try ");
//        eventText.setTextSize(15);
//        eventText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        eventCard.addView(eventText);
//        eventCard.setLayoutParams(layoutParams);
//        weekDayCol.addView(eventCard);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
//        inflater.inflate(R.menu.calendar_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.calMenu_setting:
//                return true;
//            case R.id.calMenu_other:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}