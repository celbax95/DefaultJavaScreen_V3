package fr.state.game.elements.collider;

public class Collisions {

	private static CollisionType[][] collisionTypes = { { AABBvsAABB.instance } };

	public static boolean collide(Manifold manifold, int t1, int t2) {
		return collisionTypes[t1][t2].collide(manifold);
	}
}
