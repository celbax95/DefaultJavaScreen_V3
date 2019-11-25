package fr.state.game.elements.collider;

import fr.state.game.elements.onscreen.GameObject;
import fr.util.point.Point;

public class Manifold {

	public static final float PENETRATION_ALLOWANCE = 0.05f;
	public static final float PENETRATION_CORRETION = 0.4f;

	GameObject g1;
	Body b1;
	GameObject g2;
	Body b2;

	public double penetration;
	public Point normal = new Point();
	public Point[] contacts = { new Point(), new Point() };
	public int contactCount;
	public double averageRestitution;
	public double dynamicFriction;
	public double staticFriction;

	public Manifold(GameObject g1, GameObject g2) {
		this.g1 = g1;
		this.g2 = g2;
		this.b1 = this.g1.getBody();
		this.b2 = this.g2.getBody();
	}

	public void applyImpulse() {
		if (this.b1.invMass + this.b2.invMass == 0) {
			this.infiniteMassCorrection();
			return;
		}

		Point rv = new Point(this.g1.getVelocity()).sub(this.g2.getVelocity());

		double contactVel = rv.clone().dot(this.normal);

		if (contactVel > 0)
			return;

		double j = -(1.0D + this.averageRestitution) * contactVel;
		j /= this.b1.invMass + this.b2.invMass;

		Point impulse = this.normal.clone().mult(j);

		Point vel = this.g1.getVelocity();
		this.g1.setVelocity(vel.add(impulse.clone().neg()));

		vel = this.g2.getVelocity();
		this.g2.setVelocity(vel.add(impulse));

		// Friction

		rv = new Point(this.g1.getVelocity()).sub(this.g2.getVelocity());

		Point t = rv.clone();
		t.add(this.normal);
		t.trigNorm();

		double jt = -(1.0D + this.averageRestitution) * contactVel;
		jt /= this.b1.invMass + this.b2.invMass;

		if (jt == 0)
			return;

		Point tangentImpulse;
		if (StrictMath.abs(jt) < j * this.staticFriction) {
			tangentImpulse = t.clone().mult(jt);
		} else {
			tangentImpulse = t.clone().mult(j).mult(-this.dynamicFriction);
		}

		vel = this.g1.getVelocity();
		this.g1.setVelocity(vel.add(tangentImpulse.clone().neg()));

		vel = this.g2.getVelocity();
		this.g2.setVelocity(vel.add(tangentImpulse));
	}

	private void infiniteMassCorrection() {
		this.g1.setVelocity(new Point());
		this.g2.setVelocity(new Point());
	}

	public void init() {
		this.averageRestitution = Math.min(this.b1.restitution, this.b2.restitution);

		this.staticFriction = Math.sqrt(
				this.b1.staticFriction * this.b1.staticFriction + this.b2.staticFriction * this.b2.staticFriction);
		this.dynamicFriction = Math.sqrt(
				this.b1.dynamicFriction * this.b1.dynamicFriction + this.b2.dynamicFriction * this.b2.dynamicFriction);

		// TODO ya peut etre un problème ici
		Point rv = new Point(this.g1.getVelocity()).sub(this.g2.getVelocity());

		if (rv.lengthSquarred() < 0.001) {
			this.averageRestitution = 0.0f;
		}
	}

	public void positionalCorrection() {
		double correction = Math.max(this.penetration - 2, PENETRATION_ALLOWANCE) / (this.b1.invMass + this.b2.invMass)
				* PENETRATION_CORRETION;

		Point p = this.g1.getPos();
		p.add(this.normal.clone().mult(-this.b1.invMass * correction));
		this.g1.setPos(p);
		p = this.g2.getPos();
		p.add(this.normal.clone().mult(-this.b2.invMass * correction));
		this.g2.setPos(p);
	}

	public void solve() {
		int i1 = this.b1.shape.getType().ordinal();
		int i2 = this.b2.shape.getType().ordinal();

		Collisions.collide(this, i1, i2);
	}
}
