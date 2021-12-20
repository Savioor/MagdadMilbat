package com.example.magdadmilbat.database.settings;


/**
 * This class is used throughout the code to access the exercise settings
 */
public class SettingsManager {

    private static final String DEFAULT_PROFILE = "default";
    private static SettingsManager instance;

    protected SettingsProfile currentProfile;

    private SettingsManager() {
        currentProfile = SettingsProfile.loadProfile(DEFAULT_PROFILE);
    }

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    public SettingsProfile getCurrentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(String name) {
        this.currentProfile = SettingsProfile.loadProfile(name);
    }
}
