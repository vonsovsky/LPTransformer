package data;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class ItemsData {

    List<Position> items;

    public ItemsData(List<Position> items) {
        this.items = items;
    }

    public double getDistance(int index1, int index2) {
        checkArgument(index1 < items.size());
        checkArgument(index2 < items.size());

        Position p1 = items.get(index1);
        Position p2 = items.get(index2);

        return getEuclideanDistance(p1.getPosition(), p2.getPosition());
    }

    private double getEuclideanDistance(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - b[i]) * (a[i] - b[i]);
        }

        return Math.sqrt(sum);
    }

    public List<DistanceObject> vectorize() {
        List<DistanceObject> distanceObj = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            for (int j = i + 1; j < items.size(); j++) {
                double distance = getDistance(i, j);
                String firstSymbol = "x" + (i + 1);
                String secondSymbol = "x" + (j + 1);
                distanceObj.add(new DistanceObject(distance, firstSymbol, secondSymbol));
            }
        }

        return distanceObj;
    }

}
