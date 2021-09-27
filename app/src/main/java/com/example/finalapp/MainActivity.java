package com.example.finalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.calendar);
        navigationView.setOnItemSelectedListener(listener);
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