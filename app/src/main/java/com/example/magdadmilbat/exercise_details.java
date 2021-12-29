package com.example.magdadmilbat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.MagdadMilbat.R;

public class exercise_details extends AppCompatActivity implements View.OnClickListener {
TextView tvDuration,tvTime,tvRepetition,tvLevel,tvTitle;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);
        Intent intent=getIntent();
        btnBack = findViewById(R.id.btnBack);
        tvDuration = findViewById(R.id.tvDuration);
        tvLevel = findViewById(R.id.tvLevel);
        tvTime = findViewById(R.id.tvTime);
        tvTitle = findViewById(R.id.tvTitle);
        tvRepetition = findViewById(R.id.tvRepetition);
        btnBack.setOnClickListener(this);

        tvDuration.setText(String.valueOf(intent.getExtras().getString("duration")));
        tvTitle.setText(String.valueOf(intent.getExtras().getString("description")));
        tvRepetition.setText(String.valueOf(intent.getExtras().getString("date")));
        tvTime.setText(String.valueOf(intent.getExtras().getString("time")));
        tvLevel.setText(String.valueOf(intent.getExtras().getString("quality")));
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack)
        {
            Intent intent = new Intent(this, HistoryPage.class);
            startActivity(intent);
        }
    }
}