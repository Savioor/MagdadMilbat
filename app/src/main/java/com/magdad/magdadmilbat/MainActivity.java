package com.magdad.magdadmilbat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;

import org.opencv.android.OpenCVLoader;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnStartExercise, btnHistory, btnSettings;
    SharedPreferences spSetApp;
    SharedPreferences spBreath;
    /**
     * on create func - contains three buttons - exercise, history and settings
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String DEFAULT_REPS = "10";
        String DEFAULT_DIFFICULTY = "3";
        String DEFAULT_TIME = "3";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartExercise = findViewById(R.id.btnStartExercise);
        btnHistory = findViewById(R.id.btnHistory);
        btnSettings = findViewById(R.id.btnFeedback);
        spSetApp = getSharedPreferences("spSetApp", 0);
        spBreath = getSharedPreferences("settingsBreath", 0);
        String isFirstLaunch = spSetApp.getString("firstLaunch", null);
        // set default values for application settings
        if (isFirstLaunch == null) {
            SharedPreferences.Editor editor2 = spBreath.edit();
            editor2.putString("numberOfrepBlue", DEFAULT_REPS);
            editor2.putString("difficultyBlue", DEFAULT_DIFFICULTY);
            editor2.putString("numberOfrepOrange", DEFAULT_REPS);
            editor2.putString("difficultyOrange", DEFAULT_DIFFICULTY);
            editor2.putString("orange", "false");
            editor2.putString("duration", DEFAULT_TIME);
            editor2.putString("date", "1");
            editor2.putString("hour", "1");
            editor2.apply();
            SharedPreferences.Editor editor = spSetApp.edit();
            editor.putString("firstLaunch", "false");
            editor.apply();
        }

        btnStartExercise.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        btnSettings.setOnClickListener(this);


        if (!OpenCVLoader.initDebug())
            Log.e("OpenCV", "Unable to load OpenCV!");
        else {
            Log.d("OpenCV", "OpenCV loaded Successfully!");
        }
    }

    /**
     * go to the relevant page on click
     */
    @Override
    public void onClick(View view) {
        if (view == btnStartExercise)
        {
            Intent intent = new Intent(this, ExercisePage.class);
            startActivity(intent);
        }
        else if (view == btnHistory)
        {
            Intent intent = new Intent(this, HistoryPage.class);
            startActivity(intent);
        }
        else if (view == btnSettings)
        {
            Intent intent = new Intent(this, SettingsPage.class);
            startActivity(intent);
        }
    }
}