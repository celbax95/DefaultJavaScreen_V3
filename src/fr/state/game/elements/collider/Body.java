package fr.state.game.elements.collider;

import fr.state.game.elements.onscreen.GameObject;

public class Body {

	public float mass, invMass, inertia, invInertia;
	public float staticFriction;
	public float dynamicFriction;
	public float restitution;
	public final Shape shape;

	public final GameObject gameObject;

	public Body(GameObject go) {
		this.gameObject = go;
		this.shape = go.getShape();
	}

	public void update() {
		this.shape.update(this.gameObject.getPos());
	}
}
