package com.example.magdadmilbat;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
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
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.xml.KonfettiView;


public class Feedback extends AppCompatActivity implements View.OnClickListener {
    Button btnBackMain2;
    static Double duration;
    int repsSuccess;
    int balldata;
    static SharedPreferences spBreath;
    TextView tvprogress,tvfeed,tvsubfeed;
    // ArrayList containing the duration of each repetition in the exercise
    static ArrayList<Integer> repDuration = new ArrayList<>();
    // ArrayList containing the Max Height of each repetition in the exercise
    static ArrayList<Integer> repMaxHeight = new ArrayList<>();
    int targetrep;
    double targetDuration;
    ProgressBar pbfeedback;
    private KonfettiView konfettiView = null;


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

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        btnBackMain2 = findViewById(R.id.btnBackMain2);
        btnBackMain2.setOnClickListener(this);
        Intent intent = getIntent();
        spBreath = getSharedPreferences("settingsBreath", 0);
        tvfeed = findViewById(R.id.feed);
        tvsubfeed = findViewById(R.id.subfeed);
        tvprogress = findViewById(R.id.tvprogress);
        pbfeedback = findViewById(R.id.pbfeedback);
        konfettiView = findViewById(R.id.konfettiView);
        repDuration = intent.getExtras().getIntegerArrayList("repDuration");
        repMaxHeight = intent.getExtras().getIntegerArrayList("repMaxHeight");
        repsSuccess = intent.getExtras().getInt("repsSuccess");
        duration = intent.getExtras().getDouble("duration");
        balldata = intent.getExtras().getInt("balldata");
        targetDuration = Double.parseDouble(spBreath.getString("duration",null));
        String targetBall = balldata == 3 ? "numberOfrepBlue" : balldata == 2 ? "numberOfrepOrange" :null;
        targetrep = Integer.parseInt(spBreath.getString(targetBall,null));
        tvprogress.setText(repsSuccess + "/" + targetrep);
        pbfeedback.setMax(targetrep *100);
        setProgressAnimate(pbfeedback,repsSuccess);
        if(repsSuccess >= targetrep){
            tvfeed.setText("עבודה מעולה!");
            tvsubfeed.setText("הגעת ליעד שהצבת לעצמך!");
        }else if (repsSuccess < targetrep){
            tvfeed.setText("כל הכבוד על המאמץ!");
            tvsubfeed.setText("פעם הבאה נעמוד ביעד");
        }

        EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
        Party party = new PartyFactory(emitterConfig)
                .angle(270)
                .spread(90)
                .setSpeedBetween(1f, 5f)
                .timeToLive(2000L)
                .position(0.0, 0.0, 1.0, 0.0)
                .build();

        konfettiView.start(party);
    }

    private void setProgressAnimate(ProgressBar pb, int progressTo)
    {
        ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", pb.getProgress(), progressTo *100);
        animation.setDuration(2500);
        animation.setAutoCancel(true);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
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

            Training exerciseObj = new Training(dateObj.toString(), timeObj, "נשיפה עמוקה", repsSuccess, duration, formatDouble2db(repDuration), format2db(repMaxHeight),balldata, targetrep,targetDuration);
            DatabaseManager dbObj = new DatabaseManager(Feedback.this);
            dbObj.addTraining(exerciseObj);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}