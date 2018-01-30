import data.Variable;
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

    private static void transformData(List<Variable> items, String fileName) {
        Writer writer = new TransformerWriter(fileName);
        writer.open();

        LPFileGenerator lpFileGenerator = new LPFileGenerator(writer);
        LPTransformer lpTransformer = new LPTransformer(items, lpFileGenerator);
        lpTransformer.transform();

        writer.close();
    }

    private static List<Variable> getSimpleTriangle() {
        List<Variable> items = new ArrayList<>();

        items.add(new Variable(0, 0));
        items.add(new Variable(0.5, 0));
        items.add(new Variable(0.5, 0.5));

        return items;
    }

    private static List<Variable> getSimpleRectangle() {
        List<Variable> items = new ArrayList<>();

        items.add(new Variable(0, 0));
        items.add(new Variable(0.5, 0));
        items.add(new Variable(0, 2));
        items.add(new Variable(0.5, 2));

        return items;
    }

    private static List<Variable> getRandomDistantPoints() {
        List<Variable> items = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            Variable pos = new Variable(Math.random() * 10, Math.random() * 10, Math.random() * 10,
                    Math.random() * 10, Math.random() * 10, Math.random() * 10, Math.random() * 10,
                    Math.random() * 10, Math.random() * 10, Math.random() * 10);
            items.add(pos);
        }

        return items;
    }

}
