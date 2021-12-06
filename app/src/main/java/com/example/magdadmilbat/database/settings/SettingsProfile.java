package com.example.magdadmilbat.database.settings;

import java.util.HashMap;
import java.util.List;

/**
 * This class holds the settings of an exercise. This data is saved in a array of size MAX_SETTINGS.
 * For example
 * index 1 -> difficulty
 * index 2 -> amount of repetitions
 */
public class SettingsProfile {

    public static final int MAX_SETTINGS = 10;

    /**
     * map of name to profile of all profiles currently loaded in the code.
     * no more than one profile with the same name must be loaded at any given moments to avoid
     * de-syncing
     */
    private static HashMap<String, SettingsProfile> LOADED_PROFILES;

    /**
     * unique name for setting. For example "BigSmileProfile1"
     */
    protected String name;
    /**
     * The list of the setting as described in the class documentation
     */
    protected double[] settings;

    /**
     *
     * @param name
     * @param doubles
     */
    private SettingsProfile(String name, List<Double> doubles) {

    }

    /**
     *
     * @param index
     * @return The setting at the current index
     */
    public double getSetting(int index) {
        return 0;
    }

    /**
     *
     * @param index
     * @param value
     */
    public void setSetting(int index, double value) {

    }

    /**
     * Save this setting to memory. If this setting already exists in memory (setting with the same
     * name) then edit it.
     */
    public void saveToMemory() {

    }

    /**
     * Load a profile from LOADED_PROFILES or from memory if it's not loaded.
     * If you loaded from memory add it to LOADED_PROFILES.
     * If it isn't in memory or LOADED_PROFILES then create a default profile and load it into
     * LOADED_PROFILES (but not into memory).
     * @param name
     * @return The setting profile with a given name
     */
    public static SettingsProfile loadProfile(String name) {
        return null;
    }


}
