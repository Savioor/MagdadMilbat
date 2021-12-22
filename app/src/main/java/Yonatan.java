package com.example.magdadmilbat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.MagdadMilbat.R;

public class Yonatan extends AppCompatActivity implements View.OnClickListener {
    Button btnBack, btnFeedback, btn_camera;
    TextView tvRepetition, tvExercise;

    /**
     * get premission first time we open the camera  #TODO:לבדוק למה לא עובד
      */
    companion object{
        private const val = CAMERA_PERMISSION_CODE = 1;
        private const val = CAMERA_REQUEST_CODE = 2;
    }

    /**
     * if we have permission - open camera, else ask again for permission
     */
    btn_camera.set.OnClickListener{
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
        else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA)
                    CAMERA_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0]){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }
            else
            {
                Toast.makeText(this,"you denied the permission.", +
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if ((requestCode ==  CAMERA_REQUEST_CODE )) {
                val thumbNail:Bitmap = data!!.extras!!.get("data") as Bitmap
                iv_image.setImageBitmap(thumbNail)
            }
        }
    }

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
