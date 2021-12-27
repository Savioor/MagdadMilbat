package com.example.magdadmilbat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;

public class SettingsChoiceScreen extends AppCompatActivity implements View.OnClickListener {
    Button btnBack, btnSmile, btnOpenMouth, btnKiss, btnCheeks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exr_choice_screen);

        btnBack = (Button)findViewById(R.id.btnBack);
        btnSmile = (Button)findViewById(R.id.btnSmile);
        btnOpenMouth = (Button)findViewById(R.id.btnOpenMouth);
        btnKiss = (Button)findViewById(R.id.btnKiss);
        btnCheeks = (Button)findViewById(R.id.btnCheeks);

        btnBack.setOnClickListener(this);
        btnSmile.setOnClickListener(this);
        btnOpenMouth.setOnClickListener(this);
        btnKiss.setOnClickListener(this);
        btnCheeks.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if (view == btnSmile)
        {
            Intent intent = new Intent(this, SettingsPage.class);
            intent.putExtra("exercise", "הגדרות חיוך");
            intent.putExtra("exercise sp", "settings smile");
            startActivity(intent);
        }
        else if (view == btnOpenMouth)
        {
            Intent intent = new Intent(this, SettingsPage.class);
            intent.putExtra("exercise", "הגדרות פה גדול");
            intent.putExtra("exercise sp", "settings open mouth");
            startActivity(intent);
        }
        else if (view == btnKiss)
        {
            Intent intent = new Intent(this, SettingsPage.class);
            intent.putExtra("exercise", "הגדרות נשיקה");
            intent.putExtra("exercise sp", "settings kiss");
            startActivity(intent);
        }
        else if (view == btnCheeks)
        {
            Intent intent = new Intent(this, SettingsPage.class);
            intent.putExtra("exercise", "הגדרות ניפוח לחיים");
            intent.putExtra("exercise sp", "settings cheeks");
            startActivity(intent);
        }
    }
}
