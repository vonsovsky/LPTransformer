package data;

public class Variable {
    private String name;
    private double[] position;

    public Variable(String name, double... position) {
        this.name = name;
        this.position = position;
    }

    public String getVariableName(int positionIndex) {
        return name + "_" + (positionIndex + 1);
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public boolean isDistant(Variable other) {
        for (int i = 0; i < position.length; i++) {
            double a = position[i];
            double b = other.getPosition()[i];
            if (Math.abs(a - b) > 1) {
                return true;
            }
        }
        return false;
    }
}
