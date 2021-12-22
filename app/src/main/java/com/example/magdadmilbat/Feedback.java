package com.example.magdadmilbat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.MagdadMilbat.R;
import com.example.magdadmilbat.database.DatabaseManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.LocalTime;


public class Feedback extends AppCompatActivity implements View.OnClickListener {
    Button btnBackMain2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnBackMain2 = (Button) findViewById(R.id.btnBackMain2);

        btnBackMain2.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View view) {
        if (view == btnBackMain2) {
            LocalDate dateObj = LocalDate.now();
            LocalTime timeObj = LocalTime.now();
            Training exerciseObj = new Training(dateObj.toString(),timeObj.toString(),"נשיפה עמוקה",10,22);
            DatabaseManager dbObj = new DatabaseManager(Feedback.this);
            dbObj.addTraining(exerciseObj);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}