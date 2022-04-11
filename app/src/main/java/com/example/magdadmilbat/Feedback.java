package com.example.magdadmilbat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.MagdadMilbat.R;
import com.example.magdadmilbat.database.DatabaseManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * A page that opens on the end of the practice, with a feedback to the user
 */
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


public class Feedback extends AppCompatActivity implements View.OnClickListener {
    Button btnBackMain2;
    static Double duration;
    int repsSuccess;
    TextView greenTimeText,blueTimeText,orangeTimeText;
    static SharedPreferences spBreath ;
    static ArrayList<Integer> repDuration = new ArrayList<Integer>();
    static ArrayList<Integer> repMaxHeight = new ArrayList<Integer>();
    static ArrayList<String> repDurationStr;
    /**
     * on create func - contains  feedback text, return button
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnBackMain2 = (Button) findViewById(R.id.btnBackMain2);

        btnBackMain2.setOnClickListener(this);
        Intent intent=getIntent();
        spBreath = getSharedPreferences("settingsBreath", 0);
        greenTimeText = findViewById(R.id.greenAirTime);
        blueTimeText = findViewById(R.id.blueAirTime);
        orangeTimeText = findViewById(R.id.orangeAirTime);
        repDuration = intent.getExtras().getIntegerArrayList("repDuration");
        repMaxHeight = intent.getExtras().getIntegerArrayList("repMaxHeight");
        repsSuccess = intent.getExtras().getInt("repsSuccess");
        greenTimeText.setText("משך כל חזרה (בשניות): \n"+convert2str(repDuration));
        blueTimeText.setText("גובה מקסימלי (באחוזים): \n"+convert2str(repMaxHeight));
        duration = intent.getExtras().getDouble("duration");
        repDurationStr = convertArrInt(repDuration);
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

    public static String convert2str(ArrayList<Integer> arr){
        String str = "";
        for(int i = 0; i< arr.size();i++){
            str += i+1 + ". " + arr.get(i) +"\n";
        }
        return str;
    }

    public static String format2db(ArrayList<Integer> arr){
        String str = "";
        for(int i = 0; i<arr.size();i++){
            str += i == 0 ? "" : "," + arr.get(i);
        }
        return str;
    }


    public static String format2dbStr(ArrayList<String> arr){
        String str = "";
        for(int i = 0; i<arr.size();i++){
            str += i == 0 ? "" : "," + arr.get(i);
        }
        return str;
    }


    public static ArrayList<String> convertArrInt(ArrayList<Integer> arr){
        ArrayList<String> arrstr = new ArrayList<>();
        for(int i = 0 ;i<arr.size() ;i++){
            arrstr.add(formatDuration(arr.get(i)));
        }
        return arrstr;
    }

    public static String formatDuration(int time){
        String str;
        int sec = ((time % 864000) % 3600) % 60;
        int min = ((time % 864000) % 3600) / 60;
        str =  String.format("%02d",min) + ":" + String.format("%02d",sec);
        return str;
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
            Training exerciseObj = new Training(dateObj.toString(),timeObj,"נשיפה עמוקה",repsSuccess,duration,format2dbStr(repDurationStr),format2db(repMaxHeight));
            DatabaseManager dbObj = new DatabaseManager(Feedback.this);
            dbObj.addTraining(exerciseObj);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}