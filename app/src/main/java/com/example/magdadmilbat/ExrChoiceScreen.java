package com.example.magdadmilbat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.magdadmilbat.database.settings.SettingsManager;

public class ExrChoiceScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exr_choice_screen);
    }

    /**
     * Load a setting profile and go to the exercise screen
     * @param profileToLoad
     */
    private void goToExrScreen(String profileToLoad){
        SettingsManager manager = SettingsManager.getInstance();

    }

}