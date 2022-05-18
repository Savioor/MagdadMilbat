package com.example.magdadmilbat;

public class Repetition {
    private int number; // index of Repetition
    private Double repDuration; // The duration the ball was in the air
    private int maxHeight; // max height of ball in repetition

    public Repetition(int number, Double repDuration, int maxHeight) {
        this.number = number;
        this.repDuration = repDuration;
        this.maxHeight = maxHeight;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Double getRepduration() {
        return repDuration;
    }

    public void setRepduration(Double repduration) {
        this.repDuration = repduration;
    }

    public int getMaxHeight() {
        return this.maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public String toString() {
        return "Repetition{" +
                "number=" + number +
                ", repduration='" + repDuration + '\'' +
                ", MaxHeight=" + maxHeight +
                '}';
    }
}
