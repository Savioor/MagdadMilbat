package com.example.magdadmilbat;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;
import com.example.magdadmilbat.database.DatabaseManager;

import java.util.ArrayList;

public class HistoryPage extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private Intent go;
    private ListView lv;
    private TrainingListAdapter adap;
    private ArrayList<Training> trainings;
    private String[] trainingHistory;
    private Training curr;
    private DatabaseManager databaseManager;
    private SQLiteDatabase sqLiteDatabase;
    Button btnBack;
    ListView lvHistory;
    ArrayList<Details> details;
//    DetailsAdapter detailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);
        btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        databaseManager = new DatabaseManager(this); //  initialization the database
        sqLiteDatabase = databaseManager.getWritableDatabase();
        sqLiteDatabase.close();
        //databaseManager.addTraining(new Training("18/1/2002", "2:00", "Breathe", 8)); --> Test if the function work and we can show the data on the list
        lv = (ListView) findViewById(R.id.lvHistory);
        loadHistory();
        lv.setOnItemClickListener(this);
    }

    /**
     * populate the history page
     */
    private void loadHistory() {
        trainings = databaseManager.getAllTraining();
        arraylistToStringArray();
        //creating the list view
        adap = new TrainingListAdapter(this, R.layout.list_item, trainings);
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

    // checks if training data are empty,And displays a message
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(trainings.isEmpty())
        {
            Toast.makeText(this, "There is no training in the list!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            curr = trainings.get(i);
            Intent intentDetails = new Intent(this, exercise_details.class);
            intentDetails.putExtra("quality",String.valueOf(curr.getTrainingQuality()));
            intentDetails.putExtra("date",curr.getDate());
            intentDetails.putExtra("description",curr.getExerciseDescription());
            intentDetails.putExtra("time",curr.getTime());
            intentDetails.putExtra("duration",String.valueOf(curr.getDuration()));
            startActivity(intentDetails);
        }
    }

     //returns to the main page when click on the back button
    @Override
    public void onClick(View view) {
        if (view == btnBack)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}