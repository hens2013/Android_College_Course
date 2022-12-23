package com.example.hw2.Fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hw2.AdapterScore;
import com.example.hw2.Class.MyDB;
import com.example.hw2.Class.Score;
import com.example.hw2.Interface.CallBack_ScoreList;
import com.example.hw2.R;

import java.util.ArrayList;
import java.util.Objects;


public class Fragment_ScoreList extends Fragment {

    //define variables
    private RecyclerView fragment_score_list;

    private AppCompatActivity activity;
    private CallBack_ScoreList callBack_scoreList;
    private AdapterScore adapter_score;
    private ArrayList<Score> scores;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCallBackList(CallBack_ScoreList callBackList) {
        this.callBack_scoreList = callBackList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //define view
        View view = inflater.inflate(R.layout.fragment_score_list, container, false);
        //initialize fragment view
        views_init(view);
        defineAdpater();
        initiationFragment();

        return view;
    }

    private void defineAdpater() {
        //define object to get the scores from the device
        MyDB myDB = new MyDB();
        myDB.getScoresFromDB();
        scores = myDB.getTopNValues(10);
        adapter_score = new AdapterScore(activity, scores);

    }

    private void views_init(View view) {
        fragment_score_list = view.findViewById(R.id.fragment_score_list_recycle_view);
    }

    private void initiationFragment() {


        // Vertically
        fragment_score_list.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));


        fragment_score_list.setHasFixedSize(true);
        fragment_score_list.setItemAnimator(new DefaultItemAnimator());
        //set adapter
        fragment_score_list.setAdapter(adapter_score);

        adapter_score.setScoreItemClickListener(new AdapterScore.ScoreItemClickListener() {
            @Override
            public void scoreItemClicked(Score score, int position) {
                Objects.requireNonNull(fragment_score_list.getAdapter()).notifyItemChanged(position);
                callBack_scoreList.showInMap(score);
            }
        });

    }


}