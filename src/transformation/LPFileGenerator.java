package transformation;

import enums.Direction;
import enums.Type;
import output.Writer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class LPFileGenerator {

    private boolean objectiveAdded = false;
    private boolean constraintAdded = false;
    private boolean boundAdded = false;
    private boolean typeAdded = false;

    Writer writer;

    public LPFileGenerator(Writer writer) {
        this.writer = writer;
    }

    public void addObjectiveFunction(Direction direction, String statement) {
        if (direction == Direction.MAXIMIZE) {
            writer.addString("Maximize");
        }
        if (direction == Direction.MINIMIZE) {
            writer.addString("Minimize");
        }
        writer.addString(statement);
        objectiveAdded = true;
    }

    public void addConstraint(String statement) {
        checkStepAdded(objectiveAdded);
        if (!constraintAdded) {
            writer.addString("Subject To");
        }
        writer.addString(statement);
        constraintAdded = true;
    }

    private void checkStepAdded(boolean value) {
        if (!value) {
            throw new IllegalArgumentException("Order of arguments is violated");
        }
    }

    public void addBound(double lowerBound, String symbol, double upperBound) {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("0.00", decimalFormatSymbols);

        String statement = String.format(" %s <= %s <= %s",
                decimalFormat.format(lowerBound), symbol, decimalFormat.format(upperBound));
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
            writer.addString("Bounds");
        }
        writer.addString(statement);
        boundAdded = true;
    }

    public void addTypePartial(Type type, String statement) {
        checkStepAdded(constraintAdded);
        if (!typeAdded) {
            if (type == Type.INTEGER) {
                writer.addString("General");
            }
            if (type == Type.BOOLEAN) {
                writer.addString("Binary");
            }
        }
        writer.addStringNoNewline(statement);
        typeAdded = true;
    }

    /**
     * Adds type and breaks line
     */
    public void addType(Type type, String statement) {
        addTypePartial(type, statement);
        writer.addString("");
    }

    public void addEnd() {
        checkStepAdded(constraintAdded);
        writer.addString("end");
    }

}
