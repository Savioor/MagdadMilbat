package com.example.magdadmilbat;

public class Training {
    //private Date date;
    private String date;
    private String time;
    private String exerciseDescription;
    private int trainingQuality;

    public Training(String date, String time, String exerciseDescription, int trainingQuality) {
        this.date = date;
        this.time = time;
        this.exerciseDescription = exerciseDescription;
        this.trainingQuality = trainingQuality;
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

    public String getExerciseDescription() {
        return exerciseDescription;
    }

    public void setExerciseDescription(String exerciseDescription) {
        this.exerciseDescription = exerciseDescription;
    }

    public int getTrainingQuality() {
        return trainingQuality;
    }

    public void setWorkmanshipQuality(int trainingQuality) {
        this.trainingQuality = trainingQuality;
    }

    @Override
    public String toString() {
        return "Training{" +
                "Date='" + date + '\'' +
                ", Time='" + time + '\'' +
                ", Exercise Description: '" + exerciseDescription + '\'' +
                ", Training Quality: " + trainingQuality +
                '}';
    }
}
