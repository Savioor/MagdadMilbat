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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;

public class SettingsPage extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    Button btnBack, btnBreath, btnSave;
    SeekBar sbLevel, sbRepetition;
    TextView tvLevelNumber, tvRepetitionNumber;
    EditText etDuration;
    SharedPreferences spBreath;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnBack = (Button)findViewById(R.id.btnBack);

        btnBreath = (Button)findViewById(R.id.btnBreath);
//        btnOpenMouth = (Button)findViewById(R.id.btnOpenMouth);
//        btnKiss = (Button)findViewById(R.id.btnKiss);
//        btnCheeks = (Button)findViewById(R.id.btnCheeks);

        sbLevel = (SeekBar)findViewById(R.id.sbLevel);
        sbRepetition = (SeekBar)findViewById(R.id.sbRepetition);
        tvLevelNumber = (TextView)findViewById(R.id.tvLevelNumber);
        tvRepetitionNumber = (TextView)findViewById(R.id.tvRepetitionNumber);
        etDuration = (EditText)findViewById(R.id.etDuration);

        btnBack.setOnClickListener(this);

        btnBreath.setOnClickListener(this);
//        btnOpenMouth.setOnClickListener(this);
//        btnKiss.setOnClickListener(this);
//        btnCheeks.setOnClickListener(this);

        sbLevel.setOnSeekBarChangeListener(this);
        sbRepetition.setOnSeekBarChangeListener(this);
        sbLevel.setMin(1);
        sbRepetition.setMin(1);
        sbLevel.setMax(10);
        sbRepetition.setMax(10);

        spBreath = getSharedPreferences("settings breath", 0);
//        spOpenMouth = getSharedPreferences("settings open mouth", 0);
//        spKiss = getSharedPreferences("settings kiss", 0);
//        spCheeks = getSharedPreferences("settings cheeks", 0);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = spBreath.edit();

        if (view == btnBack)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if (view == btnBreath)
        {
            editor = spBreath.edit();
        }
//        else if (view == btnKiss)
//        {
//            editor = spKiss.edit();
//        }
//        else if (view == btnCheeks)
//        {
//            editor = spCheeks.edit();
//        }

        else if (view == btnSave)
        {
            editor.putString("מספר חזרות", "1");
            editor.putString("קושי", "1");
            editor.putString("משך זמן", "1");
            editor.putString("תאריך", "1");
            editor.putString("שעה", "1");
            editor.apply();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar == sbLevel)
        {
            tvLevelNumber.setText(String.valueOf(i));
        }
        else if (seekBar == sbRepetition)
        {
            tvRepetitionNumber.setText(String.valueOf(i));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}