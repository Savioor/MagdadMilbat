package com.example.magdadmilbat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;
import com.example.magdadmilbat.database.DatabaseManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


public class Feedback extends AppCompatActivity implements View.OnClickListener {
    Button btnBackMain2;
    static Double duration;
    int repsSuccess;
    int balldata;
    static SharedPreferences spBreath;
    TextView greenTimeText, blueTimeText, orangeTimeText,tvfeed,tvsubfeed;
    // ArrayList containing the duration of each repetition in the exercise
    static ArrayList<Integer> repDuration = new ArrayList<>();
    // ArrayList containing the Max Height of each repetition in the exercise
    static ArrayList<Integer> repMaxHeight = new ArrayList<>();
    int targetrep;

    /**
     * This function gets a ArrayList which contains repetition data of exercise
     * and returns String that can be display in TextView
     */
    public static String convert2str(ArrayList<Integer> arr) {
        String str = "";
        for (int i = 0; i < arr.size(); i++) {
            str += i + 1 + ". " + arr.get(i) + "\n";
        }
        return str;
    }

    /**
     * This function gets a ArrayList which contains repetition data of exercise
     * and returns String that can be display in TextView
     */
    public static String convert2strDouble(ArrayList<Integer> arr) {
        String str = "";
        for (int i = 0; i < arr.size(); i++) {
            str += i + 1 + ". " + arr.get(i) / 10.0 + "\n";
        }
        return str;
    }

    /**
     * This function gets a ArrayList which contains repetition data of exercise
     * and returns String that can be stored in database
     * @param arr contain Integer numbers of repDuration or repMaxHeight
     * @return the String after format, for example ,1,2,3,4,5
     */
    public static String format2db(ArrayList<Integer> arr) {
        String str = "";
        for (int i = 0; i < arr.size(); i++) {
            str += "," + arr.get(i);
        }
        return str;
    }

    public static String formatDouble2db(ArrayList<Integer> arr) {
        String str = "";
        for (int i = 0; i < arr.size(); i++) {
            str += "," + arr.get(i) / 10.0;
        }
        return str;
    }

    /**
     * on create func - contains  feedback text, return button
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnBackMain2 = findViewById(R.id.btnBackMain2);

        btnBackMain2.setOnClickListener(this);
        Intent intent = getIntent();
        spBreath = getSharedPreferences("settingsBreath", 0);
        greenTimeText = findViewById(R.id.greenAirTime);
        blueTimeText = findViewById(R.id.blueAirTime);
        orangeTimeText = findViewById(R.id.orangeAirTime);
        tvfeed = findViewById(R.id.feed);
        tvsubfeed = findViewById(R.id.subfeed);
        repDuration = intent.getExtras().getIntegerArrayList("repDuration");
        repMaxHeight = intent.getExtras().getIntegerArrayList("repMaxHeight");
        repsSuccess = intent.getExtras().getInt("repsSuccess");
        // Display on textViews, the exercise data that passed from last exercise
        greenTimeText.setText("משך כל חזרה (בשניות): \n" + convert2strDouble(repDuration));
        blueTimeText.setText("גובה מקסימלי (באחוזים): \n" + convert2str(repMaxHeight));
        duration = intent.getExtras().getDouble("duration");
        balldata = intent.getExtras().getInt("balldata");

        String targetBall = balldata == 3 ? "numberOfrepBlue" : balldata == 2 ? "numberOfrepOrange" :null;
        targetrep = Integer.parseInt(spBreath.getString(targetBall,null));
        if(repsSuccess >= targetrep){
            tvfeed.setText("עבודה מעולה!");
            tvsubfeed.setText("הגעת ליעד שהצבת לעצמך!");
        }else if (repsSuccess < targetrep){
            tvfeed.setText("כל הכבוד על המאמץ!");
            tvsubfeed.setText("פעם הבאה נעמוד ביעד");
        }
    }

    /**
     * creates Training object and adds to training Database
     * returns to the main page when click on the back button
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View view) {
        if (view == btnBackMain2) {
            LocalDate dateObj = LocalDate.now();
            String timeObj = LocalTime.now()
                    .truncatedTo(ChronoUnit.SECONDS)
                    .format(DateTimeFormatter.ISO_LOCAL_TIME);
            Training exerciseObj = new Training(dateObj.toString(), timeObj, "נשיפה עמוקה", repsSuccess, duration, formatDouble2db(repDuration), format2db(repMaxHeight),balldata, targetrep);
            DatabaseManager dbObj = new DatabaseManager(Feedback.this);
            dbObj.addTraining(exerciseObj);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}