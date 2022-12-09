package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    //define variables
    private ImageView right_arrow, leftButton;

    private LinearLayout car_layout;
    private TableLayout rocks_table_layout;
    private int car_position_number;
    private int crashes_count;
    private int index;

    private Timer game_timer;


    //define the size of the board for the car
    private final int ROWS_NUMBER = 7;
    private final int COLS_NUMBER = 3;


    // Rock speed falling
    private final int SPEED_ROCK = 1100;

    //random object
    private Random rnd;

    private final int HEARTS_NUM = 3;
    private final ImageView[] hearts_array = new ImageView[HEARTS_NUM];

    // number of cols
    private final int RATE = 3; // rate to add rocks in screen

    //lines min and max number
    private final int MAX_LINES_NUM = 3;
    private final int MIN_LINES_NUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVars();
        InitializeAndFindingViews();
        ArrowLeftOnClickListeners();
        ArrowRightOnClickListeners();
        RocksFalling();
    }

    //initialize vars
    private void initializeVars() {
        rnd = new Random();
        index = 0;
        car_position_number = 2;
        crashes_count = 0;
    }

    //activate right and left arrows buttons, three hearts line,
    private void InitializeAndFindingViews() {

        //activate hearts array
        hearts_array[0] = findViewById(R.id.heart1);
        hearts_array[1] = findViewById(R.id.heart2);
        hearts_array[2] = findViewById(R.id.heart3);

        // Activate right arrow button
        right_arrow = (ImageView) findViewById(R.id.arrow_right);

        // Activate left arrow button
        leftButton = (ImageView) findViewById(R.id.arrow_left);

        //activate car
        car_layout = (LinearLayout) findViewById(R.id.car);

        //activate rocks array
        rocks_table_layout = findViewById(R.id.Table_rocks);


    }

    //the method of the timer runs in the main ui thread
    private void timerActivate() {
        this.runOnUiThread(new Runnable() {
            public void run() {


                if (index % RATE == 0)
                    rockDisplay(0, GenerateRocksPositions());
                index++;

                //update the hearts array
                carCrashValidation();
                PositionRocksUpdate();

            }
        });
    }

    //activate rocks falling method
    private void RocksFalling() {
        game_timer = new Timer();
        game_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerActivate();
            }

        }, 0, SPEED_ROCK);
    }

    //update rock positions
    private void PositionRocksUpdate() {
        //update rocks location
        for (int i = index % RATE; i < rocks_table_layout.getChildCount(); i += RATE) {
            TableRow row = (TableRow) rocks_table_layout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                ImageView rock = (ImageView) row.getChildAt(j);

                //check visible and inviable of the rock images
                if (rock.getVisibility() == View.VISIBLE) {
                    rock.setVisibility(View.INVISIBLE);
                    if (i + 1 < rocks_table_layout.getChildCount())
                        rockDisplay(i + 1, j);
                }
            }
        }
    }


    //generate rocks positions
    public int GenerateRocksPositions() {
        return rnd.nextInt(COLS_NUMBER);
    }

    private void carCrashValidation() {

        //get the row of the position of the rock
        TableRow row = (TableRow) rocks_table_layout.getChildAt(ROWS_NUMBER - 2);

        for (int i = 0; i < row.getChildCount(); i++) {
            ImageView img = (ImageView) row.getChildAt(i);

            // if the car is crashed
            if (img.getVisibility() == View.VISIBLE && car_position_number == i + 1) {
                crashes_count += 1;

                // if number of crashes less of hearts num -> decrement by one
                if (crashes_count > HEARTS_NUM) {
                    hearts_array[0].setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Oh-hh, Game Over!", Toast.LENGTH_SHORT).show();
                    game_timer.cancel();
                } else
                    hearts_array[HEARTS_NUM - crashes_count].setVisibility(View.INVISIBLE);
            }
        }

    }

    //update the car postion after every click on the arrow buttons
    private void updateCarPosition() {
        for (int i = 0; i < car_layout.getChildCount(); i++) {
            ImageView car = (ImageView) car_layout.getChildAt(i);
            car.setVisibility(View.INVISIBLE);
        }

        //update position of the car according to the car_position_number
        car_layout.getChildAt(car_position_number - 1).setVisibility(View.VISIBLE);
    }


    //rock shown method
    private void rockDisplay(int i, int j) {
        TableRow row = (TableRow) rocks_table_layout.getChildAt(i);
        ImageView img_rock = (ImageView) row.getChildAt(j);
        img_rock.setVisibility(View.VISIBLE);
    }


    //if the car move right the column number is recreant by one
    private void MoveCarRight() {
        car_position_number++;
        updateCarPosition();
    }

    //if the car move right the column number is decrement by one
    private void MoveCarLeft() {
        car_position_number--;
        updateCarPosition();
    }

    //arrow left click lister method
    private void ArrowLeftOnClickListeners() {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if car is in the limits of the screen
                if (car_position_number > MIN_LINES_NUM) {
                    MoveCarLeft();
                }
            }
        });

    }

    //arrow right click lister method
    private void ArrowRightOnClickListeners() {
        right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if car is in the limits of the screen
                if (car_position_number < MAX_LINES_NUM) {
                    MoveCarRight();
                }
            }
        });

    }





}