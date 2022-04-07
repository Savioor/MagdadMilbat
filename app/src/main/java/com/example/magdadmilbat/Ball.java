package com.example.magdadmilbat;

public class Ball {
    private int color; //ball color: 1=green , 2=blue ,3=orange
    private double durationInAir;   //The duration that ball was in the air
    private int repetitionSuccess; //The amount of successful returns of the ball
    private double maxHeight;

    public Ball(int color, double durationInAir, int repetitionSuccess, double maxHeight) {
        this.color = color;
        this.durationInAir = durationInAir;
        this.repetitionSuccess = repetitionSuccess;
        this.maxHeight = maxHeight;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double getDurationInAir() {
        return durationInAir;
    }

    public void setDurationInAir(double durationInAir) {
        this.durationInAir = durationInAir;
    }

    public int getRepetitionSuccess() {
        return repetitionSuccess;
    }

    public void setRepetitionSuccess(int repetitionSuccess) {
        this.repetitionSuccess = repetitionSuccess;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public String toString() {
        return "Ball{" +
                "color=" + color +
                ", durationInAir=" + durationInAir +
                ", repetitionSuccess=" + repetitionSuccess +
                ", maxHeight=" + maxHeight +
                '}';
    }
}