package com.example.finalapp.CountDown;

import android.content.Intent;
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

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] myDataset;
    private MyAdapter.MyViewHolder[] viewHolders;

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

        recyclerView = (RecyclerView) view.findViewById(R.id.listView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        myDataset = new String[10];

        viewHolders = new com.example.finalapp.CountDown.MyAdapter.MyViewHolder[10];

        myDataset[0] = "10 days since event 1";
        myDataset[1] = "20 days since event 2";
        myDataset[2] = "30 days since event 3";
        myDataset[3] = "40 days since event 4";
        myDataset[4] = "50 days since event 5";
        myDataset[5] = "60 days since event 6";
        myDataset[6] = "70 days since event 7";
        myDataset[7] = "80 days since event 8";
        myDataset[8] = "90 days since event 9";
        myDataset[9] = "100 days since event 10";
        mAdapter = new com.example.finalapp.CountDown.MyAdapter(myDataset);

        for (int i = 0; i < 10; i++) {
            viewHolders[i] = (com.example.finalapp.CountDown.MyAdapter.MyViewHolder) mAdapter.onCreateViewHolder(recyclerView, 1);
            mAdapter.onBindViewHolder(viewHolders[i], i);
        }

        recyclerView.setAdapter(mAdapter);

        return view;
    }


}