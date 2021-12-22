package com.example.magdadmilbat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.MagdadMilbat.R;
import androidx.appcompat.app.AppCompatActivity;

/**
 * A page that opens on the end of the practice, with a feedback to the user
 */
public class Feedback extends AppCompatActivity implements View.OnClickListener {
    Button btnBackMain2;

    /**
     * on create func - contains  feedback text, return button
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnBackMain2 = (Button) findViewById(R.id.btnBackMain2);

        btnBackMain2.setOnClickListener(this);
    }

    /**
     * returns to the main page when click on the back button
     */
    public void onClick(View view) {
        if (view == btnBackMain2) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}