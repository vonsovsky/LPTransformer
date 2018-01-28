package transformation;

import enums.Direction;
import enums.Type;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class LPFileGenerator {

    private List<String> lines = new ArrayList<>();

    private boolean objectiveAdded = false;
    private boolean constraintAdded = false;
    private boolean boundAdded = false;

    public List<String> getLines() {
        return lines;
    }

    public void addObjectiveFunction(Direction direction, String statement) {
        if (direction == Direction.MAXIMIZE) {
            lines.add("Maximize");
        }
        if (direction == Direction.MINIMIZE) {
            lines.add("Minimize");
        }
        lines.add(statement);
        objectiveAdded = true;
    }

    public void addConstraint(String statement) {
        checkStepAdded(objectiveAdded);
        if (!constraintAdded) {
            lines.add("Subject To");
        }
        lines.add(statement);
        constraintAdded = true;
    }

    private void checkStepAdded(boolean value) {
        if (!value) {
            throw new IllegalArgumentException("Order of arguments is violated");
        }
    }

    public void addBound(double lowerBound, String symbol, double upperBound) {
        String statement = String.format(" %f <= %s <= %f", lowerBound, symbol, upperBound);
        addBound(statement);
    }

    public void addBound(double lowerBound, String symbol) {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("0.00", decimalFormatSymbols);

        String statement = String.format(" %s <= %s", decimalFormat.format(lowerBound), symbol);
        addBound(statement);
    }

    private void addBound(String statement) {
        checkStepAdded(constraintAdded);
        if (!boundAdded) {
            lines.add("Bounds");
        }
        lines.add(statement);
        boundAdded = true;
    }

    public void addType(Type type, String statement) {
        checkStepAdded(constraintAdded);
        if (type == Type.INTEGER) {
            lines.add("General");
        }
        if (type == Type.BOOLEAN) {
            lines.add("Binary");
        }
        lines.add(statement);
    }

    public void addEnd() {
        checkStepAdded(constraintAdded);
        lines.add("end");
    }

    @Override
    public String toString() {
        return String.join("\r\n", lines);
    }
}
