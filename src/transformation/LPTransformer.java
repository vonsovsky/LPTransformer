package transformation;

import data.Position;
import enums.Direction;
import enums.Type;

import java.io.BufferedWriter;
import java.io.IOException;
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
    private BufferedWriter bw;

    private Set<String> symbolsToBound = new LinkedHashSet<>();
    private Set<String> symbolsToBinary = new LinkedHashSet<>();
    private LPFileGenerator lpFileGenerator;

    public LPTransformer(List<Position> items, BufferedWriter bw) {
        this.items = items;
        this.bw = bw;
    }

    public void transform() {
        checkNotNull(items != null);
        checkArgument(!items.isEmpty(), "Vector requires at least one element");

        lpFileGenerator = new LPFileGenerator(bw);
        transformToFile();
    }

    private void transformToFile() {
        try {
            addObjectiveFunction();
            addConstraintsAndRememberBoundSymbols();
            addBounds();
            addTypes();
            lpFileGenerator.addEnd();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void addObjectiveFunction() throws IOException {
        String statement = " obj: ";
        int diagonalElementsCount = items.size() * (items.size() - 1) / 2;
        for (int i = 0; i < diagonalElementsCount; i++) {
            statement += "error" + (i + 1) + " + ";
            symbolsToBinary.add("error" + (i + 1));
        }
        statement = statement.substring(0, statement.length() - 3);
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, statement);
    }

    private void addConstraintsAndRememberBoundSymbols() throws IOException {
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

    private void checkDistancesBetweenTwoItems(int index1, int index2, int errorCounter) throws IOException {
        boolean isDistant = checkIsDistant(index1, index2);

        int dimensions = items.get(index1).getPosition().length;
        String distantExtraStatement = "";
        for (int i = 0; i < dimensions; i++) {
            if (isDistant) {
                addDistantConstraint(index1, index2, i, errorCounter);
                distantExtraStatement += String.format(" b%d_%d + b%d_%d +",
                        errorCounter, (i + 1) * 2 - 1, errorCounter, (i + 1) * 2);
                symbolsToBinary.add("b" + errorCounter + "_" + (i + 1));
            } else {
                addCloseConstraint(index1, index2, i, errorCounter);
            }
        }

        if (!distantExtraStatement.isEmpty()) {
            distantExtraStatement = distantExtraStatement.substring(0, distantExtraStatement.length() - 2);
            distantExtraStatement += " = " + (dimensions * 2 - 1);
            lpFileGenerator.addConstraint(distantExtraStatement);
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

    private void addCloseConstraint(int index1, int index2, int subIndex, int errorCounter) throws IOException {
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

    private void addDistantConstraint(int index1, int index2, int subIndex, int errorCounter)
            throws IOException {
        String statement = String.format(" x%d_%d - x%d_%d + %d b%d_%d + %s error%d > 1",
                index1 + 1, subIndex + 1, index2 + 1, subIndex + 1, M,
                errorCounter, (subIndex + 1) * 2 - 1, parseDouble(E), errorCounter);
        lpFileGenerator.addConstraint(statement);

        statement = String.format(" x%d_%d - x%d_%d + %d b%d_%d + %s error%d > 1",
                index2 + 1, subIndex + 1, index1 + 1, subIndex + 1, M,
                errorCounter, (subIndex + 1) * 2, parseDouble(E), errorCounter);
        lpFileGenerator.addConstraint(statement);
    }

    private void addBounds() throws IOException {
        for (String symbol : symbolsToBound) {
            lpFileGenerator.addBound(0, symbol);
        }
    }

    private void addTypes() throws IOException {
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
