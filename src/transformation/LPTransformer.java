package transformation;

import data.Variable;
import enums.Direction;
import enums.Type;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedHashSet;
import java.util.Set;

import data.Distances;

public class LPTransformer {

	private final int TARGET_SPACE_DIMENSION = 4;// TODO move elsewhere, probably class TargetSpace
	private static int M = 1000;
	private static String PARSE_E = "0.0";
	private static double UPPER_VARIABLE_BOUND = 3;
	private static double E = 1;
	static int MAXIMUM_TYPES_IN_BUFFER = 512;

	//private List<Variable> items;
	private Distances distances;

	private Set<String> symbolsToBinary = new LinkedHashSet<>();
	private LPFileGenerator lpFileGenerator;

	public LPTransformer(Distances distances /*List<Variable> items*/, LPFileGenerator lpFileGenerator) {
		//this.items = items;
		this.distances = distances;
		this.lpFileGenerator = lpFileGenerator;
	}

	public void transform() {
		//checkNotNull(items != null);
		//checkArgument(!items.isEmpty(), "Vector requires at least one element");

		transformToFile();
	}

	private void transformToFile() {
		addObjectiveFunction();
		addConstraints();
		addBounds(UPPER_VARIABLE_BOUND);
		addTypes();
		lpFileGenerator.addEnd();
	}

	private void addObjectiveFunction() {
		String statement = " obj: ";
		statement = statement + "error0";
		lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, statement);
	}

	private void addObjectiveFunctionWithMultipleErrors() {
		String statement = " obj: ";
		int diagonalElementsCount = distances.size() * (distances.size() - 1) / 2;
		for (int i = 0; i < diagonalElementsCount; i++) {
			statement += "error" + (i + 1) + " + ";
		}
		statement = statement.substring(0, statement.length() - 3);
		lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, statement);
	}

	// TODO break into more methods
	private void addConstraints() {
		int pairCounter = 1;
		for (int x = 0; x < distances.size(); x++) {
			for (int y = x + 1; y < distances.size(); y++) {
				double distance = distances.getDistance(x, y);
				if (distance <= 1) {
					addCloseItems(x, y, pairCounter, TARGET_SPACE_DIMENSION);
				} else {
					addDistantItems(x, y, pairCounter, TARGET_SPACE_DIMENSION);
				}
				pairCounter++;
			}
		}
	}

	private String getVariableName(int objectIndex, int dimension) {
		return "x" + (objectIndex + 1) + "_" + (dimension + 1);
	}

	/*private void checkDistancesBetweenTwoItems(Variable var1, Variable var2, int pairCounter) {
        if (var1.isDistant(var2)) {
            addDistantItems(var1, var2, pairCounter);
        } else {
            addCloseItems(var1, var2, pairCounter);
        }
    }*/
	private void addDistantItems(int item1, int item2, int pairCounter, int dimensions) {
		String distantExtraStatement = "";

		for (int d = 0; d < dimensions; d++) {
			addDistantConstraint(getVariableName(item1, d), getVariableName(item2, d), d, pairCounter);
			distantExtraStatement += addItemBinaryVariables(pairCounter, d);
		}

		addItemBinaryVariablesConstraint(distantExtraStatement, dimensions);
	}

	private void addCloseItems(int item1, int item2, int pairCounter, int dimensions) {
		for (int d = 0; d < dimensions; d++) {
			addCloseConstraint(getVariableName(item1, d), getVariableName(item2, d), pairCounter);
		}
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

	private void addCloseConstraint(String varName1, String varName2, int pairCounter) {
		String statement = String.format(" %s - %s - %s error%d <= 1",
			varName1, varName2, parseDouble(E), 0 * pairCounter); // !!! 0 *
		lpFileGenerator.addConstraint(statement);

		statement = String.format(" %s - %s - %s error%d <= 1",
			varName2, varName1, parseDouble(E), 0 * pairCounter); // !!! 0 *
		lpFileGenerator.addConstraint(statement);
	}

	private String parseDouble(double d) {
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		DecimalFormat decimalFormat = new DecimalFormat(PARSE_E, decimalFormatSymbols);

		return decimalFormat.format(d);
	}

	private void addDistantConstraint(String varName1, String varName2, int subIndex, int pairCounter) {
		String statement = String.format(" %s - %s + %d b%d_%d + %s error%d > 1",
			varName1, varName2, M, pairCounter, (subIndex + 1) * 2 - 1, parseDouble(E), 0 * pairCounter); // !!! 0 *
		lpFileGenerator.addConstraint(statement);

		statement = String.format(" %s - %s + %d b%d_%d + %s error%d > 1",
			varName2, varName1, M, pairCounter, (subIndex + 1) * 2, parseDouble(E), 0 * pairCounter);// !!! 0 *
		lpFileGenerator.addConstraint(statement);
	}

	private void addBounds(double upper) {
		for (int i = 0; i < distances.size(); i++) {
			for (int d = 0; d < TARGET_SPACE_DIMENSION; d++) {
				lpFileGenerator.addBound(0, getVariableName(i, d), upper);
			}
		}

		int diagonalElementsCount = distances.size() * (distances.size() - 1) / 2;
		//for (int i = 0; i < diagonalElementsCount; i++) {
		//    lpFileGenerator.addBound(0, "error" + (i + 1), 1);
		//}
		lpFileGenerator.addBound(0, "error0");
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
