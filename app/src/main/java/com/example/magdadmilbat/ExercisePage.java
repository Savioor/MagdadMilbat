package com.example.magdadmilbat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExercisePage extends AppCompatActivity implements View.OnClickListener {
    Button btnBack;
    TextView tvRepetition, tvExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_page);

        btnBack = (Button) findViewById(R.id.btnBack);
        tvRepetition = (TextView) findViewById(R.id.tvRepetition);
        tvExercise = (TextView) findViewById(R.id.tvExercise);
        btnBack.setOnClickListener(this);
        tvExercise.setText(getIntent().getStringExtra("exercise"));

    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}