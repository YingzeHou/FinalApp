package com.example.finalapp.Calendar;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalapp.Calendar.dao.Event;
import com.example.finalapp.R;
import com.example.finalapp.utils.DBHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    private TextView weekDaySelectView;
    boolean[] selected;
    ArrayList<Integer> dayList = new ArrayList<>();
    String[] dayArray = {"Monday", "Tuesday", "Wednesday", "Thursday","Friday"};
    private TextView startTimeView;
    private TextView endTimeView;
    private boolean update=false;

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

        TextView colorPicker = view.findViewById(R.id.colorPicker);
        mDefaultColor=0;
        myColorPreview = view.findViewById(R.id.preview_selected_color);
        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickColorPick(v);
            }
        });

        weekDaySelectView = view.findViewById(R.id.weekDay);
        selected = new boolean[dayArray.length];
        weekDaySelectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickWeekDaySelect(v);
            }
        });

        startTimeView = view.findViewById(R.id.eventStartTime);
        endTimeView = view.findViewById(R.id.eventEndTime);
        startTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTimePick(v, startTimeView);
            }
        });

        endTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTimePick(v, endTimeView);
            }
        });

        TextView saveEvent = view.findViewById(R.id.saveEvent);
        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent(view,mDefaultColor, update);
            }
        });

        if(getArguments()!=null){
            update=true;
            String eventInfo = getArguments().getString("eventInfo");
            Context context = getContext();
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("events", Context.MODE_PRIVATE, null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);
            List<Event> eventList = dbHelper.selectEvent(eventInfo.split("/")[0],
                    eventInfo.split("/")[1], eventInfo.split("/")[2]);


            setEventInfo(eventList,view);
        }
        return view;
    }

    String prevName = "";
    String prevStart = "";
    String prevEnd = "";
    private void setEventInfo(List<Event> eventList, View v){
        String weekDays = "";
        for(int i = 0; i<eventList.size(); i++){
            if(i==0) {
                ((EditText) v.findViewById(R.id.eventName)).setText(eventList.get(i).getEventName());
                prevName = eventList.get(i).getEventName();
                v.findViewById(R.id.preview_selected_color).setBackgroundColor(Integer.parseInt(eventList.get(i).getColorCode()));
                mDefaultColor =Integer.parseInt(eventList.get(i).getColorCode());
                ((EditText) v.findViewById(R.id.eventNote)).setText(eventList.get(i).getNote());
                ((EditText) v.findViewById(R.id.eventParticipant)).setText(eventList.get(i).getParticipant());
                ((TextView) v.findViewById(R.id.eventStartTime)).setText(eventList.get(i).getStartTime());
                prevStart = eventList.get(i).getStartTime();
                ((TextView) v.findViewById(R.id.eventEndTime)).setText(eventList.get(i).getEndTime());
                prevEnd = eventList.get(i).getEndTime();
                ((EditText) v.findViewById(R.id.eventLocation)).setText(eventList.get(i).getLocation());
            }
            weekDays+=dayArray[eventList.get(i).getWeekDay()-1];
            if(i!=eventList.size()-1){
                weekDays+=", ";
            }
        }
        ((TextView) v.findViewById(R.id.weekDay)).setText(weekDays);
    }

    private void onClickColorPick(View v){
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

    private void onClickWeekDaySelect(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getContext()
        );
        builder.setTitle("Select Day of Week");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(dayArray, selected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked){
                    dayList.add(which);
                    Collections.sort(dayList);
                }else {
                    int toDeleteInd= 0;
                    for(int j = 0; j<dayList.size(); j++){
                        if (dayList.get(j)==which){
                            toDeleteInd = j;
                            break;
                        }
                    }
                    dayList.remove(toDeleteInd);
                }
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 0; i<dayList.size(); i++){
                    stringBuilder.append(dayArray[dayList.get(i)]);
                    if(i!=dayList.size()-1){
                        stringBuilder.append(", ");
                    }
                }
                weekDaySelectView.setText(stringBuilder.toString());
                weekDaySelectView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i=0; i<dayList.size(); i++){
                    selected[i]=false;
                    dayList.clear();
                    weekDaySelectView.setText("");
                }
            }
        });
        builder.show();
    }

    private void onClickTimePick(View v, TextView timeView){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String selectedMinuteStr = selectedMinute==0?"00":String.valueOf(selectedMinute);
                String selectedHourStr = selectedHour==0?"00":String.valueOf(selectedHour);
                timeView.setText( selectedHourStr + ":" + selectedMinuteStr);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void saveEvent(View v, int mDefaultColor, boolean update){
        EditText eventName = (EditText) v.findViewById(R.id.eventName);
        EditText eventNote = v.findViewById(R.id.eventNote);
        EditText eventParticipant = v.findViewById(R.id.eventParticipant);
        EditText eventLocation = v.findViewById(R.id.eventLocation);
        TextView eventWeekdays = v.findViewById(R.id.weekDay);
        TextView eventStart = v.findViewById(R.id.eventStartTime);
        TextView eventEnd = v.findViewById(R.id.eventEndTime);

        String eName = eventName.getText().toString();
        String eNote = eventNote.getText()==null? "": eventNote.getText().toString();
        String eParticipant = eventParticipant.getText()==null?"":eventParticipant.getText().toString();
        String eLocation = eventLocation.getText()==null?"":eventLocation.getText().toString();

        String eStart = eventStart.getText().toString();
        String eEnd = eventEnd.getText().toString();

        String[] weekDaysList = eventWeekdays.getText().toString().split(", ");
        List<Integer> weekDays =getWeekDays(weekDaysList);
        String eColor = String.valueOf(mDefaultColor);

        Context context = getContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("events", Context.MODE_PRIVATE, null);

        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        if(!update) {
            for(int d:weekDays){
                dbHelper.saveEvent(eName,eColor,eNote,d,eStart,eEnd,eParticipant,eLocation);
            }
        }
        else{
            Toast.makeText(getContext(),"UPDATE", Toast.LENGTH_SHORT).show();
            dbHelper.deleteEvent(prevName,prevStart,prevEnd);
            for(int d:weekDays){
                dbHelper.saveEvent(eName,eColor,eNote,d,eStart,eEnd,eParticipant,eLocation);
            }
        }
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
        Fragment fragment = new CalendarFrag();
        fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
    }

    private List<Integer> getWeekDays(String[] weekDaysList){
        List<Integer> weekDays = new ArrayList<>();
        for(String day: weekDaysList){
            switch (day){
                case "Monday":
                    weekDays.add(1);
                    continue;
                case "Tuesday":
                    weekDays.add(2);
                    continue;
                case "Wednesday":
                    weekDays.add(3);
                    continue;
                case "Thursday":
                    weekDays.add(4);
                    continue;
                case "Friday":
                    weekDays.add(5);
                    continue;
                case "Saturday":
                    weekDays.add(6);
                    continue;
                case "Sunday":
                    weekDays.add(7);
                    continue;
                default:
                    break;
            }
        }
        return weekDays;
    }
}