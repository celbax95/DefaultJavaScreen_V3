package fr.state.game.elements.collider;

public class Collisions {

	private static CollisionType[][] collisionTypes = { { AABBvsAABB.instance } };

	public static boolean collide(Manifold manifold, int t1, int t2) {
		if (manifold.g1.isAffectedBy(manifold.g2) || manifold.g2.isAffectedBy(manifold.g1)) {
			if (collisionTypes[t1][t2].collide(manifold)) {
				manifold.g1.interractWith(manifold.g2);
				manifold.g2.interractWith(manifold.g1);
				return true;
			}
			return false;
		} else
			return false;
	}
}