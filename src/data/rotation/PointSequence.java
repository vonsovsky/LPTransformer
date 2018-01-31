package data.rotation;

import geometry.Point;

/**
 *
 * @author Antonin Pavelka
 */
public class PointSequence {

	private Point[] points;

	public PointSequence(Point[] points) {
		this.points = points;
	}

	public double getRmsd(PointSequence other) {
		if (this.size() != other.size()) {
			throw new RuntimeException();
		}
		double rmsd = 0;
		for (int i = 0; i < size(); i++) {
			Point point1 = this.points[i];
			Point point2 = other.points[i];
			rmsd += point1.squaredDistance(point2);
		}
		rmsd /= size();
		rmsd = Math.sqrt(rmsd);
		return rmsd;
	}

	private int size() {
		return points.length;
	}
}
