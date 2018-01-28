package transformation;

import data.Position;
import enums.Direction;
import enums.Type;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class LPTransformer {

    private static int M = 1000;
    private static double E = 0.2;

    private List<Position> items;
    private BufferedWriter bw;

    private Set<String> usedSymbols = new LinkedHashSet<>();
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
            addObjectiveFunctionAndRememberSymbols();
            addConstraints();
            addBounds();
            addTypes();
            lpFileGenerator.addEnd();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Mixing two responsibilities to save processor time (one loop less)
     */
    private void addObjectiveFunctionAndRememberSymbols() throws IOException {
        String statement = " obj: ";
        for (int i = 0; i < items.size(); i++) {
            statement += "error" + (i + 1) + " + ";
            addSymbolsForPosition(items.get(i), i + 1 + "");
        }
        statement = statement.substring(0, statement.length() - 3);
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, statement);
    }

    private void addSymbolsForPosition(Position item, String index) {
        for (int i = 1; i <= item.getPosition().length; i++) {
            usedSymbols.add("x" + index + "_" + i);
        }
    }

    private void addConstraints() throws IOException {
        int errorCounter = 1;
        for (int i = 0; i < items.size(); i++) {
            for (int j = i + 1; j < items.size(); j++) {
                checkDistancesBetweenTwoItems(i, j, errorCounter);
                errorCounter++;
            }
        }
    }

    private void checkDistancesBetweenTwoItems(int index1, int index2, int errorCounter) throws IOException {
        boolean overOneDistanceAdded = false;

        int dimensions = items.get(index1).getPosition().length;
        for (int i = 0; i < dimensions; i++) {
            double a = items.get(index1).getPosition()[i];
            double b = items.get(index2).getPosition()[i];

            if (Math.abs(a - b) <= 1 || overOneDistanceAdded) {
                addCloseConstraint(index1, index2, i, errorCounter);
            } else {
                addDistantConstraint(index1, index2, i, dimensions, errorCounter);
                overOneDistanceAdded = true;
            }
        }
    }

    private void addCloseConstraint(int index1, int index2, int subIndex, int errorCounter) throws IOException {
        String statement = String.format(" x%d_%d - x%d_%d - 0.2 error%d <= 1",
                index1 + 1, subIndex + 1, index2 + 1, subIndex + 1, errorCounter);
        lpFileGenerator.addConstraint(statement);

        statement = String.format(" x%d_%d - x%d_%d - 0.2 error%d <= 1",
                index2 + 1, subIndex + 1, index1 + 1, subIndex + 1, errorCounter);
        lpFileGenerator.addConstraint(statement);
    }

    private void addDistantConstraint(int index1, int index2, int subIndex, int dimensions, int errorCounter)
            throws IOException {
        String statement = String.format(" x%d_%d - x%d_%d + %d %d + %f error%d > 1",
                index1 + 1, subIndex + 1, index2 + 1, subIndex + 1, M, dimensions - 1, E, errorCounter);
        lpFileGenerator.addConstraint(statement);

        statement = String.format(" x%d_%d - x%d_%d + %d %d + %f error%d > 1",
                index2 + 1, subIndex + 1, index1 + 1, subIndex + 1, M, dimensions - 1, E, errorCounter);
        lpFileGenerator.addConstraint(statement);
    }

    private void addBounds() throws IOException {
        for (String symbol : usedSymbols) {
            lpFileGenerator.addBound(0, symbol);
        }
    }

    private void addTypes() throws IOException {
        String statement = "";
        for (int i = 1; i <= items.size(); i++) {
            statement += " error" + i;
        }
        lpFileGenerator.addType(Type.BOOLEAN, statement);
    }

    @Override
    public String toString() {
        return lpFileGenerator.toString();
    }
}
