package transformation;

import enums.Direction;
import enums.Type;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class LPFileGenerator {

    private boolean objectiveAdded = false;
    private boolean constraintAdded = false;
    private boolean boundAdded = false;

    BufferedWriter bw;

    public LPFileGenerator(BufferedWriter bw) {
        this.bw = bw;
    }

    public void addObjectiveFunction(Direction direction, String statement) throws IOException {
        if (direction == Direction.MAXIMIZE) {
            addString("Maximize");
        }
        if (direction == Direction.MINIMIZE) {
            addString("Minimize");
        }
        addString(statement);
        objectiveAdded = true;
    }

    private void addString(String line) throws IOException {
        bw.write(line);
        bw.newLine();
    }

    public void addConstraint(String statement) throws IOException {
        checkStepAdded(objectiveAdded);
        if (!constraintAdded) {
            addString("Subject To");
        }
        addString(statement);
        constraintAdded = true;
    }

    private void checkStepAdded(boolean value) {
        if (!value) {
            throw new IllegalArgumentException("Order of arguments is violated");
        }
    }

    public void addBound(double lowerBound, String symbol, double upperBound) throws IOException {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("0.00", decimalFormatSymbols);

        String statement = String.format(" %s <= %s <= %s",
                decimalFormat.format(lowerBound), symbol, decimalFormat.format(upperBound));
        addBound(statement);
    }

    public void addBound(double lowerBound, String symbol) throws IOException {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("0.00", decimalFormatSymbols);

        String statement = String.format(" %s <= %s", decimalFormat.format(lowerBound), symbol);
        addBound(statement);
    }

    private void addBound(String statement) throws IOException {
        checkStepAdded(constraintAdded);
        if (!boundAdded) {
            addString("Bounds");
        }
        addString(statement);
        boundAdded = true;
    }

    public void addType(Type type, String statement) throws IOException {
        checkStepAdded(constraintAdded);
        if (type == Type.INTEGER) {
            addString("General");
        }
        if (type == Type.BOOLEAN) {
            addString("Binary");
        }
        addString(statement);
    }

    public void addEnd() throws IOException {
        checkStepAdded(constraintAdded);
        addString("end");
    }

}
