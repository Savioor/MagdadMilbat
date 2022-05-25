package com.example.magdadmilbat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
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
    private ListView lv;
    private TrainingListAdapter adap;
    private ArrayList<Training> trainings;
    private DatabaseManager databaseManager;
    Button btnBack;
    TextView alertTv;
    AlertDialog d;
    int idItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);
        getSupportActionBar().setTitle("היסטוריית אימונים");
        btnBack = findViewById(R.id.btnBack);
        alertTv = findViewById(R.id.alert);
        btnBack.setOnClickListener(this);
        databaseManager = new DatabaseManager(this); //  initialization the database
        SQLiteDatabase sqLiteDatabase = databaseManager.getWritableDatabase();
        sqLiteDatabase.close();
        lv = findViewById(R.id.lvHistory);
        loadHistory();
        lv.setOnItemClickListener(this);
    }

    /**
     * This function gets a training duration and makes a string format
     *
     * @param time Training duration in double
     * @return String on format minute:second
     */
    @SuppressLint("DefaultLocale")
    public static String formatDuration(double time) {
        int roundDuration = (int) Math.round(time);
        String str;
        //Convert units time second and minute
        int sec = ((roundDuration % 864000) % 3600) % 60;
        int min = ((roundDuration % 864000) % 3600) / 60;
        str = String.format("%02d", min) + ":" + String.format("%02d", sec);
        return str;
    }

    /**
     * populate the history page
     */
    private void loadHistory() {
        trainings = databaseManager.getAllTraining();
        // Reverses the order of the list
        Collections.reverse(trainings);
        // If there is no trainings data, display a relevant message
        if (trainings.isEmpty()) {
            alertTv.setText("אין אימונים ברשימה");
            alertTv.setVisibility(View.VISIBLE);
            return;
        }
        adap = new TrainingListAdapter(this, R.layout.list_item, trainings);
        lv.setAdapter(adap);
        lv.setOnItemLongClickListener(this);
    }

    /**
     * This func open the exercise details page
     * All required parameters are transmitted using intent
     *
     * @param i index of item
     */
    @SuppressLint("WrongConstant")
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (trainings.isEmpty()) {
            Toast.makeText(this, "There is no training in the list!", Toast.LENGTH_SHORT).show();
        } else {
            Training curr = trainings.get(i);
            Intent intentDetails = new Intent(this, exercise_details.class);
            int grade = (int) (((1.0 * curr.getTrainingQuality()) / (1.0 * curr.getTarget())) * 100.0);
            grade = Math.min(grade, 100);

            intentDetails.putExtra("quality", String.valueOf(grade));
            intentDetails.putExtra("date", curr.getDate());
            intentDetails.putExtra("duration", formatDuration(curr.getDuration()));
            intentDetails.putExtra("description", curr.getExerciseDescription());
            intentDetails.putExtra("time", curr.getTime());
            intentDetails.putExtra("repDuration", curr.getRepDuration());
            intentDetails.putExtra("repMaxHeight", curr.getRepMaxHeight());
            intentDetails.putExtra("balluse", curr.getBalldata());
            intentDetails.putExtra("targetDuration", curr.getTargetDuration());
            intentDetails.putExtra("targetMH", curr.getTarget());
            startActivity(intentDetails);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            // returns to the main page when click on the back button
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * function that show dialog with an option to delete a workout from the list
     * right button will delete the workout
     * left button will cancel the operation
     */
    public void showAlertDialog() {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setTitle("מחיקת אימון");
        a.setMessage("האם ברצונך למחוק את האימון?");
        a.setCancelable(false);
        a.setPositiveButton("מחק", new HandleAlertDialogListener()); // right
        a.setNegativeButton("ביטול", new HandleAlertDialogListener()); // left
        d = a.create();
        d.show();
    }

    /**
     * func that displays a dialog run when a long click on an item is detected
     *
     * @param position of item
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        idItem = position;
        showAlertDialog();
        return true;
    }

    /**
     * Handles events captured from the buttons in the dialog
     */
    private class HandleAlertDialogListener implements DialogInterface.OnClickListener {
        /**
         * @param which the button that clicked
         */
        @SuppressLint("WrongConstant")
        public void onClick(DialogInterface dialog, int which) {
            if (which == -2) { // -2 cancel the operation
                d.dismiss();
            }
            if (which == -1) { // -1 cancel the operation
                int databaseid = trainings.get(idItem).getId();
                trainings.remove(idItem);
                Toast.makeText(HistoryPage.this,"id:" + idItem + "|db id:"+databaseid,2).show();
                databaseManager.deleteTraining(String.valueOf(databaseid));
                adap = new TrainingListAdapter(HistoryPage.this, R.layout.list_item, trainings);
                lv.setAdapter(adap);
                if (trainings.isEmpty()) {
                    alertTv.setText("אין אימונים ברשימה");
                    alertTv.setVisibility(View.VISIBLE);
                    return;
                }
                d.dismiss();
            }
        }
    }
}