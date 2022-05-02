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
    static SharedPreferences spBreath;
    TextView greenTimeText, blueTimeText, orangeTimeText;
    static ArrayList<Integer> repDuration = new ArrayList<Integer>();
    static ArrayList<Integer> repMaxHeight = new ArrayList<Integer>();

    public static String convert2str(ArrayList<Integer> arr) {
        String str = "";
        for (int i = 0; i < arr.size(); i++) {
            str += i + 1 + ". " + arr.get(i) + "\n";
        }
        return str;
    }

//    public static int calculateScore(){
//        int numberOfrepOrange = Integer.parseInt(spBreath.getString("numberOfrepOrange",null));
//        int numberOfrepBlue = Integer.parseInt(spBreath.getString("numberOfrepBlue",null));
//        int difficultyOrange = Integer.parseInt(spBreath.getString("difficultyOrange",null));
//        int difficultyBlue = Integer.parseInt(spBreath.getString("difficultyBlue",null));
//        int duration = Integer.parseInt(spBreath.getString("duration",null));
//        double heightScore = ((blueMaxHeight + orangeMaxHeight)/(difficultyBlue + difficultyOrange))*33;
//        double airTimeScore = ((blueAirTime + orangeAirTime)/(duration + duration))*33;
//        double repScore = ((blueRepSuccess + orangeRepSuccess)/(numberOfrepBlue + numberOfrepOrange))*34;
//       return (int) (heightScore + airTimeScore + repScore);
//    }

    public static String convert2strDouble(ArrayList<Integer> arr) {
        String str = "";
        for (int i = 0; i < arr.size(); i++) {
            str += i + 1 + ". " + arr.get(i) / 10.0 + "\n";
        }
        return str;
    }

    public static String format2db(ArrayList<Integer> arr) {
        String str = "";
        for (int i = 0; i < arr.size(); i++) {
            str += "," + arr.get(i) / 10;
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
        repDuration = intent.getExtras().getIntegerArrayList("repDuration");
        repMaxHeight = intent.getExtras().getIntegerArrayList("repMaxHeight");
        repsSuccess = intent.getExtras().getInt("repsSuccess");
        greenTimeText.setText("משך כל חזרה (בשניות): \n" + convert2strDouble(repDuration));
        blueTimeText.setText("גובה מקסימלי (באחוזים): \n" + convert2str(repMaxHeight));
        duration = intent.getExtras().getDouble("duration");
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
            Training exerciseObj = new Training(dateObj.toString(), timeObj, "נשיפה עמוקה", repsSuccess, duration, format2db(repDuration), format2db(repMaxHeight));
            DatabaseManager dbObj = new DatabaseManager(Feedback.this);
            dbObj.addTraining(exerciseObj);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}