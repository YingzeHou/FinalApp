package com.example.finalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
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
                    Fragment selectedFragment = new Calendar();

                    switch (item.getItemId()){
                        case R.id.archive:
                            selectedFragment = new Archive();
                            break;
                        case R.id.reminder:
                            selectedFragment = new Reminder();
                            break;
                        case R.id.calendar:
                            selectedFragment = new Calendar();
                            break;
                        case R.id.countdown:
                            selectedFragment = new Countdown();
                            break;
                        case R.id.map:
                            selectedFragment = new Map();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment,selectedFragment).commit();
                    return true;
                }
            };
}