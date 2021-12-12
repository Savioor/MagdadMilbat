package com.example.magdadmilbat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HistoryPage extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private Intent go;
    private ListView lv;
    private ArrayAdapter adap;
    private ArrayList<Training> trainings;
    private String[] trainingHistory;
    private String curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);
        lv = (ListView) findViewById(R.id.lvHistory);
        loadHistory();
        curr = "None";
        lv.setOnItemClickListener(this);
    }

    /**
     * populate the history page
     */
    private void loadHistory() {
        trainings = new ArrayList<>();//TODO: Get data  from Database
        arraylistToStringArray();
        adap = new ArrayAdapter(this, android.R.layout.simple_list_item_1, trainingHistory);
        lv.setAdapter(adap);
    }
    /**
     * This function create string array
     */
    private void arraylistToStringArray()
    {
        if (trainings.size() == 0) {
            trainingHistory = new String[1];
            trainingHistory[0] = "There is no training in the list!";
        }
        else {
            trainingHistory = new String[trainings.size()];
            for (int i = 0; i < trainings.size(); i++) {
                trainingHistory[i] = trainings.get(i).toString();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(trainings.isEmpty())
        {
            Toast.makeText(this, "There is no training in the list!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            curr = trainingHistory[i];
            Toast.makeText(this, curr, Toast.LENGTH_SHORT).show();
        }
    }
}