package com.example.magdadmilbat;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;

public class ExercisePage extends AppCompatActivity implements View.OnClickListener {
    Button btnBack, btnFeedback;
    TextView tvRepetition, tvExercise;

    private SurfaceTexture previewFrameTexture;
    private SurfaceView previewDisplayView;


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

        tvExercise.setText(getIntent().getStringExtra("exercise"));



        previewDisplayView = new SurfaceView(this);
        setupPreviewDisplayView();

//        PermissionHelper.checkAndRequestCameraPermissions(this);
//
//        glSurfaceView.setVisibility(View.VISIBLE);

//        cameraInput.start(this, faceMesh.getGlContext(), CameraInput.CameraFacing.FRONT,
//                1024, 768);

    }

    private void setupPreviewDisplayView() {
//            previewDisplayView.setVisibility(View.GONE);
//            ViewGroup viewGroup = findViewById(R.id.preview_display_layout);
//            viewGroup.addView(previewDisplayView);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (PermissionHelper.cameraPermissionsGranted(this)) {
//            startCamera();
//        }
    }


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