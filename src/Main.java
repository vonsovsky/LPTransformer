import data.Position;
import output.TransformerWriter;
import output.Writer;
import transformation.LPFileGenerator;
import transformation.LPTransformer;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        saveSimpleTriangle();
    }

    private static void saveSimpleTriangle() {
        Writer writer = new TransformerWriter("triangle.lp");
        writer.open();

        LPFileGenerator lpFileGenerator = new LPFileGenerator(writer);
        LPTransformer lpTransformer = new LPTransformer(prepareItems(), lpFileGenerator);
        lpTransformer.transform();

        writer.close();
    }

    private static List<Position> prepareItems() {
        List<Position> items = new ArrayList<>();

        /*
        items.add(new Position(0, 0));
        items.add(new Position(0.5, 0));
        items.add(new Position(0.5, 0.5));
        */


        items.add(new Position(0, 0));
        items.add(new Position(0.5, 0));
        items.add(new Position(0, 2));
        items.add(new Position(0.5, 2));


        /*for (int i = 0; i < 500; i++) {
            Position pos = new Position(Math.random() * 10, Math.random() * 10, Math.random() * 10,
                    Math.random() * 10, Math.random() * 10, Math.random() * 10, Math.random() * 10,
                    Math.random() * 10, Math.random() * 10, Math.random() * 10);
            items.add(pos);
        }*/

        return items;
    }

}
