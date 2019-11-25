package fr.state.game.elements.collider;

import fr.state.game.elements.onscreen.GameObject;

public class Body {

	public double mass, invMass, inertia, invInertia;
	public double staticFriction;
	public double dynamicFriction;
	public double restitution;
	public final Shape shape;

	public final GameObject gameObject;

	public Body(GameObject go, Shape shape) {
		this.gameObject = go;
		this.shape = shape;
		this.defaultData();
	}

	private void defaultData() {
		double MASS = 100;
		this.mass = MASS;
		this.invMass = 1 / this.mass;
		this.staticFriction = 0.5;
		this.restitution = 0.2;
		this.dynamicFriction = 0.3;
	}

	public void update() {
		this.shape.update(this.gameObject.getPos());
	}
}
