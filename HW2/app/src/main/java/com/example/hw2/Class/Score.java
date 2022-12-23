package com.example.hw2.Class;

import com.google.android.gms.maps.model.Marker;

public class Score implements Comparable<Score> {

    private int value;  //score value
    private Marker marker;
    private boolean selected; // is selceled
    //location on the map
    private double lat;
    private double lon;


    //constructor with vars
    public Score(int score, double lat, double lon) {
        this.value = score;
        this.lat = lat;
        this.lon = lon;
    }

    //constructor
    public Score() {
        this.value = 0;
        this.lat = 0.0;
        this.lon = 0.0;
        this.marker = null;
        this.selected = false;
    }

    /*
     getters and setter for all the attributes above
  */


    public Score setScore(int score) {
        this.value = score;
        return this;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public Score setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public int getValue() {
        return value;
    }

    public double getLong() {

        return this.lon;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void addValue(int value) {
        this.value += value;
    }

    public boolean isSelected() {
        return selected;
    }


    public void setMarker(Marker marker) {
        this.marker = marker;
    }


    //compare method implemented from comparison interface
    @Override
    public int compareTo(Score score) {
        return Integer.compare(score.getValue(), this.value);

    }
}
