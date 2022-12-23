package com.example.hw2.Class;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


//class for implementation of the arraylist of the score for the users
public class MyDB {

    //score key
    private final String SCORES_KEY = "scores";

    //arraylist for saving the scores
    private ArrayList<Score> scores_array = new ArrayList<>();

    public MyDB() {
    }

    //rerun all the scores
    public ArrayList<Score> getRecords() {

        return scores_array;
    }

    //set scores
    public void setScores(ArrayList<Score> scores_records) {
        this.scores_array = scores_records;
    }

    //constructor for initialize of the array list of the scores
    public ArrayList<Score> getScoresFromDB() {

        //get the key
        String scores_str = MSPV.getMe().getString(SCORES_KEY, "");

        // if not empty load from json
        //else define new one
        if (Objects.equals(scores_str, "")) {
            this.scores_array = new ArrayList<Score>();

        } else {

            Gson gson = new Gson();
            this.scores_array = gson.fromJson(scores_str, MyDB.class).getRecords();
        }

        return this.scores_array;
    }

    //save the records in the db
    public void saveScoresToDB() {
        Gson gson = new Gson();
        String json_str = gson.toJson(this);
        MSPV.getMe().putString(SCORES_KEY, json_str);
    }


    //function for gets the top N
    public ArrayList<Score> getTopNValues(int n) {
        //sorts the array list
        Collections.sort(this.scores_array);
        //gets the n top values with for loop after sort
        ArrayList<Score> top_n_values_scores = new ArrayList<>();
        for (int i = 0; i < n && i < this.scores_array.size(); i++) {
            top_n_values_scores.add(this.scores_array.get(i));
        }

        return top_n_values_scores;
    }
}
