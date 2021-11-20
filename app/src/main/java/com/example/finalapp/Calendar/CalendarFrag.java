package com.example.finalapp.Calendar;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalapp.App;
import com.example.finalapp.Calendar.dao.Event;
import com.example.finalapp.Calendar.enums.DayOfWeek;
import com.example.finalapp.MainActivity;
import com.example.finalapp.R;
import com.example.finalapp.utils.DBHelper;
import com.example.finalapp.utils.NotificationReceiver;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private NotificationManagerCompat notificationManager;

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
        List<CardView> cardViewList = setEventCard(viewGroup);
        ImageButton calSettingBtn = (ImageButton) viewGroup.findViewById(R.id.calSetting);
        ImageButton calAddEventBtn = (ImageButton) viewGroup.findViewById(R.id.addEvent);
        calSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","Click");
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
                Fragment fragment = new CalSettingFrag();
                fragmentTransaction.replace(R.id.nav_fragment,fragment).addToBackStack(null).commit();
            }
        });

        calAddEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
                Fragment fragment = new CalAddEventFrag();
                fragmentTransaction.replace(R.id.nav_fragment,fragment).addToBackStack(null).commit();
            }
        });
        for(CardView c:cardViewList){
            registerForContextMenu(c);
        }
        return viewGroup;
    }

    public void sendNotification(View v){
        Toast.makeText(getContext(),"Send", Toast.LENGTH_SHORT).show();
        Notification notification = new NotificationCompat.Builder(getActivity(), App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Event Alarm")
                .setContentText("event alarm here!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager = NotificationManagerCompat.from(getActivity());
        notificationManager.notify(1, notification);
    }
    CardView selectedView = null;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        selectedView = (CardView) v;
        // you can set menu header with title icon etc
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.cal_popup_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.editEvent) {
            Bundle bundle = new Bundle();

            TextView textView = selectedView==null?null: (TextView) selectedView.getChildAt(0);
            String eventInfo = textView==null?null:
                    textView.getText().toString().split("\n")[0]+"/"+
                            textView.getText().toString().split("\n")[3]+"/"+
                            textView.getText().toString().split("\n")[4];

            bundle.putString("eventInfo", eventInfo);
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
            Fragment fragment = new CalAddEventFrag();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.nav_fragment,fragment).addToBackStack(null).commit();

            return true;
        }
        if(item.getItemId() == R.id.delEvent){
            Context context = getContext();
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("events", Context.MODE_PRIVATE, null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);
            TextView textView = (TextView) selectedView.getChildAt(0);
            String eventName = textView.getText().toString().split("\n")[0];
            String startTime = textView.getText().toString().split("\n")[3];
            String endTime = textView.getText().toString().split("\n")[4];
            AlertDialog.Builder deleteConfirmation = new AlertDialog.Builder(getContext());
            deleteConfirmation.setTitle("Do you want to delete this event?")
                    .setMessage("Delete Confirmation")
                    .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<Event> eventList = dbHelper.selectEvent(eventName,startTime,endTime);
                            MainActivity mainActivity = (MainActivity) getActivity();
                            for(Event e:eventList){
                                mainActivity.cancelAlarm(e);
                            }
                            dbHelper.deleteEvent(eventName,startTime,endTime);

                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
                            Fragment fragment = new CalendarFrag();
                            fragmentTransaction.replace(R.id.nav_fragment,fragment).commit();
                        }
                    })
            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            deleteConfirmation.create().show();
        }
        return true;
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
        dayView.setTypeface(null, Typeface.BOLD_ITALIC);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<CardView> setEventCard(ViewGroup viewGroup){
        List<CardView> cardViewList = new ArrayList<>();
        Context context = getContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("events", Context.MODE_PRIVATE,null);

        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        ArrayList<Event> eventList = dbHelper.readEvents();

        Collections.sort(eventList);

        for(Event event:eventList){
            Event prevEvent = getPrevEvent(eventList,event);
            CardView cardView = setEventCardHelper(viewGroup,event, prevEvent);
            cardViewList.add(cardView);
        }
        return cardViewList;

    }

    private Event getPrevEvent(List<Event> eventList, Event event){
        Event eventPrev = null;
        for(Event event1:eventList) {
            if (event1.equals(event)) {
                return eventPrev;
            }
            else if(event1.getWeekDay()==event.getWeekDay()){
                eventPrev=event1;
            }
        }
        return eventPrev;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private CardView setEventCardHelper(ViewGroup viewGroup, Event event, Event prevEvent){
        // Get Week Panel to draw
        int weekDay = event.getWeekDay();
        int weekPanelId = DayOfWeek.valueOf((weekDay+1)%7).getWeekPanelId();
        LinearLayout weekDayCol = (LinearLayout) viewGroup.findViewById(weekPanelId);

        // Instantiate event card
        CardView eventCard = new CardView(getContext());
        eventCard.setMinimumWidth(150);

        // Get time slot size needed
        LocalTime startTime = LocalTime.of(Integer.valueOf(event.getStartTime().split(":")[0]),
                Integer.valueOf(event.getStartTime().split(":")[1]));
        LocalTime endTime = LocalTime.of(Integer.valueOf(event.getEndTime().split(":")[0]),
                Integer.valueOf(event.getEndTime().split(":")[1]));
        Double timeSlotRatio = Math.abs(ChronoUnit.HOURS.between(endTime,startTime)+ChronoUnit.MINUTES.between(endTime,startTime)%60/Double.valueOf(60));

        // Get time start size needed
        int baseHour = prevEvent==null?8: Integer.parseInt(prevEvent.getEndTime().split(":")[0]);
        int baseMin = prevEvent==null?0: Integer.parseInt(prevEvent.getEndTime().split(":")[1]);
        int isTop = prevEvent==null?1:0;
        LocalTime baseTime = LocalTime.of(baseHour,baseMin);
        Double timeStartRatio = prevEvent==null?
                Math.abs(ChronoUnit.HOURS.between(startTime,baseTime)+ChronoUnit.MINUTES.between(startTime,baseTime)%60/Double.valueOf(60))+0.5:
                Math.abs(ChronoUnit.HOURS.between(startTime,baseTime)+ChronoUnit.MINUTES.between(startTime,baseTime)%60/Double.valueOf(60));

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(150,(int) (getResources().getDimension(R.dimen.hourBlockHeight)*timeSlotRatio));
        layoutParams.setMargins(20, (int) ((int) getResources().getDimension(R.dimen.hourBlockHeight)*timeStartRatio),10,0);

        Log.d("Tet",event.getLocation());
        String loc=event.getLocation();
        String[] str=loc.split(",");//
        Double lat=Double.parseDouble(str[1]);
        Double lon=Double.parseDouble(str[2]);
        Log.d("Tet",lat+","+lon);

        TextView eventText = new TextView(getContext());
        eventText.setText(String.format("%s\n\n%s\n%s\n%s",event.getEventName(),str[0],event.getStartTime(),event.getEndTime()));
        eventText.setTextColor(getResources().getColor(R.color.white));
        eventText.setTypeface(null, Typeface.BOLD);
        eventText.setPadding(5,5,5,0);
        eventText.setTextSize(10);
        eventText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        eventCard.addView(eventText);
        eventCard.setCardBackgroundColor(Integer.parseInt(event.getColorCode()));
        eventCard.setRadius(30);
        eventCard.setAlpha(0.75F);
        eventCard.setLayoutParams(layoutParams);

        eventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),String.format(
                        "Event Name: %s\n\nEvent Location: %s\n\nStart Time: %s\n\nEnd Time: " +
                                "%s\n\nParticipant: %s\n\nNotes: %s",event.getEventName(),
                        str[0],event.getStartTime(),event.getEndTime(),
                        event.getParticipant(), event.getNote()),Toast.LENGTH_SHORT)
                        .show();

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon+"&mode=w");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        weekDayCol.addView(eventCard);
        return eventCard;
    }

}