package transformation;

import data.Variable;
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

    private List<Variable> items;

    private Set<String> symbolsToBinary = new LinkedHashSet<>();
    private LPFileGenerator lpFileGenerator;

    public LPTransformer(List<Variable> items, LPFileGenerator lpFileGenerator) {
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
        addConstraints();
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

    private void addConstraints() {
        int errorCounter = 1;
        for (int i = 0; i < items.size(); i++) {
            for (int j = i + 1; j < items.size(); j++) {
                checkDistancesBetweenTwoItems(items.get(i), items.get(j), errorCounter);
                errorCounter++;
            }
        }
    }

    private void checkDistancesBetweenTwoItems(Variable var1, Variable var2, int errorCounter) {
        String distantExtraStatement = "";
        int dimensions = var1.getPosition().length;

        for (int i = 0; i < dimensions; i++) {
            if (var1.isDistant(var2)) {
                addDistantConstraint(var1.getVariableName(i), var2.getVariableName(i), i, errorCounter);
                distantExtraStatement += addItemBinaryVariables(errorCounter, i);
            } else {
                addCloseConstraint(var1.getVariableName(i), var2.getVariableName(i), errorCounter);
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

    private void addCloseConstraint(String varName1, String varName2, int errorCounter) {
        String statement = String.format(" %s - %s - %s error%d <= 1",
                varName1, varName2, parseDouble(E), errorCounter);
        lpFileGenerator.addConstraint(statement);

        statement = String.format(" %s - %s - %s error%d <= 1",
                varName2, varName1, parseDouble(E), errorCounter);
        lpFileGenerator.addConstraint(statement);
    }

    private String parseDouble(double d) {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat(PARSE_E, decimalFormatSymbols);

        return decimalFormat.format(d);
    }

    private void addDistantConstraint(String varName1, String varName2, int subIndex, int errorCounter) {
        String statement = String.format(" %s - %s + %d b%d_%d + %s error%d > 1",
                varName1, varName2, M, errorCounter, (subIndex + 1) * 2 - 1, parseDouble(E), errorCounter);
        lpFileGenerator.addConstraint(statement);

        statement = String.format(" %s - %s + %d b%d_%d + %s error%d > 1",
                varName2, varName1, M, errorCounter, (subIndex + 1) * 2, parseDouble(E), errorCounter);
        lpFileGenerator.addConstraint(statement);
    }

    private void addBounds() {
        for (Variable var : items) {
            for (int i = 0; i < var.getPosition().length; i++) {
                lpFileGenerator.addBound(0, var.getVariableName(i));
            }
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
