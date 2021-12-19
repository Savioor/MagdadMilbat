package com.example.magdadmilbat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;

public class SettingsPage extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    Button btnBack;
    SeekBar sbLevel, sbTimes;
    TextView tvLevelNumber, tvTimesNumber;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnBack = (Button)findViewById(R.id.btnBack);
        sbLevel = (SeekBar)findViewById(R.id.sbLevel);
        sbTimes = (SeekBar)findViewById(R.id.sbTimes);
        tvLevelNumber = (TextView)findViewById(R.id.tvLevelNumber);
        tvTimesNumber = (TextView)findViewById(R.id.tvTimesNumber);

        btnBack.setOnClickListener(this);
        sbLevel.setOnSeekBarChangeListener(this);
        sbTimes.setOnSeekBarChangeListener(this);
        sbLevel.setMin(1);
        sbTimes.setMin(1);
        sbLevel.setMax(10);
        sbTimes.setMax(10);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar == sbLevel)
        {
            tvLevelNumber.setText(String.valueOf(i));
        }
        else if (seekBar == sbTimes)
        {
            tvTimesNumber.setText(String.valueOf(i));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View view) {
        if (view == btnBack)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}