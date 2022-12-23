package com.example.hw2.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hw2.Class.Score;
import com.example.hw2.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Fragment_Map extends Fragment implements OnMapReadyCallback {

    private AppCompatActivity activity;
    private MapView map_scores_fragment;
    private final String MAPVIEW_BUNDLE_KEY = "AIzaSyCce_VtQX2XnQuinrMtuugEwq8VGTwwITM";
    private GoogleMap google_map_activity;

    public Fragment_Map() {
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        map_scores_fragment = view.findViewById(R.id.frag_map_scores);
        googleMaputilization(savedInstanceState);

        return view;
    }


    //function for init google maps
    private void googleMaputilization(Bundle savedInstanceState) {
        Bundle map_Bundle = null;
        if (savedInstanceState != null)
            map_Bundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);


        map_scores_fragment.onCreate(map_Bundle);
        map_scores_fragment.getMapAsync(this);
    }

    //function of andrioed circle life
    // resume, on create, on destroy, on pause

    @Override
    public void onStop() {
        super.onStop();
        map_scores_fragment.onStop();

    }

    //on start
    @Override
    public void onStart() {
        super.onStart();
        map_scores_fragment.onStart();
    }

    //on puase
    @Override
    public void onPause() {
        map_scores_fragment.onPause();
        super.onPause();
    }

    //on resume
    @Override
    public void onResume() {
        super.onResume();
        map_scores_fragment.onResume();
    }


    //on destroy
    @Override
    public void onDestroy() {
        map_scores_fragment.onDestroy();
        super.onDestroy();
    }

    //override of google map class
    @Override
    public void onMapReady(@NonNull GoogleMap google_map) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.google_map_activity = google_map;
        this.google_map_activity.setMyLocationEnabled(true);

    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map_scores_fragment.onLowMemory();
    }

    //function for set maker
    public Marker setMarker(Score score) {
        LatLng sydney = new LatLng(score.getLat(), score.getLong());
        return this.google_map_activity.addMarker(new MarkerOptions()
                .position(sydney)
                .title(score.getValue() + ""));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle map_Bundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (map_Bundle == null) {
            map_Bundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, map_Bundle);
        }


    }


    //function to the remove the maker is exist
    public void removeMarker(Score score) {
        Marker marker = score.getMarker();
        if (marker != null)
            marker.remove();
    }


}