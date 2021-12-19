package com.example.magdadmilbat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.MagdadMilbat.R;
import java.util.ArrayList;

public class HistoryPage extends AppCompatActivity implements View.OnClickListener {
    Button btnBack;
    ListView lvHistory;
    ArrayList<Details> details;
    DetailsAdapter detailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);

        btnBack = (Button)findViewById(R.id.btnBack);
        details = new ArrayList<Details>();
        detailsAdapter = new DetailsAdapter(this, details);
        lvHistory = (ListView)findViewById(R.id.lvHistory);

        lvHistory.setAdapter(detailsAdapter);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}