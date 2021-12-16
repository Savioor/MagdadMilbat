package com.example.magdadmilbat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ExercisePage extends AppCompatActivity {

    companion bobject {
        private const val camara_permission_code = 1;
        private const val camara = 2;
    }
    
    
    override fun onCreate(savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_page);
        
        btn_camera.setOnClickListener {it:View!
            if(ContextCompat.checkselfpermission(context:this,Manifast.permission.camera)
               == PackageManager.PERMISSION_GRANTED)
            {
             val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            
    }
    
    
}
