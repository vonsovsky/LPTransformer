package transformation;

import data.Position;
import enums.Direction;
import enums.Type;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class LPTransformer {

    private static int M = 1000;
    private static double E = 0.2;
    private static String PARSE_E = "0.0";
    static int MAXIMUM_TYPES_IN_BUFFER = 512;

    private List<Position> items;

    private Set<String> symbolsToBound = new LinkedHashSet<>();
    private Set<String> symbolsToBinary = new LinkedHashSet<>();
    private LPFileGenerator lpFileGenerator;

    public LPTransformer(List<Position> items, LPFileGenerator lpFileGenerator) {
        this.items = items;
        this.lpFileGenerator = lpFileGenerator;
    }

    public void transform() {
        checkNotNull(items != null);
        checkArgument(!items.isEmpty(), "Vector requires at least one element");

        transformToFile();
    }

    private void transformToFile() {
        addObjectiveFunction();
        addConstraintsAndRememberBoundSymbols();
        addBounds();
        addTypes();
        lpFileGenerator.addEnd();
    }

    private void addObjectiveFunction() {
        String statement = " obj: ";
        int diagonalElementsCount = items.size() * (items.size() - 1) / 2;
        for (int i = 0; i < diagonalElementsCount; i++) {
            statement += "error" + (i + 1) + " + ";
            symbolsToBinary.add("error" + (i + 1));
        }
        statement = statement.substring(0, statement.length() - 3);
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, statement);
    }

    private void addConstraintsAndRememberBoundSymbols() {
        int errorCounter = 1;
        for (int i = 0; i < items.size(); i++) {
            addSymbolsForBoundVars(items.get(i), i + 1 + "");
            for (int j = i + 1; j < items.size(); j++) {
                checkDistancesBetweenTwoItems(i, j, errorCounter);
                errorCounter++;
            }
        }
    }

    private void addSymbolsForBoundVars(Position item, String index) {
        for (int i = 1; i <= item.getPosition().length; i++) {
            symbolsToBound.add("x" + index + "_" + i);
        }
    }

    private void checkDistancesBetweenTwoItems(int index1, int index2, int errorCounter) {
        boolean isDistant = checkIsDistant(index1, index2);

        int dimensions = items.get(index1).getPosition().length;
        String distantExtraStatement = "";
        for (int i = 0; i < dimensions; i++) {
            if (isDistant) {
                addDistantConstraint(index1, index2, i, errorCounter);
                distantExtraStatement += addItemBinaryVariables(i, errorCounter);
            } else {
                addCloseConstraint(index1, index2, i, errorCounter);
            }
        }

        addItemBinaryVariablesConstraint(distantExtraStatement, dimensions);
    }

    private String addItemBinaryVariables(int index, int subIndex) {
        symbolsToBinary.add("b" + index + "_" + ((subIndex + 1) * 2 - 1));
        symbolsToBinary.add("b" + index + "_" + ((subIndex + 1) * 2));
        return String.format(" b%d_%d + b%d_%d +",
                index, (subIndex + 1) * 2 - 1, index, (subIndex + 1) * 2);
    }

    private void addItemBinaryVariablesConstraint(String statement, int dimensions) {
        if (!statement.isEmpty()) {
            statement = statement.substring(0, statement.length() - 2);
            statement += " = " + (dimensions * 2 - 1);
            lpFileGenerator.addConstraint(statement);
        }
    }

    private boolean checkIsDistant(int index1, int index2) {
        for (int i = 0; i < items.get(index1).getPosition().length; i++) {
            double a = items.get(index1).getPosition()[i];
            double b = items.get(index2).getPosition()[i];

            if (Math.abs(a - b) > 1) {
                return true;
            }
        }

        return false;
    }

    private void addCloseConstraint(int index1, int index2, int subIndex, int errorCounter) {
        String statement = String.format(" x%d_%d - x%d_%d - %s error%d <= 1",
                index1 + 1, subIndex + 1, index2 + 1, subIndex + 1, parseDouble(E), errorCounter);
        lpFileGenerator.addConstraint(statement);

        statement = String.format(" x%d_%d - x%d_%d - %s error%d <= 1",
                index2 + 1, subIndex + 1, index1 + 1, subIndex + 1, parseDouble(E), errorCounter);
        lpFileGenerator.addConstraint(statement);
    }

    private String parseDouble(double d) {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat(PARSE_E, decimalFormatSymbols);

        return decimalFormat.format(d);
    }

    private void addDistantConstraint(int index1, int index2, int subIndex, int errorCounter) {
        String statement = String.format(" x%d_%d - x%d_%d + %d b%d_%d + %s error%d > 1",
                index1 + 1, subIndex + 1, index2 + 1, subIndex + 1, M,
                errorCounter, (subIndex + 1) * 2 - 1, parseDouble(E), errorCounter);
        lpFileGenerator.addConstraint(statement);

        statement = String.format(" x%d_%d - x%d_%d + %d b%d_%d + %s error%d > 1",
                index2 + 1, subIndex + 1, index1 + 1, subIndex + 1, M,
                errorCounter, (subIndex + 1) * 2, parseDouble(E), errorCounter);
        lpFileGenerator.addConstraint(statement);
    }

    private void addBounds() {
        for (String symbol : symbolsToBound) {
            lpFileGenerator.addBound(0, symbol);
        }
    }

    private void addTypes() {
        String statement = "";
        int counter = 0;
        for (String symbol : symbolsToBinary) {
            if (counter >= MAXIMUM_TYPES_IN_BUFFER) {
                lpFileGenerator.addTypePartial(Type.BOOLEAN, statement);
                statement = "";
                counter = 0;
            }
            counter++;
            statement += " " + symbol;
        }
        lpFileGenerator.addType(Type.BOOLEAN, statement);
    }

}
