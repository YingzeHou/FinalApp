package com.example.finalapp.Reminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalapp.ArchiveFrag;
import com.example.finalapp.Calendar.CalAddEventFrag;
import com.example.finalapp.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class ChooseLocFrag extends Fragment {

    private EditText eventLocation;

    public ChooseLocFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_map, container, false);
        eventLocation = view.findViewById(R.id.eventLocation);
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), "AIzaSyAwC97kQwOtY8hkxT83RGyIbgS3oL3-V5A");//后面这个填谷歌地图api的Place API的key
        }
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.map_choose);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.finalapp.Reminder", Context.MODE_PRIVATE);
            @Override
            public void onPlaceSelected(Place place) {
                Log.d("THe",place.getName());
                Log.d("THe",place.getLatLng().latitude+"");
                Log.d("THe",place.getLatLng().longitude+"");
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);

                Bundle bundle = getArguments();
                String type = bundle.getString("type");
                if (type.compareTo("edit") == 0) {
                    EditTodoFrag frag = (EditTodoFrag) fragmentManager.getFragments().get(0);
                    frag.locationLat = "" + place.getLatLng().latitude;
                    frag.locationLon = "" + place.getLatLng().longitude;
                    frag.editLocation.setText(place.getName());
                }
                else {
                    AddToDoFrag frag = (AddToDoFrag) fragmentManager.getFragments().get(0);
//                    frag.location = place.getName()+","+place.getLatLng().latitude+","+place.getLatLng().longitude;
                    frag.locationLat = "" + place.getLatLng().latitude;
                    frag.locationLon = "" + place.getLatLng().longitude;
                    frag.chooseLocation.setText(place.getName());
                }
                fragmentManager.popBackStack();
            }
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.d("The---", "An error occurred: " + status);
            }
        });
        return view;
    }
    
}
