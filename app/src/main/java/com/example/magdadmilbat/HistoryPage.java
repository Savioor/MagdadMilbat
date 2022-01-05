package com.example.magdadmilbat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;
import com.example.magdadmilbat.database.DatabaseManager;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryPage extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {
    private Intent go;
    private ListView lv;
    private TrainingListAdapter adap;
    private ArrayList<Training> trainings;
    private Training curr;
    private DatabaseManager databaseManager;
    private SQLiteDatabase sqLiteDatabase;
    Button btnBack;
    TextView alertTv;
    AlertDialog d;
    int idItem ;
    ListView lvHistory;
    ArrayList<Details> details;
//    DetailsAdapter detailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);
        btnBack = (Button)findViewById(R.id.btnBack);
        alertTv = findViewById(R.id.alert);
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
        Collections.reverse(trainings);
        if(trainings.isEmpty()){
            alertTv.setText("אין אימונים ברשימה");
            alertTv.setVisibility(View.VISIBLE);
            return;
        }
        adap = new TrainingListAdapter(this, R.layout.list_item, trainings);
        lv.setAdapter(adap);
        lv.setOnItemLongClickListener(this);
    }

    // checks if training data are empty,And displays a message
    @SuppressLint("WrongConstant")
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
            intentDetails.putExtra("blueAirTime",String.valueOf(curr.getArrBalls()[1].getDurationInAir()));
            intentDetails.putExtra("orangeAirTime",String.valueOf(curr.getArrBalls()[2].getDurationInAir()));
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

    public void showAlertDialog(){
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setTitle("מחיקת אימון");
        a.setMessage("האם ברצונך למחוק את האימון?");
        a.setCancelable(false);
        a.setPositiveButton("מחק", new HandleAlertDialogListener()); // ימין
        a.setNegativeButton("ביטול", new HandleAlertDialogListener()); // שמאל
        d=a.create();
        d.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        idItem = position;
        showAlertDialog();
        return false;
    }

    private class HandleAlertDialogListener implements DialogInterface.OnClickListener {
        @SuppressLint("WrongConstant")
        public void onClick(DialogInterface dialog, int which) {
            if(which == -2){
                d.dismiss();
            }
            if(which == -1){
//                Toast.makeText(HistoryPage.this,String.valueOf(idItem),2).show();
//                databaseManager.deleteTraining(String.valueOf(idItem));
//                loadHistory();
                trainings.remove(idItem);
                adap = new TrainingListAdapter(HistoryPage.this, R.layout.list_item, trainings);
                lv.setAdapter(adap);
                d.dismiss();
            }
        }
}
}