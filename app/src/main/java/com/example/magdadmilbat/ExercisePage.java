package com.example.magdadmilbat;

import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;

/**
 *  A page that opens a camera during the practices.
 *  # TODO: display practice duration and num of reps
 */
public class ExercisePage extends AppCompatActivity implements View.OnClickListener {
    Button btnBack, btnFeedback;
    TextView tvRepetition, tvExercise;

    /**
     * on create func - contains return button
     * # TODO: go automatically to feedback when finish practice, else just return to main page.
     * # TODO : add camera frame
     * # TODO: add  image processing code
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_page);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnFeedback = (Button) findViewById(R.id.btnFeedback);
        btnFeedback.setOnClickListener(this);
        tvRepetition = (TextView) findViewById(R.id.tvRepetition);
        tvExercise = (TextView) findViewById(R.id.tvExercise);

    }
    /**
     * returns to the main page when click on the back button
     * # TODO: delete feedback button
     */
    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        if (view == btnFeedback) {
            Intent intent = new Intent(this, Feedback.class);
            startActivity(intent);
        }
    }
}