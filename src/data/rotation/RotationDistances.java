package data.rotation;

import data.Distances;
import geometry.Point;
import java.util.Random;

/**
 *
 * @author Antonin Pavelka
 */
public class RotationDistances implements Distances {

	private Random random = new Random(1);
	private PointSequence[] items;

	public RotationDistances(int numberOfObjects) {
		items = new PointSequence[numberOfObjects];
		for (int i = 0; i < items.length; i++) {
			items[i] = new PointSequence(createRandomRotation());
		}
	}

	@Override
	public double getDistance(int index1, int index2) {
		PointSequence item1 = items[index1];
		PointSequence item2 = items[index2];
		return item1.getRmsd(item2);
	}

	/**
	 * Creates a pair of random unit vectors with uniform radial distribution, perpendicular to each other.
	 */
	private Point[] createRandomRotation() {
		Point x = createRandomUnit();
		boolean found = false;
		Point z = null;
		while (!found) {
			Point y = createRandomUnit();
			z = x.cross(y); // perpendicular to x
			double dot = Math.abs(y.dot(z));
			found = dot < 0.00001; // defence against degenerate x, y pair
		}
		Point[] rotation = {x, z};
		return rotation;
	}

	private Point createRandomUnit() {
		double phi = randomRange(0, Math.PI * 2);
		double costheta = randomRange(-1, 1);
		double theta = Math.acos(costheta);
		double x = Math.sin(theta) * Math.cos(phi);
		double y = Math.sin(theta) * Math.sin(phi);
		double z = Math.cos(theta);
		return new Point(x, y, z);
	}

	private double randomRange(double min, double max) {
		return random.nextDouble() * (max - min) + min;
	}

	public int size() {
		return items.length;
	}

	/*public static void main(String[] args) {
		RotationDistances rd = new RotationDistances(1000);
		for (int i = 0; i < 100; i++) {
			//System.out.println(rd.createRandomUnit().size());
			//System.out.println(rd.randomRange(10, 11));
			//Point[] rr = rd.createRandomRotation();
			//System.out.println(rr[0] + "   " + rr[1]);
		}
	}*/

}
