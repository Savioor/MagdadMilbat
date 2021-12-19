package com.example.magdadmilbat;

public class Details {
    private String date;
    private String time;
    private int quality;
    private String exercise;

    public Details(String date, String time, int quality, String exercise) {
        this.date = date;
        this.time = time;
        this.quality = quality;
        this.exercise = exercise;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }
}
