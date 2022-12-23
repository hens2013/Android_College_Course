package com.example.hw2.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hw2.Class.MyDB;
import com.example.hw2.Class.Score;
import com.example.hw2.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private ImageView game_left_arrow, right_arrow;
    private TextView text_score;
    private String mode;
    private LinearLayout carBox;
    private ImageView[] hearts_array;
    private int count_car_jumps;
    private TableLayout rocks;
    private Timer myTimer;

    // index
    private int index;

    // number of crashes
    private int accidentCount;

    // speed of rocks move
    private int SPEED = 1200;
    private Random rnd;
    private final int HEARTS_NUM = 3;

    // number of cols
    private final int COLS = 5;
    // rows number
    private final int ROWS = 15;

    // rate to add rocks in screen
    private final int RATE = 3;


    // max lines number in the screen
    private final int MAX_LINES_NUM = 5;

    // min lines number in the screen
    private final int MIN_LINES_NUM = 1;
    private Score player;
    private SensorManager sm;
    private Sensor accSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        if (intent.getStringExtra("speed").equals(MainActivity.MAXIMUM_VISCOSITY)) {
            SPEED = 500;
        }
        //activate functions
        inputVars();
        InitializeVariables();
        views_utlizition();
        modeSelection();
        leftArrowClick();
        rightArrowClick();
    }

    //sensor function
    private final SensorEventListener accSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if (x > 4 && count_car_jumps > MIN_LINES_NUM) {
                JumpLeft();
            } else if (x < -4 && count_car_jumps < MAX_LINES_NUM) {
                JumpRight();
            }
            System.out.println("x = " + x + " y= " + y + "z = " + z);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    //mode selection method for sensor or keywords
    private void modeSelection() {
        if (!mode.equals(MainActivity.SENSOR_MODE)) {
            game_left_arrow.setVisibility(View.VISIBLE);
            right_arrow.setVisibility(View.VISIBLE);
        } else {
            game_left_arrow.setVisibility(View.INVISIBLE);
            right_arrow.setVisibility(View.INVISIBLE);
            sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }


    //activate views
    private void views_utlizition() {
        game_left_arrow = findViewById(R.id.game_imgLeftArrow);
        right_arrow = findViewById(R.id.game_imgRightArrow);
        hearts_array[0] = findViewById(R.id.game_imageHeart1);
        hearts_array[1] = findViewById(R.id.game_imageHeart2);
        hearts_array[2] = findViewById(R.id.game_imageHeart3);
        text_score = findViewById(R.id.game_txtScore);
        carBox = findViewById(R.id.game_carBox);
        rocks = findViewById(R.id.game_table_rocks);

    }

    //right arrow function for right dir
    public void JumpRight() {
        count_car_jumps++;
        updateCarLocations();
    }


    //left arrow function for right dir
    private void leftArrowClick() {
        game_left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count_car_jumps > MIN_LINES_NUM) {
                    JumpLeft();
                }
            }
        });
    }

    //right arrow function for right dir
    private void rightArrowClick() {
        right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count_car_jumps < MAX_LINES_NUM) {
                    JumpRight();
                }
            }
        });


    }

    //function for update the postions
    private void updateCarLocations() {
        for (int i = 0; i < carBox.getChildCount(); i++) {
            ImageView car = (ImageView) carBox.getChildAt(i);
            car.setVisibility(View.INVISIBLE);
        }

        ((ImageView) carBox.getChildAt(count_car_jumps - 1)).setVisibility(View.VISIBLE);
    }

    //function to generate the roccs locations and visible
    private void showRock(int i, int j) {
        TableRow row = (TableRow) rocks.getChildAt(i);
        ImageView img = (ImageView) row.getChildAt(j);
        img.setVisibility(View.VISIBLE);
    }

    //generate rocks locations
    private void GenerateRocks() {
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Timer();
            }

        }, 0, SPEED);
    }

    //activate sound
    public void activateSound(int soundId) {
        //play sound effect
        MediaPlayer mPlayer = MediaPlayer.create(this, soundId);
        mPlayer.start();
    }

    //update score list
    private void updateTop10Scores() {
        MyDB myDB = new MyDB();
        ArrayList<Score> scores = myDB.getScoresFromDB();
        scores.add(player);
        myDB.setScores(scores);
        myDB.saveScoresToDB();

    }

    //function to the check the crash per row
    private boolean checkRationalisationRow(TableRow row) {
        boolean isCrashed = false;
        for (int i = 0; i < row.getChildCount(); i++) {
            ImageView img = (ImageView) row.getChildAt(i);
            //car is crashed
            if (img.getVisibility() == View.VISIBLE && count_car_jumps == i + 1) {
                accidentCount += 1;
                isCrashed = true;
                // if still have hears decrement by one
                if (accidentCount < HEARTS_NUM) {
                    activateSound(R.raw.crash);// play crash sound
                    hearts_array[HEARTS_NUM - accidentCount].setVisibility(View.INVISIBLE);
                } else {
                    // no hears left then game over
                    hearts_array[0].setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
                    myTimer.cancel();
                    activateSound(R.raw.game_over);//play game over sound
                    updateTop10Scores();
                    finish();
                }
            }
        }
        return isCrashed;
    }


    //method for check the crash
    private void validationsCrash() {
        //check if car crashed
        TableRow row = (TableRow) rocks.getChildAt(ROWS - 5);
        boolean isCrashed = checkRationalisationRow(row);
    }

    //update rock locations
    private void updateLocations() {
        //update rocks location
        for (int i = index % RATE; i < rocks.getChildCount(); i += RATE) {
            TableRow row = (TableRow) rocks.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                ImageView img = (ImageView) row.getChildAt(j);
                //image visible then invisible and visible the image below it
                if (img.getVisibility() == View.VISIBLE) {
                    img.setVisibility(View.INVISIBLE);
                    if (i + 1 < rocks.getChildCount())
                        showRock(i + 1, j);
                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mode.equals(MainActivity.SENSOR_MODE))
            sm.registerListener(accSensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onStop() {
        super.onStop();
        myTimer.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sm != null) {
            sm.unregisterListener(accSensorEventListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GenerateRocks();
    }

    //activate timer in another thread
    private void Timer() {
        this.runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            public void run() {
                //This method runs in the same thread as the UI.
                if (index % RATE == 0) {
                    showRock(0, generateRocksNumber());
                }
                validationsCrash();
                updateLocations();

                index++;
                player.addValue(10);
                text_score.setText("" + player.getValue());
            }
        });
    }

    //generate rocks number
    public int generateRocksNumber() {
        return rnd.nextInt(COLS);
    }

    //input to vars
    private void inputVars() {

        player = new Score();
        count_car_jumps = 3;
        index = 0;
        rnd = new Random();
        hearts_array = new ImageView[HEARTS_NUM];
        accidentCount = 0;

    }

    private void InitializeVariables() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locationGPS != null) {
            double lat = locationGPS.getLatitude();
            double longi = locationGPS.getLongitude();
            player.setLat(lat).setLon(longi);
        }
    }

    //jumps left
    public void JumpLeft() {
        count_car_jumps--;
        updateCarLocations();
    }

}