package com.example.finalapp.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.finalapp.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //TODO googlemap place apikey
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyAwC97kQwOtY8hkxT83RGyIbgS3oL3-V5A");//后面这个填谷歌地图api的Place API的key
        }
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d("THe",place.getName());
                Log.d("THe",place.getLatLng().latitude+"");
                Log.d("THe",place.getLatLng().longitude+"");
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);
                Fragment fragment = new CalAddEventFrag();
                Bundle bundle = new Bundle();
                bundle.putString("data",place.getName()+"_"+place.getLatLng().latitude+"_"+place.getLatLng().longitude);
                fragment.setArguments(bundle);//数据传递到fragment中
                fragmentTransaction.replace(R.id.container,fragment).commit();
            }
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.d("The---", "An error occurred: " + status);
            }
        });
    }
}