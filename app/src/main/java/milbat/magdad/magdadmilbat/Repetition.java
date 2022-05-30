package milbat.magdad.magdadmilbat;

public class Repetition {
    private int number; // index of Repetition
    private Double repDuration; // The duration the ball was in the air
    private int maxHeight; // max height of ball in repetition
    private boolean target;

    public Repetition(int number, Double repDuration, int maxHeight,boolean target) {
        this.number = number;
        this.repDuration = repDuration;
        this.maxHeight = maxHeight;
        this.target = target;
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

    public boolean isTarget() {
        return target;
    }

    public void setTarget(boolean target) {
        this.target = target;
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
