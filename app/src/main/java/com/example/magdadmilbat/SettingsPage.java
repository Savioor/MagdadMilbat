package com.example.magdadmilbat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.magdadmilbat.database.settings.SettingsManager;

public class SettingsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    /**
     * Return to the main menu
     */
    private void backToMain() {

    }

    /**
     * Load the data from the given profile into the UI elements
     * @param profile
     */
    private void loadData(String profile) {
        SettingsManager manager = SettingsManager.getInstance();
    }

    /**
     * Save the date from the UI elements to memory (both code memory [RAM] and the phone memory
     * [hard disk])
     * @param profile
     */
    private void saveData(String profile) {
        SettingsManager manager = SettingsManager.getInstance();
    }

}