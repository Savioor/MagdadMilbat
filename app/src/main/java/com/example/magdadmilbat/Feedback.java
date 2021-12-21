package com.example.magdadmilbat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.MagdadMilbat.R;

public class Feedback extends AppCompatActivity implements View.OnClickListener {
    Button btnBackMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnBackMain = (Button) findViewById(R.id.btnFeedback);

        btnBackMain.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view == btnBackMain) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}