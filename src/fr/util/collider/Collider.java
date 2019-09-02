package fr.util.collider;

import fr.util.point.Point;

public class Collider {

	public static boolean AABBvsAABB(AABB a, AABB b) {
		if (a.max().x < b.min().x || a.min().x > b.max().x)
			return false;
		if (a.max().y < b.min().y || a.min().y > b.max().y)
			return false;

		return true;
	}

	public static boolean AABBvsCircle(AABB a, Circle b) {
		// Vector from A to B
		Point aCenter = a.getCenter();
		Point amin = a.min();
		Point amax = a.max();

		Point n = new Point(b.getCenter()).sub(aCenter);

		// Closest point on A to center of B
		Point closest = new Point(n);

		// Calculate half extents along each axis
		double xExtent = (amax.x() - amin.x()) / 2;
		double yExtent = (amax.y() - amin.y()) / 2;

		// Clamp point to edges of the AABB
		closest.x(clamp(xExtent, -xExtent, closest.x()));
		closest.y(clamp(yExtent, -yExtent, closest.y()));

		return closest.equals(n) || closest.lengthSquarred() < b.rad();
	}

	public static boolean AABBvsPoint(AABB a, Point b) {
		return a.min().x < b.x && b.x < a.max().x && a.min().y < b.y && b.y < a.max().y;
	}

	public static boolean CirclevsCircle(Circle a, Circle b) {
		double r = a.rad() + b.rad();
		r *= r;

		Point ac = a.getCenter(), bc = b.getCenter();

		return r < Math.pow(ac.x() + bc.x(), 2) + Math.pow(ac.y() + bc.y(), 2);
	}

	public static double clamp(double val, double min, double max) {
		return Math.max(min, Math.min(max, val));
	}
}
