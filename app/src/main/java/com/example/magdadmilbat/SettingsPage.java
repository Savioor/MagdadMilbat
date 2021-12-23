package com.example.magdadmilbat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;

public class SettingsPage extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    Button btnBack, btnSave;
    SeekBar sbLevel, sbRepetition;
    TextView tvLevelNumber, tvRepetitionNumber;
    EditText etDuration;
    SharedPreferences spBreath;
    int sbLevelNumber = 1; //difficulty level
    int sbRepNumber = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnBack = (Button)findViewById(R.id.btnBack);
        btnSave = (Button)findViewById(R.id.btnSave);
        sbLevel = (SeekBar)findViewById(R.id.sbLevel);
        sbRepetition = (SeekBar)findViewById(R.id.sbRepetition);
        tvLevelNumber = (TextView)findViewById(R.id.tvLevelNumber);
        tvRepetitionNumber = (TextView)findViewById(R.id.tvRepetitionNumber);
        etDuration = (EditText)findViewById(R.id.etDuration);
        //Set listeners
        btnSave.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        sbLevel.setOnSeekBarChangeListener(this);
        sbRepetition.setOnSeekBarChangeListener(this);

        // set the range of the slider (min & max)
        sbLevel.setMin(1);
        sbRepetition.setMin(1);
        sbLevel.setMax(10);
        sbRepetition.setMax(10);

        // Sets the SharedPreferences "settingsBreath"
        spBreath = getSharedPreferences("settingsBreath", 0);
        String numberOfrep = spBreath.getString("numberOfrep",null);
        String difficulty = spBreath.getString("difficulty",null);
        String duration = spBreath.getString("duration",null);

        //If there are already saved settings,display and update the page
        if(numberOfrep != null){
            sbRepNumber = Integer.parseInt(numberOfrep);
            sbRepetition.setProgress(sbRepNumber);
            tvRepetitionNumber.setText(numberOfrep);
        }
        if(difficulty != null){
            tvLevelNumber.setText(difficulty);
            sbLevelNumber = Integer.parseInt(difficulty);
            sbLevel.setProgress(sbLevelNumber);
        }
        if(duration != null){
            etDuration.setText(duration);
        }

    }


//   * returns to the main page when click on the back button.
//   * if btnSave clicked ,save the "settingsBreath" on SharedPreferences.
    @Override
    public void onClick(View view) {

        if (view == btnBack)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        else if (view == btnSave)
        {
            SharedPreferences.Editor editor = spBreath.edit();
            String d = etDuration.getText().toString();
            editor.putString("numberOfrep", String.valueOf(sbRepNumber));
            editor.putString("difficulty", String.valueOf(sbLevelNumber));
            if(d.length() > 0){
                editor.putString("duration", d);
            }
            editor.putString("date", "1");
            editor.putString("hour", "1");
            editor.apply();
        }
    }

    //if slider level change, update the difficulty level setting
    //if slider Repetition change,update the "number Of Repetition" setting
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar == sbLevel)
        {
            tvLevelNumber.setText(String.valueOf(i));
            sbLevelNumber = i;
        }
        else if (seekBar == sbRepetition)
        {
            tvRepetitionNumber.setText(String.valueOf(i));
            sbRepNumber = i;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}