package com.example.magdadmilbat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.MagdadMilbat.R;
import com.example.magdadmilbat.ExercisePage;
import com.example.magdadmilbat.MainActivity;

public class Feedback extends AppCompatActivity implements View.OnClickListener {
    Button btnBackMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnBackMain = (Button) findViewById(R.id.btnSettings);

        btnBackMain.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view == btnBackMain) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}