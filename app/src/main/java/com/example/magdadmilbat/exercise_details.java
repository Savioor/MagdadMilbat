package com.example.magdadmilbat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.MagdadMilbat.R;

import java.util.ArrayList;

public class exercise_details extends AppCompatActivity implements View.OnClickListener {
TextView tvDuration,tvTime,tvRepetition,tvLevel,tvTitle;
String repDuration,repMaxHeight;
    Button btnBack;
    TextView alertrep;
    String [] arrRepDuration;
    String [] arrRepMaxHeight;
    ArrayList<Repetition> repsList = new ArrayList<>();//ArrayList for Repetition
    ListView lvReps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent intent=getIntent();
        btnBack = findViewById(R.id.btnBack);
        tvDuration = findViewById(R.id.tvDuration);
        tvLevel = findViewById(R.id.tvLevel);
        tvTime = findViewById(R.id.tvTime);
        tvTitle = findViewById(R.id.tvTitle);
        tvRepetition = findViewById(R.id.tvRepetition);
        lvReps = (ListView) findViewById(R.id.lvreps);
        alertrep = findViewById(R.id.alertrep);
        btnBack.setOnClickListener(this);
        repDuration = intent.getExtras().getString("repDuration");
        repMaxHeight = intent.getExtras().getString("repMaxHeight");
        buildListReps();

        //set the data to TextViews that we sent from History page
        tvDuration.setText(String.valueOf(intent.getExtras().getString("duration")));
        tvTitle.setText(String.valueOf(intent.getExtras().getString("description")));
        tvRepetition.setText(String.valueOf(intent.getExtras().getString("date")));
        tvTime.setText(String.valueOf(intent.getExtras().getString("time")));
        tvLevel.setText(String.valueOf(intent.getExtras().getString("quality")));
    }

    /**
     * function that create list of repetitions and load to selected exercise
     */
    public void buildListReps(){
        if(repDuration.length() == 0){
            //If there is no repetition data, display a relevant message
            alertrep.setText("לא בוצעו חזרות");
            alertrep.setVisibility(View.VISIBLE);
            return;
        }
        arrRepDuration = repDuration.split(",");
        arrRepMaxHeight = repMaxHeight.split(",");
        //Create a new object for each repetition
        for(int i = 1;i<arrRepDuration.length;i++){
            Repetition rep = new Repetition(i,Double.parseDouble(arrRepDuration[i]) /10.0,Integer.parseInt(arrRepMaxHeight[i]));
            repsList.add(rep);
        }
        RepsListAdapter repsadap = new RepsListAdapter(this, R.layout.list_reps_item, repsList);
        lvReps.setAdapter(repsadap);
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack)
        {
            //return to HistoryPage
            Intent intent = new Intent(this, HistoryPage.class);
            startActivity(intent);
        }
    }
}