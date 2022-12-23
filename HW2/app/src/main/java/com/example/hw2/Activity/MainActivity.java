package com.example.hw2.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.hw2.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {


    public static final String SENSOR_MODE = "sensor_mode";
    public static final String BUTTONS_MODE = "buttons_mode";
    public static final String MAXIMUM_VISCOSITY = "fast";
    public static final String MINIMUM_VISCOSITY = "slow";


    private MaterialButton button_start;
    private MaterialButton button_scores;
    private RadioGroup rBtn_group_modesOptions, rBtn_group_speedOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_scores = findViewById(R.id.btn_scores);
        button_start = findViewById(R.id.btn_startGame);
        rBtn_group_modesOptions = findViewById(R.id.rBtnModeOptions);
        rBtn_group_speedOptions = findViewById(R.id.rBtnSpeedOptions);


        buttonScores();
        buttonStart();


        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //button score function
    private void buttonScores() {
        button_scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                startActivity(intent);
            }
        });
    }


    //mode selected if sensor of by buttons
    private String modeSelected() {
        String mode = "";
        switch (rBtn_group_modesOptions.getCheckedRadioButtonId()) {
            case R.id.rBtn_buttons:
                mode = BUTTONS_MODE;
                break;
            case R.id.rBtn_sensor:
                mode = SENSOR_MODE;
                break;
        }
        return mode;
    }

    //button start function
    private void buttonStart() {

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("mode", modeSelected());
                intent.putExtra("speed", speedSelected());

                startActivity(intent);
            }
        });
    }


    //speed selected slow or fast
    private String speedSelected() {
        String mode = "";
        switch (rBtn_group_speedOptions.getCheckedRadioButtonId()) {
            case R.id.rBtnSpeedSlow:
                mode = MINIMUM_VISCOSITY;
                break;

            case R.id.rBtnSpeedFast:
                mode = MAXIMUM_VISCOSITY;
                break;

        }
        return mode;
    }


}