import data.Position;
import output.TransformerWriter;
import output.Writer;
import transformation.LPFileGenerator;
import transformation.LPTransformer;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        transformData(getSimpleTriangle(), "triangle.lp");
        transformData(getSimpleRectangle(), "rectangle.lp");
        //transformData(getRandomDistantPoints(), "random.lp");
    }

    private static void transformData(List<Position> items, String fileName) {
        Writer writer = new TransformerWriter(fileName);
        writer.open();

        LPFileGenerator lpFileGenerator = new LPFileGenerator(writer);
        LPTransformer lpTransformer = new LPTransformer(items, lpFileGenerator);
        lpTransformer.transform();

        writer.close();
    }

    private static List<Position> getSimpleTriangle() {
        List<Position> items = new ArrayList<>();

        items.add(new Position(0, 0));
        items.add(new Position(0.5, 0));
        items.add(new Position(0.5, 0.5));

        return items;
    }

    private static List<Position> getSimpleRectangle() {
        List<Position> items = new ArrayList<>();

        items.add(new Position(0, 0));
        items.add(new Position(0.5, 0));
        items.add(new Position(0, 2));
        items.add(new Position(0.5, 2));

        return items;
    }

    private static List<Position> getRandomDistantPoints() {
        List<Position> items = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            Position pos = new Position(Math.random() * 10, Math.random() * 10, Math.random() * 10,
                    Math.random() * 10, Math.random() * 10, Math.random() * 10, Math.random() * 10,
                    Math.random() * 10, Math.random() * 10, Math.random() * 10);
            items.add(pos);
        }

        return items;
    }

}
