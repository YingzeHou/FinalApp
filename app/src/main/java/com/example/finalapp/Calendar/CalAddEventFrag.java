package com.example.finalapp.Calendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalapp.Calendar.dao.Event;
import com.example.finalapp.MainActivity;
import com.example.finalapp.R;
import com.example.finalapp.utils.DBHelper;
import com.example.finalapp.utils.NotificationReceiver;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import top.defaults.colorpicker.ColorPickerPopup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalAddEventFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalAddEventFrag extends Fragment {

    public String location="";
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
    public EditText eventLocation;
    private boolean update=false;
    String place="";

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

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

//    SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.finalapp.Calendar", Context.MODE_PRIVATE);
//    if(!sharedPreferences.get("location","").equals("")){
//        eventLocation.setText(sharedPreferences.getString("location", ""));
//    }
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
        eventLocation=view.findViewById(R.id.eventLocation);
        eventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Choose Location", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getContext(),MapActivity.class));
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
                fragmentTransaction.addToBackStack(null);
                Fragment fragment = new ChooseLocFrag();
                fragmentTransaction.add(R.id.nav_fragment,fragment).commit();
            }
        });
        if(getArguments()!=null){
            if(getArguments().getString("eventInfo")!=null) {
                update = true;
                String eventInfo = getArguments().getString("eventInfo");
                if (!TextUtils.isEmpty(eventInfo)) {
                    Context context = getContext();
                    SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("events", Context.MODE_PRIVATE, null);
                    DBHelper dbHelper = new DBHelper(sqLiteDatabase);
                    List<Event> eventList = dbHelper.selectEvent(eventInfo.split("/")[0],
                            eventInfo.split("/")[1], eventInfo.split("/")[2]);
                    setEventInfo(eventList, view);
                }
            }

            place=getArguments().getString("data");
            if(!TextUtils.isEmpty(place)){
                eventLocation.setText(place);
            }
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
                String selectedMinuteStr = selectedMinute<=9?"0"+selectedMinute:String.valueOf(selectedMinute);
                String selectedHourStr = selectedHour<=9?"0"+selectedHour:String.valueOf(selectedHour);
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
        EditText eventRoom = v.findViewById(R.id.eventRoom);
        TextView eventWeekdays = v.findViewById(R.id.weekDay);
        TextView eventStart = v.findViewById(R.id.eventStartTime);
        TextView eventEnd = v.findViewById(R.id.eventEndTime);

        String eName = "";
        String eStart = "";
        String eEnd = "";
        List<Integer> weekDays;
        if(TextUtils.isEmpty(eventName.getText())) {
            Toast.makeText(getContext(),"Please Input Event Name",Toast.LENGTH_SHORT).show();
            eventName.setError("Please Input Event Name");
            return;
        }
        else{
            eName = eventName.getText().toString();
        }
        String eNote = eventNote.getText()==null? "": eventNote.getText().toString();
        String eParticipant = eventParticipant.getText()==null?"":eventParticipant.getText().toString();
        String eLocation = location;//eventLocation.getText()==null?"":eventLocation.getText().toString();
        String eRoom = eventRoom.getText()==null?"":eventRoom.getText().toString();

        if(TextUtils.isEmpty(eventStart.getText())) {
            Toast.makeText(getContext(),"Please Input Start Time",Toast.LENGTH_SHORT).show();
            eventStart.setError("Please Input Start Time");
            return;
        }
        else{
            eStart = eventStart.getText().toString();
        }
        if(TextUtils.isEmpty(eventEnd.getText())) {
            Toast.makeText(getContext(),"Please Input End Time",Toast.LENGTH_SHORT).show();
            eventEnd.setError("Please Input End Time");
            return;
        }
        else{
            eEnd = eventEnd.getText().toString();
        }

        String[] weekDaysList = eventWeekdays.getText().toString().split(", ");

        if(TextUtils.isEmpty(eventWeekdays.getText())){
            Toast.makeText(getContext(),"Please Input Week Days",Toast.LENGTH_SHORT).show();
            eventWeekdays.setError("Please Input Week Days");
            return;
        }
        else {
            weekDays = getWeekDays(weekDaysList);
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        String eColor = String.valueOf(mDefaultColor);

        Context context = getContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("events", Context.MODE_PRIVATE, null);

        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        if(!update) {
            for(int d:weekDays){
                int alarmId = mainActivity.setAlarm(eventStart.getText().toString(), d, eName, eLocation);
                dbHelper.saveEvent(eName,eColor,eNote,d,eStart,eEnd,eParticipant,eLocation, eRoom, alarmId);
            }
        }
        else{
            Toast.makeText(getContext(),"UPDATE", Toast.LENGTH_SHORT).show();
            dbHelper.deleteEvent(prevName,prevStart,prevEnd);
            for(int d:weekDays){
                int alarmId = mainActivity.setAlarm(eventStart.getText().toString(), d, eName, eLocation);
                dbHelper.saveEvent(eName,eColor,eNote,d,eStart,eEnd,eParticipant,eLocation, eRoom, alarmId);
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