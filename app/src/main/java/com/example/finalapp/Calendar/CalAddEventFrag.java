package com.example.finalapp.Calendar;

import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalapp.R;

import top.defaults.colorpicker.ColorPickerPopup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalAddEventFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalAddEventFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int mDefaultColor;
    private View myColorPreview;

    public CalAddEventFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalAddEventFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static CalAddEventFrag newInstance(String param1, String param2) {
        CalAddEventFrag fragment = new CalAddEventFrag();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cal_add_event, container, false);
        ImageButton goBackBtn = view.findViewById(R.id.goBackBtn);
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
                Fragment fragment = new CalendarFrag();
                fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
            }
        });

        Button colorPicker = view.findViewById(R.id.colorPicker);
        mDefaultColor=0;
        myColorPreview = view.findViewById(R.id.preview_selected_color);
        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerPopup.Builder(getContext()).initialColor(
                        Color.RED
                ).enableBrightness(
                        true
                ).okTitle(
                        "Select"
                ).cancelTitle(
                        "Cancel"
                ).showIndicator(
                        true
                ).showValue(
                        true
                ).build().show(
                        v,
                        new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void
                            onColorPicked(int color) {
                                // set the color
                                // which is returned
                                // by the color
                                // picker
                                mDefaultColor = color;

                                // now as soon as
                                // the dialog closes
                                // set the preview
                                // box to returned
                                // color
                                myColorPreview.setBackgroundColor(mDefaultColor);
                            }
                        }
                );
            }
        });

        return view;
    }
}