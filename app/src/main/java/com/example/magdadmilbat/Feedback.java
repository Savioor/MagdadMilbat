package com.example.magdadmilbat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.MagdadMilbat.R;
import androidx.appcompat.app.AppCompatActivity;


public class Feedback extends AppCompatActivity implements View.OnClickListener {
    Button btnBackMain2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnBackMain2 = (Button) findViewById(R.id.btnBackMain2);

        btnBackMain2.setOnClickListener(this);

    }

    public void onClick(View view) {
        if (view == btnBackMain2) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}