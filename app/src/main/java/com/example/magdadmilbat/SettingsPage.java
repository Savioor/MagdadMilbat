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
    TextView tvExercise, tvLevelNumber, tvRepetitionNumber;
    EditText etDuration;
    SharedPreferences sp;
    int level, repetition;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView tvExercise = (TextView) findViewById(R.id.tvExercise);
        tvExercise.setText(getIntent().getStringExtra("exercise"));

        btnBack = (Button)findViewById(R.id.btnBack);
        btnSave = (Button)findViewById(R.id.btnSave);

        sbLevel = (SeekBar)findViewById(R.id.sbLevel);
        sbRepetition = (SeekBar)findViewById(R.id.sbRepetition);
        tvLevelNumber = (TextView)findViewById(R.id.tvLevelNumber);
        tvRepetitionNumber = (TextView)findViewById(R.id.tvRepetitionNumber);
        etDuration = (EditText)findViewById(R.id.etDuration);

        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        sbLevel.setOnSeekBarChangeListener(this);
        sbRepetition.setOnSeekBarChangeListener(this);
        sbLevel.setMin(1);
        sbRepetition.setMin(1);
        sbLevel.setMax(10);
        sbRepetition.setMax(10);

        sp = getSharedPreferences(getIntent().getStringExtra("exercise sp"), 0);
        loadSP(sp);
    }

    @Override
    public void onClick(View view)
    {
        if (view == btnBack)
        {
            Intent intent = new Intent(this, SettingsChoiceScreen.class);
            startActivity(intent);
        }
        else if (view == btnSave)
        {
            saveSP(sp);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar == sbLevel)
        {
            tvLevelNumber.setText(String.valueOf(i));
            level = i;
        }
        else if (seekBar == sbRepetition)
        {
            tvRepetitionNumber.setText(String.valueOf(i));
            repetition = i;
        }
    }

    public void loadSP(SharedPreferences sp)
    {
        this.sbLevel.setProgress(Integer.parseInt(sp.getString("level", "1")));
        this.sbRepetition.setProgress(Integer.parseInt(sp.getString("repetition", "1")));
        this.etDuration.setText(sp.getString("duration", null));
    }

    public void saveSP(SharedPreferences sp)
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("level", String.valueOf(level));
        editor.putString("repetition", String.valueOf(repetition));
        editor.putString("duration", etDuration.getText().toString());
        editor.apply();
        Toast.makeText(this, "ההגדרות נשמרו", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}