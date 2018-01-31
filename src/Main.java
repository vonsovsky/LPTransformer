import data.Variable;
import output.TransformerWriter;
import output.Writer;
import transformation.LPFileGenerator;
import transformation.LPTransformer;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        //transformData(getSimpleTriangle(), "triangle.lp");
        //transformData(getSimpleRectangle(), "rectangle.lp");
        transformData(getRandomDistantPoints(), "random.lp");
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

        items.add(new Variable("x1", 0, 0));
        items.add(new Variable("x2", 0.5, 0));
        items.add(new Variable("x3", 0.5, 0.5));

        return items;
    }

    private static List<Variable> getSimpleRectangle() {
        List<Variable> items = new ArrayList<>();

        items.add(new Variable("x1", 0, 0));
        items.add(new Variable("x2", 0.5, 0));
        items.add(new Variable("x3", 0, 2));
        items.add(new Variable("x4", 0.5, 2));

        return items;
    }

    private static List<Variable> getRandomDistantPoints() {
        List<Variable> items = new ArrayList<>();
		int max = 300;
		int dim = 3;
        for (int i = 0; i < 100; i++) {
			double[] r = new double[dim];
			for (int d = 0 ; d < dim; d++) {
				r[d] = Math.random() * max;
			}
            Variable pos = new Variable("x" + (i + 1), r);
            items.add(pos);
        }

        return items;
    }
	
	private static List<Variable> getRandomDistantRotations() {
        List<Variable> items = new ArrayList<>();
		int max = 300;
		int dim = 3;
        for (int i = 0; i < 100; i++) {
			double[] r = new double[dim];
			for (int d = 0 ; d < dim; d++) {
				r[d] = Math.random() * max;
			}
            Variable pos = new Variable("x" + (i + 1), r);
            items.add(pos);
        }

        return items;
    }

}
