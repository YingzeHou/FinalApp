package com.example.finalapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalapp.Calendar.dao.Event;
import com.example.finalapp.utils.DBHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapFrag extends Fragment {

    private View view;
    private GoogleMap mMap;
    private List<LatLng> mDestinationLatLngList=new ArrayList<>();
    private ArrayList<Event> eventList=new ArrayList<>();
    private List<String> colors=new ArrayList<>();
    private List<String> locations=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_map, container, false);

        SQLiteDatabase sqLiteDatabase = getActivity().openOrCreateDatabase("events", Context.MODE_PRIVATE, null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        ArrayList<Event> eventList = dbHelper.readEvents();
        Collections.sort(eventList);
        initData();

        SupportMapFragment mapFragment = (SupportMapFragment) getParentFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            for(LatLng latLng: mDestinationLatLngList){
                googleMap.addMarker(new MarkerOptions().position(latLng));
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(mDestinationLatLngList.get(0)));
        });
        return view;
    }
    private void initData(){
        for(Event event:eventList){
            String colorCode=event.getColorCode();
            String location=event.getLocation();
            String[] splits=location.split("-");
            colors.add(colorCode);
            locations.add(splits[0]);
            LatLng mDestinationLatLng=new LatLng(Double.parseDouble(splits[1]), Double.parseDouble(splits[2]));
            mDestinationLatLngList.add(mDestinationLatLng);
        }
    }
}