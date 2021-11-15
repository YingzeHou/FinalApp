package com.example.finalapp.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.finalapp.ArchiveFrag;
import com.example.finalapp.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Arrays;

public class ChooseLocFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText eventLocation;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChooseLocFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChooseLoc.
     */
    // TODO: Rename and change types and number of parameters
    public static ArchiveFrag newInstance(String param1, String param2) {
        ArchiveFrag fragment = new ArchiveFrag();
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
        View view = inflater.inflate(R.layout.activity_map, container, false);
        eventLocation = view.findViewById(R.id.eventLocation);
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), "AIzaSyAwC97kQwOtY8hkxT83RGyIbgS3oL3-V5A");//后面这个填谷歌地图api的Place API的key
        }
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.map_choose);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.finalapp.Calendar", Context.MODE_PRIVATE);
            @Override
            public void onPlaceSelected(Place place) {
                Log.d("THe",place.getName());
                Log.d("THe",place.getLatLng().latitude+"");
                Log.d("THe",place.getLatLng().longitude+"");
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.nav_default_enter_anim,R.anim.nav_default_exit_anim);

                CalAddEventFrag frag = (CalAddEventFrag) fragmentManager.getFragments().get(0);
                frag.eventLocation.setText(place.getName());
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
