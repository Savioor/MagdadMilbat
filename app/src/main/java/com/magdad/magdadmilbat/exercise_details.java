package com.magdad.magdadmilbat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;

import java.util.ArrayList;

public class exercise_details extends AppCompatActivity implements View.OnClickListener {
    TextView tvDuration, tvTime, tvRepetition, tvLevel, tvTitle, tvballuse;
    String repDuration, repMaxHeight;
    Button btnBack;
    TextView alertrep;
    String[] arrRepDuration;
    String[] arrRepMaxHeight;
    ArrayList<Repetition> repsList = new ArrayList<>();//ArrayList for Repetition
    ListView lvReps;
    String strballuse = "";
    SharedPreferences spBreath;
    double targetDuration;
    int targetMH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent intent = getIntent();
        btnBack = findViewById(R.id.btnBack);
        tvDuration = findViewById(R.id.tvDuration);
        tvLevel = findViewById(R.id.tvLevel);
        tvTime = findViewById(R.id.tvTime);
        tvTitle = findViewById(R.id.tvTitle);
        tvRepetition = findViewById(R.id.tvRepetition);
        lvReps = findViewById(R.id.lvreps);
        alertrep = findViewById(R.id.alertrep);
        tvballuse = findViewById(R.id.balluse);
        btnBack.setOnClickListener(this);
        repDuration = intent.getExtras().getString("repDuration");
        repMaxHeight = intent.getExtras().getString("repMaxHeight");
        targetDuration = intent.getExtras().getDouble("targetDuration");
        targetMH = intent.getExtras().getInt("targetMH");
        buildListReps();

        //set the data to TextViews that we sent from History page
        tvDuration.setText(String.valueOf(intent.getExtras().getString("duration")));
        tvTitle.setText(String.valueOf(intent.getExtras().getString("description")));
        tvRepetition.setText(String.valueOf(intent.getExtras().getString("date")));
        tvTime.setText(String.valueOf(intent.getExtras().getString("time")));
        tvLevel.setText(String.valueOf(intent.getExtras().getString("quality")));
        int balluse = intent.getExtras().getInt("balluse");
        if (balluse == 3) {
            strballuse = "כחול";
            tvballuse.setTextColor(getResources().getColor(R.color.blue));
        }
        if (balluse == 2) {
            strballuse = "כתום";
            tvballuse.setTextColor(getResources().getColor(R.color.orange));
        }
        tvballuse.setText("הנתונים מתייחסים לכדור ה" + strballuse);

    }

    /**
     * function that create list of repetitions and load to selected exercise
     */
    public void buildListReps() {
        if (repDuration.length() == 0) {
            //If there is no repetition data, display a relevant message
            alertrep.setText("לא בוצעו חזרות");
            alertrep.setVisibility(View.VISIBLE);
            return;
        }
        arrRepDuration = repDuration.split(",");
        arrRepMaxHeight = repMaxHeight.split(",");
        double repD;
        int repMH;
        //Create a new object for each repetition
        for (int i = 1; i < arrRepDuration.length; i++) {
            boolean target = false;
            repD = Double.parseDouble(arrRepDuration[i]);
            repMH = Integer.parseInt(arrRepMaxHeight[i]);
            target = repD >= targetDuration && repMH >= targetMH;
            Repetition rep = new Repetition(i, repD, repMH,target);
            repsList.add(rep);
        }
        RepsListAdapter repsadap = new RepsListAdapter(this, R.layout.list_reps_item, repsList);
        lvReps.setAdapter(repsadap);
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            //return to HistoryPage
            Intent intent = new Intent(this, HistoryPage.class);
            startActivity(intent);
        }
    }
}