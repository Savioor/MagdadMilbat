package com.example.magdadmilbat;

public class Details {
    private String exercise;
    private String date;
    private String time;
    private int level;
    private int repetition;
    private int duration;

    public Details(String exercise, String date, String time, int level, int repetition, int duration) {
        this.exercise = exercise;
        this.date = date;
        this.time = time;
        this.level = level;
        this.repetition = repetition;
        this.duration = duration;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}