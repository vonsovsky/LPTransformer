package data;

import java.util.Objects;

public class DistanceObject {
    private double distance;
    private String firstSymbol;
    private String secondSymbol;

    public DistanceObject(double distance, String firstSymbol, String secondSymbol) {
        this.distance = distance;
        this.firstSymbol = firstSymbol;
        this.secondSymbol = secondSymbol;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getFirstSymbol() {
        return firstSymbol;
    }

    public void setFirstSymbol(String firstSymbol) {
        this.firstSymbol = firstSymbol;
    }

    public String getSecondSymbol() {
        return secondSymbol;
    }

    public void setSecondSymbol(String secondSymbol) {
        this.secondSymbol = secondSymbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DistanceObject)) return false;

        DistanceObject that = (DistanceObject) o;
        double c = Math.abs(that.distance - distance);
        return c <= 0.0001 &&
                Objects.equals(firstSymbol, that.firstSymbol) &&
                Objects.equals(secondSymbol, that.secondSymbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, firstSymbol, secondSymbol);
    }

}
