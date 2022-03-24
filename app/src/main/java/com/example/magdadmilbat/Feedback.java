package com.example.magdadmilbat;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


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
import java.util.UUID;


public class Feedback extends AppCompatActivity implements View.OnClickListener {
    Button btnBackMain2;
    static Double greenAirTime,blueAirTime,orangeAirTime,greenMaxHeight,blueMaxHeight,orangeMaxHeight,duration;
    static int greenRepSuccess,blueRepSuccess,orangeRepSuccess;
    TextView greenTimeText,blueTimeText,orangeTimeText;
    static SharedPreferences spBreath ;
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
        greenAirTime = intent.getExtras().getDouble("greenAirTime");
        blueAirTime = intent.getExtras().getDouble("blueAirTime");
        orangeAirTime = intent.getExtras().getDouble("orangeAirTime");
        greenMaxHeight = intent.getExtras().getDouble("greenMaxHeight");
        blueMaxHeight = intent.getExtras().getDouble("blueMaxHeight");
        orangeMaxHeight = intent.getExtras().getDouble("orangeMaxHeight");
        greenRepSuccess = intent.getExtras().getInt("greenRepSuccess");
        blueRepSuccess = intent.getExtras().getInt("blueRepSuccess");
        orangeRepSuccess = intent.getExtras().getInt("orangeRepSuccess");
        greenMaxHeight = intent.getExtras().getDouble("greenMaxHeight");
        greenTimeText.setText("זמן באוויר (כדור ירוק) : "+greenAirTime);
        blueTimeText.setText("זמן באוויר (כדור כחול) : "+blueAirTime);
        blueTimeText.setText("זמן באוויר (כדור כתום) : "+orangeAirTime);
        duration = intent.getExtras().getDouble("duration");
    }

    public static int calculateScore(){
        int numberOfrepOrange = Integer.parseInt(spBreath.getString("numberOfrepOrange",null));
        int numberOfrepBlue = Integer.parseInt(spBreath.getString("numberOfrepBlue",null));
        int difficultyOrange = Integer.parseInt(spBreath.getString("difficultyOrange",null));
        int difficultyBlue = Integer.parseInt(spBreath.getString("difficultyBlue",null));
        int duration = Integer.parseInt(spBreath.getString("duration",null));
        double heightScore = ((blueMaxHeight + orangeMaxHeight)/(difficultyBlue + difficultyOrange))*33;
        double airTimeScore = ((blueAirTime + orangeAirTime)/(duration + duration))*33;
        double repScore = ((blueRepSuccess + orangeRepSuccess)/(numberOfrepBlue + numberOfrepOrange))*34;
       return (int) (heightScore + airTimeScore + repScore);
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
            Training exerciseObj = new Training(dateObj.toString(),timeObj,"נשיפה עמוקה",calculateScore(),duration,greenAirTime,blueAirTime,orangeAirTime,greenMaxHeight,blueMaxHeight,orangeMaxHeight,greenRepSuccess,blueRepSuccess,orangeRepSuccess);
            DatabaseManager dbObj = new DatabaseManager(Feedback.this);
            dbObj.addTraining(exerciseObj);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}