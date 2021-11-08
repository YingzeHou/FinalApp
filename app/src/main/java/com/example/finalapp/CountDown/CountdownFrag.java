package com.example.finalapp.CountDown;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.finalapp.Calendar.CalSettingFrag;
import com.example.finalapp.R;

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
    //public static ArrayList<Note> notes = new ArrayList<>();

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

}