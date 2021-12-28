package com.example.magdadmilbat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;

public class SettingsPage extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    Button btnBack, btnSave;
    SeekBar sbLevel, sbRepetition;
    TextView tvLevelNumber, tvRepetitionNumber;
    EditText etDuration;
    RadioGroup rg1;
    RadioButton b1,b2;
    boolean blue = true,orange = false;
    SharedPreferences spBreath;
    int sbLevelNumberBlue = 1; //difficulty level
    int sbRepNumberBlue = 1;
    int sbLevelNumberOrange = 1;
    int sbRepNumberOrange = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnBack = (Button)findViewById(R.id.btnBack);
        btnSave = (Button)findViewById(R.id.btnSave);
        sbLevel = (SeekBar)findViewById(R.id.sbLevel);
        b1 = findViewById(R.id.blue);
        b2 = findViewById(R.id.orange);
        rg1 = findViewById(R.id.rg1);
        sbRepetition = (SeekBar)findViewById(R.id.sbRepetition);
        tvLevelNumber = (TextView)findViewById(R.id.tvLevelNumber);
        tvRepetitionNumber = (TextView)findViewById(R.id.tvRepetitionNumber);
        etDuration = (EditText)findViewById(R.id.etDuration);
        b1.setChecked(true);
        //Set listeners
        btnSave.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        rg1.setOnCheckedChangeListener(this);
        sbLevel.setOnSeekBarChangeListener(this);
        sbRepetition.setOnSeekBarChangeListener(this);
        // set the range of the slider (min & max)
        sbLevel.setMin(1);
        sbRepetition.setMin(1);
        sbLevel.setMax(10);
        sbRepetition.setMax(10);

        // Sets the SharedPreferences "settingsBreath"
        spBreath = getSharedPreferences("settingsBreath", 0);
        String numberOfrepOrange = spBreath.getString("numberOfrepOrange",null);
        String numberOfrepBlue = spBreath.getString("numberOfrepBlue",null);
        String difficultyOrange = spBreath.getString("difficultyOrange",null);
        String difficultyBlue = spBreath.getString("difficultyBlue",null);
        String duration = spBreath.getString("duration",null);

        //If there are already saved settings,display and update the page
        if(numberOfrepBlue != null){
            sbRepNumberBlue = Integer.parseInt(numberOfrepBlue);
            sbRepetition.setProgress(sbRepNumberBlue);
            tvRepetitionNumber.setText(numberOfrepBlue);
        }
        if(difficultyBlue != null){
            tvLevelNumber.setText(difficultyBlue);
            sbLevelNumberBlue = Integer.parseInt(difficultyBlue);
            sbLevel.setProgress(sbLevelNumberBlue);
        }
        if(numberOfrepOrange != null){
            sbRepNumberOrange = Integer.parseInt(numberOfrepOrange);
        }
        if(difficultyOrange != null){
            sbLevelNumberOrange = Integer.parseInt(difficultyOrange);
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
            editor.putString("numberOfrepBlue", String.valueOf(sbRepNumberBlue));
            editor.putString("difficultyBlue", String.valueOf(sbLevelNumberBlue));
            editor.putString("numberOfrepOrange", String.valueOf(sbRepNumberOrange));
            editor.putString("difficultyOrange", String.valueOf(sbLevelNumberOrange));
            if(d.length() > 0){
                editor.putString("duration", d);
            }
            editor.putString("date", "1");
            editor.putString("hour", "1");
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    //if slider level change, update the difficulty level setting
    //if slider Repetition change,update the "number Of Repetition" setting
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar == sbLevel)
        {
            tvLevelNumber.setText(String.valueOf(i));
            if(orange){
                sbLevelNumberOrange = i;
            }else if(blue){
                sbLevelNumberBlue = i;
            }
        }
        else if (seekBar == sbRepetition)
        {
            tvRepetitionNumber.setText(String.valueOf(i));
            if(orange){
                sbRepNumberOrange = i;
            }else if(blue){
                sbRepNumberBlue = i;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(rg1 == group){
            blue = checkedId == R.id.blue;
            orange = checkedId == R.id.orange;
            if(blue){
                b2.setChecked(false);
                b1.setChecked(true);
                sbLevel.setProgress(sbLevelNumberBlue);
                sbRepetition.setProgress(sbRepNumberBlue);
            }
            if(orange){
                b1.setChecked(false);
                b2.setChecked(true);
                sbLevel.setProgress(sbLevelNumberOrange);
                sbRepetition.setProgress(sbRepNumberOrange);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}