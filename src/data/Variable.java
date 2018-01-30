package data;

public class Variable {
    private double[] position;

    public Variable(double... position) {
        this.position = position;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }
}
