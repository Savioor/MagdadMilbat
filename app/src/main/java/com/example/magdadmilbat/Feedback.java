package com.example.magdadmilbat;

import android.content.Intent;
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


public class Feedback extends AppCompatActivity implements View.OnClickListener {
    Button btnBackMain2;
    String greenAirTime,blueAirTime;
    TextView greenTimeText,blueTimeText;
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
        greenTimeText = findViewById(R.id.greenAirTime);
        blueTimeText = findViewById(R.id.blueAirTime);
        greenAirTime = String.valueOf(intent.getExtras().getDouble("greenAirTime"));
        blueAirTime = String.valueOf(intent.getExtras().getDouble("blueAirTime"));
        greenTimeText.setText("זמן באוויר (כדור ירוק) : "+greenAirTime);
        blueTimeText.setText("זמן באוויר (כדור כחול) : "+blueAirTime);
    }

    /**
     * creates Training object and adds to training Database
     * returns to the main page when click on the back button
     */
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