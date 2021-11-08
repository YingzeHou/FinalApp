package com.example.finalapp.CountDown;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalapp.Calendar.CalSettingFrag;
import com.example.finalapp.Calendar.dao.CountdownEvent;
import com.example.finalapp.Calendar.dao.Event;
import com.example.finalapp.Calendar.enums.DayOfWeek;
import com.example.finalapp.R;

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
    private List<CountdownEvent> eventList = new ArrayList<>();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countdown, container, false);
        addEventButton = view.findViewById(R.id.addEvent);

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

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setEventCard(ViewGroup viewGroup){
        CountdownEvent event1 = new CountdownEvent("Enroll in CS407", "01/01/2000", true);
        CountdownEvent event2 = new CountdownEvent("CS407 exam", "12/12/2021", false);


        eventList.add(event1);
        eventList.add(event2);

        Collections.sort(eventList);

        for(CountdownEvent event:eventList){
            CountdownEvent prevEvent = getPrevEvent(eventList,event);
            setEventCardHelper(viewGroup,event, prevEvent);
        }

    }

    private CountdownEvent getPrevEvent(List<CountdownEvent> eventList, CountdownEvent event){
        CountdownEvent eventPrev = null;
        for(CountdownEvent event1:eventList) {
            if (event1.equals(event)) {
                return eventPrev;
            }
            else if(event1.getDate()==event.getDate()){
                eventPrev=event1;
            }
        }
        return eventPrev;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setEventCardHelper(ViewGroup viewGroup, CountdownEvent event, CountdownEvent prevEvent) {

        // Instantiate event card
        CardView eventCard = new CardView(getContext());
        eventCard.setMinimumWidth(10);

        // Get date
        LocalDate date = LocalDate.of(Integer.valueOf(event.getDate().split(":")[0]),
                Integer.valueOf(event.getDate().split(":")[1]),
                Integer.valueOf(event.getDate().split(":")[2]));

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (getResources().getDimension(R.dimen.hourBlockHeight)));
        layoutParams.setMargins(10, (int) ((int) getResources().getDimension(R.dimen.hourBlockHeight)), 10, 0);

        TextView eventText = new TextView(getContext());
        eventText.setText(String.format("%s\n\n%s", event.getEventName(), event.getDate()));
        eventText.setTextColor(getResources().getColor(R.color.white));
        eventText.setTypeface(null, Typeface.BOLD);
        eventText.setPadding(5, 5, 5, 0);
        eventText.setTextSize(10);
        eventText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        eventCard.addView(eventText);
        eventCard.setRadius(30);
        eventCard.setAlpha(0.75F);
        eventCard.setLayoutParams(layoutParams);
//        eventCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(),String.format(
//                        "Event Name: %s\n\nEvent Date: %s\n\n",event.getEventName(),
//                        event.getDate(),Toast.LENGTH_SHORT)
//                        .show());
//            }
//        });
    }

}