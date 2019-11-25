package fr.state.game.elements.collider;

import fr.util.point.Point;

public class AABBvsAABB implements CollisionType {

	public static final AABBvsAABB instance = new AABBvsAABB();

	@Override
	public boolean collide(Manifold manifold) {

		AABB s1 = (AABB) manifold.b1.shape;
		AABB s2 = (AABB) manifold.b2.shape;

		Point n = new Point(s1.getCenter()).sub(s2.getCenter());

		double aExtent = (s1.getMax().x() - s1.getMin().x()) / 2;
		double bExtent = (s2.getMax().x() - s2.getMin().x()) / 2;

		// Calculate overlap on x axis
		double xOverlap = aExtent + bExtent - Math.abs(n.x());

		// SAT test on x axis
		if (xOverlap > 0) {

			// Calculate half extents along x axis for each object
			aExtent = (s1.getMax().y() - s1.getMin().y()) / 2;
			bExtent = (s2.getMax().y() - s2.getMin().y()) / 2;

			// Calculate overlap on y axis
			double yOverlap = aExtent + bExtent - Math.abs(n.y());

			// SAT test on y axis
			if (yOverlap > 0) {
				// Find out which axis is axis of least penetration
				if (xOverlap < yOverlap) {
					// Point towards B knowing that n points from A to B
					if (n.x() < 0) {
						manifold.normal = new Point(-1, 0);
					} else {
						manifold.normal = new Point(1, 0);
					}
					manifold.penetration = xOverlap;
					return true;
				} else {
					// Point toward B knowing that n points from A to B
					if (n.y() < 0) {
						manifold.normal = new Point(0, -1);
					} else {
						manifold.normal = new Point(0, 1);
					}
					manifold.penetration = yOverlap;
					return true;
				}
			}
		}
		return false;
	}

}
