package com.example.magdadmilbat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final boolean GO_TO_EXR_CHOICE = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void switchToSettings() {
        switchActivity(SettingsPage.class);
    }
    private void switchToHistory() {
        switchActivity(HistoryPage.class);
    }
    private void switchToExr() {
        if (GO_TO_EXR_CHOICE) {
            switchActivity(ExrChoiceScreen.class);
        } else {
            switchActivity(ExercisePage.class);
        }
    }

    private void switchActivity(Class<?> cls) {
        Intent switchActivityIntent = new Intent(this, cls);
        startActivity(switchActivityIntent);
    }

}