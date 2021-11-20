package com.example.finalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.finalapp.Calendar.CalendarFrag;
import com.example.finalapp.Calendar.dao.Event;
import com.example.finalapp.CountDown.CountdownFrag;
import com.example.finalapp.Reminder.ReminderFrag;
import com.example.finalapp.utils.NotificationReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private NotificationManagerCompat notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.calendar);
        navigationView.setOnItemSelectedListener(listener);
    }

    public int setAlarm(String eventStart, Integer weekDay, String eventName, String eventLocation){
        int hour = Integer.parseInt(eventStart.split(":")[0]);
        int minute = Integer.parseInt(eventStart.split(":")[1]);
//        weekDay%7==0?1:weekDay%7+1
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        hour = minute<10?hour-1:hour;
        minute = minute<10?(60-(minute-10)):minute-10;
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("eventName", eventName);
        intent.putExtra("eventLocation", eventLocation);
        final int id = (int) System.currentTimeMillis();
        intent.putExtra("alarmId",id);
        pendingIntent = PendingIntent.getBroadcast(this,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                24 * 60 * 60 * 1000,pendingIntent);

        Toast.makeText(this, "Alarm set for "+id, Toast.LENGTH_SHORT).show();

        return id;
    }

    public void cancelAlarm(Event event){
        int id = event.getAlarmId();
        Intent intent = new Intent(this, NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if(alarmManager==null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        System.out.println(alarmManager.toString());
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this,"Alarm Cancel for "+ event.getEventName()+" on "+event.getWeekDay(),Toast.LENGTH_SHORT).show();
    }

    private NavigationBarView.OnItemSelectedListener listener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = new CalendarFrag();

                    switch (item.getItemId()){
                        case R.id.archive:
                            selectedFragment = new ArchiveFrag();
                            break;
                        case R.id.reminder:
                            selectedFragment = new ReminderFrag();
                            break;
                        case R.id.calendar:
                            selectedFragment = new CalendarFrag();
                            break;
                        case R.id.countdown:
                            selectedFragment = new CountdownFrag();
                            break;
                        case R.id.map:
                            selectedFragment = new MapFrag();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment,selectedFragment).commit();
                    return true;
                }
            };


}