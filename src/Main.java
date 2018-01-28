import data.ItemsData;
import data.Position;
import output.FileOperator;
import transformation.LPTransformer;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String [] args) {
        saveSimpleTriangle();
    }

    private static void saveSimpleTriangle() {
        //ItemsData itemsData = new ItemsData(prepareItems());
        LPTransformer lpTransformer = new LPTransformer(prepareItems());

        lpTransformer.transform();

        FileOperator.writeTo("triangle.lp", lpTransformer.toString());
    }

    private static List<Position> prepareItems() {
        List<Position> items = new ArrayList<>();

        /*items.add(new Position(0, 0));
        items.add(new Position(0.5, 0));
        items.add(new Position(0.5, 0.5));/*/

        for (int i = 0; i < 500; i++) {
            Position pos = new Position(Math.random(), Math.random(), Math.random(), Math.random(), Math.random(), Math.random(), Math.random(), Math.random(), Math.random(), Math.random());
            items.add(pos);
        }

        return items;
    }

}
