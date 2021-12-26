package com.example.magdadmilbat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.MagdadMilbat.R;

/**
 *  A page that opens a camera during the practices.
 *  # TODO: display practice duration and num of reps
 */
public class ExercisePage extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SearchActivity";
    private static final int REQUEST_CODE = 209;
    Button btnBack, btnBack0, btnFeedback;
    TextView tvRepetition, tvExercise;
    Camera camera;
    FrameLayout cameraFrameLayout;
    ShowCamera showCamera;

    /**
     * on create func - contains return button
     * # TODO: go automatically to feedback when finish practice, else just return to main page.
     * # TODO: add  image processing code.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_page);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnBack0 = (Button) findViewById(R.id.btnBack0);
        btnBack0.setOnClickListener(this); //temporary btnBack button
        btnFeedback = (Button) findViewById(R.id.btnFeedback);
        btnFeedback.setOnClickListener(this);
        tvRepetition = (TextView) findViewById(R.id.tvRepetition);
        tvExercise = (TextView) findViewById(R.id.tvExercise);

        verifyPermissions();
    }

    private void verifyPermissions() {
        Log.d(TAG, "verifyPermissions: asking user for permission");
        String[] permissions = {Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ExercisePage.this,
                    permissions, REQUEST_CODE);
        }
        else{
            // open camera
            cameraFrameLayout = findViewById(R.id.cameraFrameLayout);
            camera = Camera.open();
            showCamera = new ShowCamera(this, camera);
            cameraFrameLayout.addView(showCamera);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

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