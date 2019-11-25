package fr.state.game.elements.collider;

public class Collisions {

	private static CollisionType[][] collisionTypes = { { AABBvsAABB.instance } };

	public static void collide(Manifold manifold, int t1, int t2) {
		collisionTypes[t1][t2].collide(manifold);
	}
}
