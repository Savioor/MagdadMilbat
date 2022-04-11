package com.example.magdadmilbat;

public class Repetition {
    private int number;
    private String repDuration;
    private int maxHeight;

    public Repetition(int number, String repDuration, int maxHeight) {
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

    public String getRepduration() {
        return repDuration;
    }

    public void setRepduration(String repduration) {
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
