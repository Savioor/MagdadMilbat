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
TextView tvDuration,tvTime,tvRepetition,tvLevel,tvTitle,orangeTimeText,blueTimeText,tvorangeMaxHeight;
String repDuration,repMaxHeight;
    Button btnBack;
    TextView alertrep;
    String [] arrRepDuration;
    String [] arrRepMaxHeight;
    ArrayList<Repetition> repsList = new ArrayList<>();
    ListView lvReps;
    private RepsListAdapter repsadap;
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

        tvDuration.setText(String.valueOf(intent.getExtras().getString("duration")));
        tvTitle.setText(String.valueOf(intent.getExtras().getString("description")));
        tvRepetition.setText(String.valueOf(intent.getExtras().getString("date")));
        tvTime.setText(String.valueOf(intent.getExtras().getString("time")));
        tvLevel.setText(String.valueOf(intent.getExtras().getString("quality")));
    }

    public void buildListReps(){
        if(repDuration.length() == 0){
            alertrep.setText("לא בוצעו חזרות");
            alertrep.setVisibility(View.VISIBLE);
            return;
        }
        arrRepDuration = repDuration.split(",");
        arrRepMaxHeight = repMaxHeight.split(",");
        for(int i = 0;i<arrRepDuration.length;i++){
            Repetition rep = new Repetition(i+1,arrRepDuration[i],Integer.parseInt(arrRepMaxHeight[i]));
            repsList.add(rep);
        }
        repsadap = new RepsListAdapter(this, R.layout.list_item, repsList);
        lvReps.setAdapter(repsadap);
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