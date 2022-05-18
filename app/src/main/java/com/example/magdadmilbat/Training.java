package com.example.magdadmilbat;

public class Training {
    private String date; // date in String format [ dd/mm/yy ]
    private String time; // Training start time
    private String exerciseDescription;
    private int trainingQuality; // training score
    private double duration;//duration that the Training take

    //duration of each repetition in the exercise
    private String repDuration;

    //Max Height of each repetition in the exercise
    private String repMaxHeight;

    private int balldata;  // Which ball the data were collected


    public Training(String date, String time, String exerciseDescription, int trainingQuality, double duration, String repDuration, String repMaxHeight, int balldata) {
        this.date = date;
        this.time = time;
        this.exerciseDescription = exerciseDescription;
        this.trainingQuality = trainingQuality;
        this.duration = duration;
        this.repDuration = repDuration;
        this.repMaxHeight = repMaxHeight;
        this.balldata = balldata;
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

    public String getRepDuration() {
        return repDuration;
    }

    public void setRepDuration(String repDuration) {
        this.repDuration = repDuration;
    }

    public String getRepMaxHeight() {
        return repMaxHeight;
    }

    public void setRepMaxHeight(String repMaxHeight) {
        this.repMaxHeight = repMaxHeight;
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

    public int getBalldata() {
        return balldata;
    }

    public void setBalldata(int balldata) {
        this.balldata = balldata;
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
