package fr.state.game.elements.collider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.state.game.elements.onscreen.GameObject;

public class Collider {

	private static final double EPSILON = 0.0001;

	private List<Manifold> collisions;

	private double averageGravity;

	public Collider() {
		this.collisions = new ArrayList<>();
		this.averageGravity = 0;
	}

	public void clear() {
		this.collisions.clear();
	}

	public void correctPositions() {
		for (Manifold m : this.collisions) {
			m.positionalCorrection();
		}
	}

	public void initCollisions() {
		for (Manifold m : this.collisions) {
			m.init();
		}
	}

	public void searchCollisions(Collection<GameObject> gameObjects) {
		int a1 = 0, a2 = 0;
		for (GameObject g1 : gameObjects) {
			if (g1.getBody() == null) {
				continue;
			}
			for (GameObject g2 : gameObjects) {
				if (a2++ <= a1) {
					continue;
				}
				if (g2.getBody() == null) {
					continue;
				}

				Manifold m = new Manifold(g1, g2);

				if (m.solve()) {
					this.collisions.add(m);
				}
			}
			a1++;
			a2 = 0;
		}
	}

	public void setAvergaeGravity(double averageGravity) {
		this.averageGravity = averageGravity + EPSILON;
	}

	public void solveCollisions() {
		for (Manifold m : this.collisions) {
			m.applyImpulse();
		}
	}

}
