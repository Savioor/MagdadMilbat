package com.example.magdadmilbat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.MagdadMilbat.R;

public class ExrChoiceScreen extends AppCompatActivity implements View.OnClickListener {
    Button btnBack, btnCheeks, btnKiss, btnOpenMouth, btnSmile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_exr_choice_screen);

        btnBack = (Button)findViewById(R.id.btnBack);
        btnSmile = (Button)findViewById(R.id.btnSmile);
        btnOpenMouth = (Button)findViewById(R.id.btnOpenMouth);
        btnKiss = (Button)findViewById(R.id.btnKiss);
        btnCheeks = (Button)findViewById(R.id.btnCheeks);

        this.btnBack.setOnClickListener(this);
        this.btnSmile.setOnClickListener(this);
        this.btnOpenMouth.setOnClickListener(this);
        this.btnKiss.setOnClickListener(this);
        this.btnCheeks.setOnClickListener(this);

    }

    public void onClick(View view) {
        if (view == this.btnBack)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if (view == this.btnSmile)
        {
            Intent intent = new Intent(this, ExercisePage.class);
            intent.putExtra("exercise", "חיוך");
            startActivity(intent);
        }
        else if (view == this.btnOpenMouth)
        {
            Intent intent = new Intent(this, ExercisePage.class);
            intent.putExtra("exercise", "פה גדול");
            startActivity(intent);
        }
        else if (view == this.btnKiss)
        {
            Intent intent = new Intent(this, ExercisePage.class);
            intent.putExtra("exercise", "נשיקה");
            startActivity(intent);
        }
        else if (view == this.btnCheeks)
        {
            Intent intent = new Intent(this, ExercisePage.class);
            intent.putExtra("exercise", "ניפוח לחיים");
            startActivity(intent);
        }
    }
}
