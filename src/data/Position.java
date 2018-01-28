package data;

public class Position {
    private double[] position;

    public Position(double... position) {
        this.position = position;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }
}
