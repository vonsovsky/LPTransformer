package transformation;

import data.DistanceObject;
import enums.Direction;
import enums.Type;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class LPTransformer {

    private List<DistanceObject> distanceObjectVector;
    private Set<String> usedSymbols = new HashSet<>();
    private LPFileGenerator lpFileGenerator = new LPFileGenerator();

    public LPTransformer(List<DistanceObject> distanceObjectVector) {
        this.distanceObjectVector = distanceObjectVector;
    }

    public void transform() {
        checkNotNull(distanceObjectVector != null);
        checkArgument(!distanceObjectVector.isEmpty(), "Vector requires at least one element");

        addObjectiveFunctionAndRememberSymbols();
        addConstraints();
        addBounds();
        addTypes();
        lpFileGenerator.addEnd();
    }

    private void addObjectiveFunctionAndRememberSymbols() {
        String statement = " obj: error1";
        for (int i = 1; i < distanceObjectVector.size(); i++) {
            statement += " + error" + (i + 1);
            DistanceObject distObj = distanceObjectVector.get(i);
            usedSymbols.add(distObj.getFirstSymbol());
            usedSymbols.add(distObj.getSecondSymbol());
        }
        lpFileGenerator.addObjectiveFunction(Direction.MINIMIZE, statement);
    }

    private void addConstraints() {
        for (int i = 0; i < distanceObjectVector.size(); i++) {
            DistanceObject distObj = distanceObjectVector.get(i);
            if (distObj.getDistance() <= 1) {
                addSmallConstraint(distObj, i + 1);
            }
        }
    }

    private void addBounds() {
        for (String symbol : usedSymbols) {
            lpFileGenerator.addBound(0, symbol);
        }
    }

    private void addSmallConstraint(DistanceObject distObj, int errorCounter) {
        String statement = String.format(" %s - %s - 0.2 error%d <= 1",
                distObj.getFirstSymbol(), distObj.getSecondSymbol(), errorCounter);
        lpFileGenerator.addConstraint(statement);

        statement = String.format(" %s - %s - 0.2 error%d <= 1",
                distObj.getSecondSymbol(), distObj.getFirstSymbol(), errorCounter);
        lpFileGenerator.addConstraint(statement);
    }

    private void addTypes() {
        String statement = "";
        for (int i = 0; i < distanceObjectVector.size(); i++) {
            statement += " error" + (i + 1);
        }
        lpFileGenerator.addType(Type.BOOLEAN, statement);
    }

    @Override
    public String toString() {
        return lpFileGenerator.toString();
    }
}
