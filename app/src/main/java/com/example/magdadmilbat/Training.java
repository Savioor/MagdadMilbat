package com.example.magdadmilbat;

public class Training {
    //private Date date;
    private String date;
    private String time; // Training start time
    private String exerciseDescription;
    private int trainingQuality;
    private double duration;//duration that the Training take

    public Training(String date, String time, String exerciseDescription, int trainingQuality, double duration) {
        this.date = date;
        this.time = time;
        this.exerciseDescription = exerciseDescription;
        this.trainingQuality = trainingQuality;
        this.duration = duration;
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

    public double getDuration(){return this.duration;}

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
                ", Training Quality: " + trainingQuality + '\'' +
                ", duration:" + duration +
                '}';
    }
}
