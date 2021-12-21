package com.example.magdadmilbat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.MagdadMilbat.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnStartExercise, btnHistory, btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartExercise = (Button)findViewById(R.id.btnStartExercise);
        btnHistory = (Button)findViewById(R.id.btnHistory);
        btnSettings = (Button)findViewById(R.id.btnFeedback);

        btnStartExercise.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
    }

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